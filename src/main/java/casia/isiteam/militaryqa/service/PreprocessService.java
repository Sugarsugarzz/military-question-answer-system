package casia.isiteam.militaryqa.service;

import casia.isiteam.militaryqa.common.AliasMapper;
import casia.isiteam.militaryqa.common.Constant;
import casia.isiteam.militaryqa.mapper.AnswerMapper;
import casia.isiteam.militaryqa.mapper.ResultMapper;
import casia.isiteam.militaryqa.model.DictMatcher;
import casia.isiteam.militaryqa.model.Result;
import casia.isiteam.militaryqa.utils.EntityAliasExtractor;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Slf4j
@Service
public class PreprocessService {

    @Autowired
    ResultMapper resultMapper;
    @Autowired
    AnswerMapper answerMapper;
    @Autowired
    EntityAliasExtractor entityAliasExtractor;

    public static final String rootpath= "";

    /*
    ===================================================================================================================
      一、将自定义的 concept 和 entity 别名上传到对应 sameas 表中的工具
    ===================================================================================================================
     */
    public void addConceptAliasToDB() {
        log.info("正在将自定义 concept 别名导入数据库...");

        // 获取 concepts 表中 concept_id 和 concept_name 键值关系
        List<Result> results = resultMapper.getConcepts();
        Map<String, Integer> map = new HashMap<>();
        for (Result result : results) {
            map.put(result.getConcept_name(), result.getC_id());
        }
        log.info("获取 concept_name 与 id 键值关系完成.");

        // 清空 concept_sameas 表
        resultMapper.emptyConceptSameas();
        log.info("清空 concept_sameas 表完成.");

        // 读取 data/dict_for_sameas 下的自定义别名库，上传到数据库对应 sameas 表中
        String filepath = rootpath + "data/dict_for_sameas/concepts_alias.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = br.readLine()) != null) {
                String key = str.split("：")[0];
                String[] aliases = str.split("：")[1].split("\\|");
                for (String alias : aliases) {
                    resultMapper.insertConceptSameas(alias, map.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("自定义 concept 别名导入数据库完成！");
    }

    public void addEntityAliasToDB() {
        log.info("正在将自定义 entity 别名导入数据库...");

        // 清空 entity_sameas 表
        resultMapper.emptyEntitySameas();
        log.info("清空 entity_sameas 表完成.");

        // 获取 entities 表中的所有实体
        List<Result> results = resultMapper.getEntities();
        for (Result result : results) {
            log.info("正在处理 " + result + "...");
            // 根据 entity_name，获取所有别名
            Set<String> aliases = entityAliasExtractor.getEntityAlias(result.getEntity_id(), result.getEntity_name());
            // 保存别名
            entityAliasExtractor.saveEntityAlias(result.getEntity_id(), result.getEntity_name(), aliases);
        }

        log.info("自定义 entity 别名导入数据库完成！");
    }

    /*
    ===================================================================================================================
      二、将country.txt、most.txt、compare.txt、entities(entity_sameas)表、concepts(concept_sameas)表的数据存入match_dict表中
    ===================================================================================================================
     */

    /**
     * 将 country.txt most.txt compare.txt 键值信息存入 match_dict 数据库表
     */
    public void getCountryCompareMostToDB() {
        log.info("正在将 country、most 和 compare 文件的映射信息存入 match_dict 表...");
        // 获取文件路径
        File file = new File(rootpath + "data/dict_for_basic");
        List<String> file_list = new ArrayList<>();
        for (String filename : file.list()) {
            if (filename.equals("country.txt") || filename.equals("most.txt") || filename.equals("compare.txt")) {
                file_list.add(rootpath + "data/dict_for_basic/" + filename);
            }
        }
        log.info("获取文件路径成功.");

        // 清空 compare、country、most 标签项
        resultMapper.emptyMatchDictByLabel(Arrays.asList("compare", "country", "most"));
        log.info("清空 match_dict 表 compare、country、most 字段完成.");

        // 存入数据库
        for (String filepath : file_list) {
            String[] names = filepath.split("/");
            String label = names[names.length - 1].replace(".txt", "");
            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String str;
                while ((str = br.readLine()) != null) {
                    resultMapper.insertMatchDict(str.split("：")[0], str.split("：")[1], label);
                }
            } catch (FileNotFoundException e) {
                log.error(filepath + " - 写文件未找到！", e);
            } catch (IOException e) {
                log.error(filepath + " - 写文件发生错误！", e);
            }
        }

        log.info("将 country、most 和 compare 文件的映射信息存入 match_dict 表完成！");
    }

    /**
     * 将 big_category、small_category、attributes 存入match_dict 数据库表
     */
    public void getConceptsAndSameasToDB() {
        log.info("正在将 concepts 表中的 big_category、small_category 和 attributes 的映射信息存入 match_dict 表...");
        // 存 concept_name - 标签和别名
        Map<String, String[]> map = new HashMap<>();

        // 获取概念库
        List<Result> results = resultMapper.getConceptsByLevel(Arrays.asList(0, 2, 3));
        for (Result result : results) {
            String label = null;
            switch (result.getLevel()) {
                case 0:
                    label = "attribute";
                    break;
                case 2:
                    label = "big_category";
                    break;
                case 3:
                    label = "small_category";
                    break;
            }
            String[] tuple = new String[2];
            tuple[0] = label;
            tuple[1] = result.getConcept_name();
            map.put(result.getConcept_name(), tuple);
        }

        // 再获取概念别名库
        results = resultMapper.getConceptsSameas();
        for (Result result : results) {
            String concept1 = result.getConcept1();
            String concept2 = result.getConcept2();
            String[] tuple = map.get(concept1);
            tuple[1] = map.get(concept1)[1] + "|" + concept2.replace(" ", "");
            map.put(concept1, tuple);
        }
        log.info("获取 concepts 和别名成功.");

        // 清空 attribute、big_category、small_category 标签项
        resultMapper.emptyMatchDictByLabel(Arrays.asList("attribute", "big_category", "small_category"));
        log.info("清空 match_dict 表 attribute、big_category、small_category 字段完成.");

        // 存入 match_dict 表
        for (String key : map.keySet()) {
            resultMapper.insertMatchDict(key, map.get(key)[1], map.get(key)[0]);
        }
        log.info("将 concepts 表中的 big_category、small_category 和 attributes 的映射信息存入 match_dict 表完成！");
    }


    /**
     * 将 entity及别名 存入 match_dict 数据库表
     */
    public void getEntitiesAndSameasToDB() {

        log.info("正在将 entities 表及其别名的映射信息存入 match_dict 表...");

        Map<String, String> map = new HashMap<>();

        // 先获取实体库、再获取实体别名库
        List<Result> results = resultMapper.getEntitiesSameas();
        for (Result result : results) {
            if (!map.containsKey(result.getEntity_name_1())) {
                map.put(result.getEntity_name_1(), result.getEntity_name_1().replace(" ", ""));
            }
            map.put(result.getEntity_name_1(), map.get(result.getEntity_name_1()) + "|" + result.getEntity_name_2().replace(" ", ""));
        }
        log.info("获取 entities 和别名成功.");

        // 清空 entity 标签项
        resultMapper.emptyMatchDictByLabel(Arrays.asList("entity"));
        log.info("清空 match_dict 表 entity 字段完成.");

        // 存入 match_dict 表
        for (String key : map.keySet()) {
            resultMapper.insertMatchDict(key, map.get(key), "entity");
        }
        log.info("将 entities 表及其别名的映射信息存入 match_dict 表完成！");
    }

    /*
    ===================================================================================================================
      三、将 match_dict 表中的数据获取到本地，加载到HanLP分词器的自定义词典中（未生效可以先删除custom/CustomDictionary.txt.bin）
    ===================================================================================================================
     */
//    /**
//     * 根据数据库的 match_dict 表，获取分词词典到本地，到 data/dict_for_segment 目录下
//     */
//    public void getDBToSegmentDict() {
//
//        logger.info("正在获取 match_dict 表到本地 data/dict_for_segment 目录下...");
//
//        // 存（所属标签 - 词条）
//        Map<String, Set<String>> map = new HashMap<>();
//        // 获取match_dict表信息
//        List<Result> matchers = resultMapper.getSimpleMatchDict();
//        for (Result matcher : matchers) {
//            if (!map.containsKey(matcher.getLabel())) {
//                map.put(matcher.getLabel(), new HashSet<>());
//            }
//            for (String alias : matcher.getAlias().split("\\|")) {
//                map.get(matcher.getLabel()).add(alias);
//            }
//        }
//        logger.info("获取 match_dict 成功.");
//
//        // 存入本地txt
//        for (String key : map.keySet()) {
//            String filepath = rootpath + "data/dict_for_segment/" + key + ".txt";
//            try {
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
//                for (String s : map.get(key)) {
//                    bw.write(s + "\n");
//                }
//                bw.flush();
//                bw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        logger.info("获取 match_dict 表到本地 data/dict_for_segment 目录下成功！");
//    }

    /**
     * 根据数据库的 match_dict 表，读取到 HanLP 的 CustomDictionary 中
     */
    public void getDBToCustomDictionary() {

        log.info("【MatchDict】正在获取 match_dict 表到 HanLP - CustomDictionary...");

        // 存（所属标签 - 词条）
        Map<String, Set<String>> map = new HashMap<>();
        // 获取match_dict表信息
        List<Result> matchers = resultMapper.getSimpleMatchDict();
        for (Result matcher : matchers) {
            if (!map.containsKey(matcher.getLabel())) {
                map.put(matcher.getLabel(), new HashSet<>());
            }
            for (String alias : matcher.getAlias().split("\\|")) {
                map.get(matcher.getLabel()).add(alias);
            }
        }
        log.info("【MatchDict】获取 match_dict 成功.");

        // label -> nature
        Map<String, String> natureMap = new HashMap<>();
        natureMap.put("country", "n_country");
        natureMap.put("entity", "n_entity");
        natureMap.put("attribute", "n_attr");
        natureMap.put("big_category", "n_big");
        natureMap.put("small_category", "n_small");
        natureMap.put("compare", "n_compare");
        natureMap.put("most", "n_most");

        // 加载到 CustomDictionary
        map.keySet().forEach(label -> {
            map.get(label).forEach(word -> {
                CustomDictionary.add(word, natureMap.get(label));
            });
        });

        log.info("【MatchDict】补充到 CustomDictionary 成功！");
    }

    /**
     * 程序首次启动，初始化 CustomDictionary 和 AliasMapper
     * */
    public void initCustomDictionaryAndAliasMapper() {
        log.info("【InitCustomDictionaryAndAliasMapper】正在获取 match_dict 表，并初始化 HanLP(CustomDictionary) 和 AliasMapper...");

        List<DictMatcher> matchers = answerMapper.getMatchDict();
        log.info("match_dict 加载成功，size：{}", matchers.size());


        for (DictMatcher matcher : matchers) {
            String[] aliases = matcher.getAlias().split("\\|");

            // init CustomDictionary
            for (String alias : aliases) {
                CustomDictionary.add(alias, Constant.labelNatureMap.get(matcher.getLabel()));
            }

            // init AliasMapper
            if ("entity".equals(matcher.getLabel())) {
                Arrays.stream(aliases).forEach(alias -> {
                    if (!AliasMapper.Entity.containsKey(alias)) {
                        // 一个别名对应多个实体
                        AliasMapper.Entity.put(alias, new HashSet<>());
                    }
                    AliasMapper.Entity.get(alias).add(matcher.getWord());
                });
            } else {
                switch (matcher.getLabel()) {
                    case "country":
                        Arrays.stream(aliases).forEach(alias -> AliasMapper.Country.put(alias, matcher.getWord())); break;
                    case "big_category":
                        Arrays.stream(aliases).forEach(alias -> AliasMapper.BigCategory.put(alias, matcher.getWord())); break;
                    case "small_category":
                        Arrays.stream(aliases).forEach(alias -> AliasMapper.SmallCategory.put(alias, matcher.getWord())); break;
                    case "attribute":
                        Arrays.stream(aliases).forEach(alias -> AliasMapper.Attribute.put(alias, matcher.getWord())); break;
                    case "compare":
                        Arrays.stream(aliases).forEach(alias -> AliasMapper.Compare.put(alias, matcher.getWord())); break;
                    case "most":
                        Arrays.stream(aliases).forEach(alias -> AliasMapper.Most.put(alias, matcher.getWord())); break;
                    default:  break;
                }
            }
        }

        log.info("【InitCustomDictionaryAndAliasMapper】初始化成功.");
    }

    /**
     * 清空DictMapper
     */
    private void clearDictMapper() {
        AliasMapper.Country.clear();
        AliasMapper.BigCategory.clear();
        AliasMapper.SmallCategory.clear();
        AliasMapper.Entity.clear();
        AliasMapper.Attribute.clear();
        AliasMapper.Compare.clear();
        AliasMapper.Most.clear();
    }
}
