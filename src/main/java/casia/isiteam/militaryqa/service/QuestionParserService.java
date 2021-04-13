package casia.isiteam.militaryqa.service;

import casia.isiteam.militaryqa.common.Constant;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class QuestionParserService {

    Map<String, List<String>> parserDict = new HashMap<>();

    /**
     * 初始化词性字典(每轮都调用初始化，防止模式多次叠加)
     */
    private void buildParserDict() {
        for (String nature: Constant.natures) {
            parserDict.put(nature, new ArrayList<>());
        }
        // pattern存问句词性模式
        parserDict.put("pattern", new ArrayList<>());
    }

    /**
     * 问句模式解析
     * @return 词性字典
     */
    public Map<String, List<String>> parser(String question) {

        // 初始化词性字典，防止多次查询模式叠加
        buildParserDict();

        // 首先判断是否是 <热点> <期刊> <报告> 和 <直达> 问题，否则，转 <百科> 和 <对比> 查询
        boolean flag = checkQuestion(question);
        if (flag) {
            return parserDict;
        }

        // 识别出问句中的<时间>，加入分词器词典
        Matcher m_time = Pattern.compile("[0-9]{4}年([0-9]{0,2}月)?([0-9]{0,2}日)?").matcher(question);
        while (m_time.find()) {
            CustomDictionary.add(m_time.group(), "n_time");
        }

        // 识别出问句中的<单位数值>，加入分词器词典
        String[] units = { "海里", "英里", "吨", "公里", "公里/节", "公里/小时", "毫米", "节", "克", "里", "米", "千克", "千米",
                "千米/时", "千米/小时", "千米每小时", "余英里", "约海里", "最大海里", "厘米", "分米", "人", "位"};
        String unit_regex = String.format("([0-9]+(.[0-9]+)?)(%s)+", String.join("|", units));
        Matcher m_unit = Pattern.compile(unit_regex).matcher(question);
        while (m_unit.find()) {
            CustomDictionary.add(m_unit.group(), "n_unit");
        }

        // 利用HanLP分词，遍历词和词性
        List<Term> terms = HanLP.segment(question);
        for (Term term : terms) {
//            System.out.println(term.nature + " - " + term.word);
            if (Constant.natures.contains(term.nature.toString())) {
                parserDict.get(term.nature.toString()).add(term.word);
                parserDict.get("pattern").add(term.nature.toString());
            }
        }

//        log.info("问句解析完成。");
        log.info("词性匹配情况：{}", parserDict);
        log.info("问句模式：{}", parserDict.get("pattern"));

        return parserDict;
    }

    /**
     * 判断是否是 <热点>、<期刊>、<报告>、<直达> 问题，对于 <热点>、<期刊>、<报告> 问题提取时间和关键词
     * @param question 问句
     * @return 标志符
     */
    private boolean checkQuestion(String question) {

        if (question.contains("热点") || question.contains("REDIAN")) {
            parserDict.get("pattern").add("3");
            question = question.replace("热点", "").replace("REDIAN", "").replace("新闻", "").replace("想", "").replace("现在", "");
            extractTimeAndKeywords(question);
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：热点查询模式");
        }

        else if (question.contains("期刊") || question.contains("QIKAN")) {
            parserDict.get("pattern").add("4");
            question = question.replace("期刊", "").replace("QIKAN", "").replace("新闻", "").replace("想", "").replace("现在", "");
            extractTimeAndKeywords(question);
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：期刊查询模式");
        }

        else if (question.contains("报告") || question.contains("BAOGAO")) {
            parserDict.get("pattern").add("5");
            question = question.replace("报告", "").replace("BAOGAO", "").replace("新闻", "").replace("想", "").replace("现在", "");
            extractTimeAndKeywords(question);
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：报告查询模式");
        }

        else if (question.contains("头条") || question.contains("TOUTIAO")) {
            parserDict.get("pattern").add("61");
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：直达 - 头条");
        }

        else if (question.contains("百科") || question.contains("BAIKE")) {
            parserDict.get("pattern").add("62");
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：直达 - 百科");
        }

        else if (question.contains("订阅") || question.contains("DINGYUE")) {
            parserDict.get("pattern").add("63");
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：直达 - 订阅");
        }

        else if (question.contains("我的收藏") || question.contains("WODESHOUCANG") || question.contains("收藏") || question.contains("SHOUCANG")) {
            parserDict.get("pattern").add("64");
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：直达 - 我的收藏");
        }

        else if (question.contains("浏览历史") || question.contains("LIULANLISHI") || question.contains("浏览") || question.contains("LIULAN")) {
            parserDict.get("pattern").add("65");
            log.info("词性匹配情况：{}", parserDict);
            log.info("问句模式：直达 - 浏览历史");
        }

        else {
            return false;
        }

        return true;
    }

    /**
     * 提取问句中的起始、结束时间，以及关键词
     * @param question 问句
     */
    private void extractTimeAndKeywords(String question) {
        // 提取 起始时间 和 结束时间
        Map<String, TimeUnit> timeResults = new TimeNormalizer().parse(question);
        for (String key : timeResults.keySet()) {
            if (question.contains("神舟") && key.contains("号")) {
                continue;
            }
            question = question.replace(key, "");
            parserDict.get("n_time").add(DateUtil.formatDateDefault(timeResults.get(key).getTime()));
        }
        // 提取 关键词
        parserDict.get("keywords").addAll(HanLP.extractKeyword(question, 5));
        // 针对 n_time 长度为一的情况，将对应字段也加入，以便后面识别加入end_time
        if (parserDict.get("n_time").size() == 1) {
            for (String key : timeResults.keySet()) {
                if (question.contains("神舟") && key.contains("号")) {
                    continue;
                }
                parserDict.get("n_unit").add(key);
            }
        }
    }
}
