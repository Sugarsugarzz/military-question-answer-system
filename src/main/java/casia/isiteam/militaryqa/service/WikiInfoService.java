package casia.isiteam.militaryqa.service;

import casia.isiteam.militaryqa.common.AliasMapper;
import casia.isiteam.militaryqa.common.Constant;
import casia.isiteam.militaryqa.mapper.master.ResultMapper;
import casia.isiteam.militaryqa.mapper.cluster.WikiInfoMapper;
import casia.isiteam.militaryqa.model.WikiInfo;
import casia.isiteam.militaryqa.utils.EntityAliasExtractor;
import casia.isiteam.militaryqa.utils.MultiQaUtil;
import casia.isiteam.militaryqa.utils.ProcessUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WikiInfoService {

    @Autowired
    WikiInfoMapper wikiInfoMapper;
    @Autowired
    ResultMapper resultMapper;


    /**
     * 预处理，将 wiki_info表数据解析到问答表
     */
    public void preprocessWikiInfoData() {
        while (true) {
            log.info("【数据解析模板】开始解析wiki_info数据...");
            Long cursor = resultMapper.getCursor();
            log.info("【数据解析模板】本次Cursor：{}", cursor);
            if (ObjectUtil.isEmpty(cursor)) {
                cursor = 0L;
            }
            long maxCursorId = cursor;

            List<Map<String, Object>> records = wikiInfoMapper.list(cursor, 100L);
            if (records.size() == 0) {
                log.info("【数据解析模板】本次无新数据. 休眠 10min.");
                try {
                    TimeUnit.MINUTES.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            log.info("【数据解析模板】获取新数据：{} 条", records.size());

            for (Map<String, Object> record : records) {
                if (ObjectUtil.isEmpty(record.get("auto_id")) || ObjectUtil.isEmpty(record.get("wikiID")) || ObjectUtil.isEmpty(record.get("name"))) {
                    log.error("该条wiki_info缺少必要字段！{}", record);
                    continue;
                }
                WikiInfo wikiInfo = new WikiInfo(Long.parseLong(record.get("auto_id").toString()),
                        record.get("wikiID").toString(),
                        record.get("name").toString(),
                        record.getOrDefault("summary", "").toString(),
                        getAlias(record.getOrDefault("othernames", "").toString(),
                                 record.getOrDefault("transnames", "").toString()),
                        getAttrBox(record.getOrDefault("infobox", "").toString()));
                log.info("wikiinfo: {}", wikiInfo);
                // 解析 wikiInfo 并存储
                analysisAndSave(wikiInfo);

                maxCursorId = wikiInfo.getAutoId();
            }
            if (maxCursorId != cursor) {
                resultMapper.updateCursor(maxCursorId);
            }
            log.info("【数据解析模板】本次解析结束，处理游标到：{}", maxCursorId);
        }
    }

    /**
     * 解析并存储 wikiInfo
     */
    private void analysisAndSave(WikiInfo wikiInfo) {

        // 新增到entities表
        Long entitiesMaxId = resultMapper.getEntitiesMaxId();
        Long entityId = ObjectUtil.isEmpty(entitiesMaxId) ? 1L : entitiesMaxId + 1;
        // 分类
        long cid = entityClassifyBySummary(wikiInfo.getSummary());
        try {
            resultMapper.saveEntities(entityId, wikiInfo.getName(), cid, wikiInfo.getWikiId());
        } catch (Exception e) {
            log.error("该实体已存在！wikiInfo:{} - error_msg:{}", wikiInfo, e.getMessage());
            return;
        }

        // 1.存储别名
        Set<String> aliases = wikiInfo.getAliases().stream().map(MultiQaUtil::preProcessQuestion).collect(Collectors.toSet());
        Set<String> entityAlias = EntityAliasExtractor.getEntityAlias(wikiInfo.getName());
        aliases.addAll(entityAlias);

        for (String alias : aliases) {
            // 新增到entity_sameas表
            resultMapper.saveEntitySameAs(alias, entityId);
            // 更新CustomDictionary
            CustomDictionary.add(alias, Constant.Nature_Entity);
            // 更新AliasMapper的Entity
            if (!AliasMapper.Entity.containsKey(alias)) {
                AliasMapper.Entity.put(alias, new HashSet<>());
            }
            AliasMapper.Entity.get(alias).add(wikiInfo.getName());
        }
        // 更新match_dict
        resultMapper.insertMatchDict(wikiInfo.getName(), String.join("|", aliases.toArray(new String[0])), Constant.Nature_Entity);

        // 2.存储属性
        Map<String, Object> attrBox = wikiInfo.getAttrBox();
        for (Map.Entry<String, Object> entry : attrBox.entrySet()) {
            String attrName = entry.getKey().replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
            String attrValue = entry.getValue().toString().replaceAll("[^a-zA-Z0-9.\\u4E00-\\u9FA5]", "");
            if (StringUtils.isEmpty(attrName) || StringUtils.isEmpty(attrValue)) {
                continue;
            }
            // 如果该属性名已存在，则无需重复加入CustomDictionary、AliasMapper的Attribute以及match_dict表
            Long conceptId = resultMapper.getConceptIdByName(attrName);
            if (ObjectUtil.isEmpty(conceptId)) {
                // 新增到concept_sameas表
                Long conceptsMaxId = resultMapper.getConceptsMaxId();
                conceptId = ObjectUtil.isEmpty(conceptsMaxId) ? 1L : conceptsMaxId + 1;
                resultMapper.saveConcept(conceptId, attrName);
                // 更新match_dict
                resultMapper.insertMatchDict(attrName, attrName, Constant.Nature_Attribute);
                // 更新CustomDictionary
                CustomDictionary.add(attrName, Constant.Nature_Attribute);
                // 更新AliasMapper的Attribute
                AliasMapper.Attribute.put(attrName, attrName);
            }
            // 新增到entity_attr表
            String[] attrReg = extractRegularAttr(attrValue);
            resultMapper.saveEntityAttr(entityId, conceptId, attrValue, attrReg[0], attrReg[1]);
        }
    }

    /** 从属性值提取标准化的属性值和属性单位 */
    private String[] extractRegularAttr(String attrValue) {
        String[] attrReg = new String[]{"", ""};
        if (attrValue.contains("年")) {
            String date = ProcessUtil.processExplicitTime(attrValue);
            attrReg[0] = date;
        } else {
            Matcher m = Pattern.compile("^([\\d.]+)(.*)").matcher(attrValue);
            if (m.find()) {
                attrReg[0] = m.group(1);
                attrReg[1] = m.group(2);
            }
        }
        return attrReg;
    }

    /** 根据 summary 字段对实体分类 */
    private long entityClassifyBySummary(String summary) {
        if (StrUtil.isEmpty(summary)) {
            return -1;
        }
        for (Map.Entry<String, Long> entry : Constant.smallCategoriesMapping.entrySet()) {
            if (summary.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return -1;
    }

    /** 根据 othernames 和 transnames 字段获取别名 */
    private Set<String> getAlias(String otherNames, String transNames) {
        // ["歼20","威龙"]
        Set<String> aliases = new HashSet<>();
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        if (!StringUtils.isEmpty(otherNames)) {
            Matcher matcher = pattern.matcher(otherNames);
            while (matcher.find()) {
                aliases.add(matcher.group(1));
            }
        }
        if (!StringUtils.isEmpty(transNames)) {
            Matcher matcher = pattern.matcher(transNames);
            while (matcher.find()) {
                aliases.add(matcher.group(1));
            }
        }
        return aliases;
    }

    /** 根据 infoBox 字段返回 属性名与属性值的映射Map */
    private Map<String, Object> getAttrBox(String attrBox) {
        // [{"infoName":"中文名称","infoValue":"1143.7型航空母舰"},{"infoName":"英文名称","infoValue":"Ulyanovsk"},{"infoName":"前型/级","infoValue":"1143.5型航空母舰和1153型航空母舰 [1]\u00A0"},{"infoName":"服役时间","infoValue":"未服役 [2]\u00A0"},{"infoName":"国\u00A0\u00A0\u00A0\u00A0家","infoValue":"苏联/乌克兰 [1]\u00A0"},{"infoName":"建造单位","infoValue":"尼古拉耶夫造船厂 [1]\u00A0"},{"infoName":"航\u00A0\u00A0\u00A0\u00A0速","infoValue":"30节（56千米/小时）"},{"infoName":"动力装置","infoValue":"4台KN-3核反应堆；4台蒸汽轮机"},{"infoName":"舰载机数","infoValue":"68架"}]
        Map<String, Object> attrs = new HashMap<>();
        if (!StringUtils.isEmpty(attrBox)) {
            try {
                JSONArray arrays = JSONArray.parseArray(attrBox);
                for (Object array : arrays) {
                    JSONObject obj = (JSONObject) array;
                    attrs.put(obj.getString("infoName"), obj.getString("infoValue"));
                }
            } catch (Exception e) {
                log.error("wiki_info表infoBox字段无法解析为JSON！{}", attrBox);
            }
        }
        return attrs;
    }
}
