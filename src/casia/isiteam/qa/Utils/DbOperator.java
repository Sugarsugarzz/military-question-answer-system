package casia.isiteam.qa.Utils;

import casia.isiteam.qa.Model.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class DbOperator {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static final String rootpath= "";

    /*
    ===================================================================================================================
      一、将自定义的 concept 和 entity 别名上传到对应 sameas 表中的工具
    ===================================================================================================================
     */
    public static void addConceptAliasToDB() {
        logger.info("正在将自定义 concept 别名导入数据库...");

        // 获取 concepts 表中 concept_id 和 concept_name 键值关系
        List<Result> results = DBKit.getConcepts();
        Map<String, Integer> map = new HashMap<>();
        for (Result result : results) {
            map.put(result.getConcept_name(), result.getC_id());
        }
        logger.info("获取 concept_name 与 id 键值关系完成.");

        // 清空 concept_sameas 表
        DBKit.emptyConceptSameas();
        logger.info("清空 concept_sameas 表完成.");

        // 读取 data/dict_for_sameas 下的自定义别名库，上传到数据库对应 sameas 表中
        String filepath = rootpath + "data/dict_for_sameas/concepts_alias.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = br.readLine()) != null) {
                String key = str.split("：")[0];
                String[] aliases = str.split("：")[1].split("\\|");
                for (String alias : aliases) {
                    DBKit.insertConceptSameas(alias, map.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("自定义 concept 别名导入数据库完成！");
    }

    public static void addEntityAliasToDB() {
        logger.info("正在将自定义 entity 别名导入数据库...");

        // 清空 entity_sameas 表
        DBKit.emptyEntitySameas();
        logger.info("清空 entity_sameas 表完成.");

        // 获取 entities 表中的所有实体
        List<Result> results = DBKit.getEntities();
        for (Result result : results) {
            logger.info("正在处理 " + result + "...");
            // 根据 entity_name，获取所有别名
            Set<String> aliases = EntityAliasExtractor.getEntityAlias(result.getEntity_id(), result.getEntity_name());
            // 保存别名
            EntityAliasExtractor.saveEntityAlias(result.getEntity_id(), result.getEntity_name(), aliases);
        }

        logger.info("自定义 entity 别名导入数据库完成！");
    }

    /*
    ===================================================================================================================
      二、将country.txt、most.txt、compare.txt、entities(entity_sameas)表、concepts(concept_sameas)表的数据存入match_dict表中
    ===================================================================================================================
     */

    /**
     * 将 country.txt most.txt compare.txt 键值信息存入 match_dict 数据库表
     */
    public static void getCountryCompareMostToDB() {
        logger.info("正在将 country、most 和 compare 文件的映射信息存入 match_dict 表...");
        // 获取文件路径
        File file = new File(rootpath + "data/dict_for_basic");
        List<String> file_list = new ArrayList<>();
        for (String filename : file.list()) {
            if (filename.equals("country.txt") || filename.equals("most.txt") || filename.equals("compare.txt")) {
                file_list.add(rootpath + "data/dict_for_basic/" + filename);
            }
        }
        logger.info("获取文件路径成功.");

        // 清空 compare、country、most 标签项
        DBKit.emptyMatchDictByLabel(Arrays.asList("compare", "country", "most"));
        logger.info("清空 match_dict 表 compare、country、most 字段完成.");

        // 存入数据库
        for (String filepath : file_list) {
            String[] names = filepath.split("/");
            String label = names[names.length - 1].replace(".txt", "");
            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String str;
                while ((str = br.readLine()) != null) {
                    DBKit.insertMatchDict(str.split("：")[0], str.split("：")[1], label);
                }
            } catch (FileNotFoundException e) {
                logger.error(filepath + " - 写文件未找到！", e);
            } catch (IOException e) {
                logger.error(filepath + " - 写文件发生错误！", e);
            }
        }

        logger.info("将 country、most 和 compare 文件的映射信息存入 match_dict 表完成！");
    }

    /**
     * 将 big_category、small_category、attributes 存入match_dict 数据库表
     */
    public static void getConceptsAndSameasToDB() {

        logger.info("正在将 concepts 表中的 big_category、small_category 和 attributes 的映射信息存入 match_dict 表...");
        // 存 concept_name - 标签和别名
        Map<String, String[]> map = new HashMap<>();

        // 获取概念库
        List<Result> results = DBKit.getConceptsByLevel(Arrays.asList(0, 2, 3));
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
        results = DBKit.getConceptsSameas();
        for (Result result : results) {
            String concept1 = result.getConcept1();
            String concept2 = result.getConcept2();
            String[] tuple = map.get(concept1);
            tuple[1] = map.get(concept1)[1] + "|" + concept2.replace(" ", "");
            map.put(concept1, tuple);
        }
        logger.info("获取 concepts 和别名成功.");

        // 清空 attribute、big_category、small_category 标签项
        DBKit.emptyMatchDictByLabel(Arrays.asList("attribute", "big_category", "small_category"));
        logger.info("清空 match_dict 表 attribute、big_category、small_category 字段完成.");

        // 存入 match_dict 表
        for (String key : map.keySet()) {
            DBKit.insertMatchDict(key, map.get(key)[1], map.get(key)[0]);
        }
        logger.info("将 concepts 表中的 big_category、small_category 和 attributes 的映射信息存入 match_dict 表完成！");
    }


    /**
     * 将 entity及别名 存入 match_dict 数据库表
     */
    public static void getEntitiesAndSameasToDB() {

        logger.info("正在将 entities 表及其别名的映射信息存入 match_dict 表...");

        Map<String, String> map = new HashMap<>();

        // 先获取实体库、再获取实体别名库
        List<Result> results = DBKit.getEntitiesSameas();
        for (Result result : results) {
            if (!map.containsKey(result.getEntity_name_1())) {
                map.put(result.getEntity_name_1(), result.getEntity_name_1().replace(" ", ""));
            }
            map.put(result.getEntity_name_1(), map.get(result.getEntity_name_1()) + "|" + result.getEntity_name_2().replace(" ", ""));
        }
        logger.info("获取 entities 和别名成功.");

        // 清空 attribute、big_category、small_category 标签项
        DBKit.emptyMatchDictByLabel(Arrays.asList("entity"));
        logger.info("清空 match_dict 表 entity 字段完成.");

        // 存入 match_dict 表
        for (String key : map.keySet()) {
            DBKit.insertMatchDict(key, map.get(key), "entity");
        }
        logger.info("将 entities 表及其别名的映射信息存入 match_dict 表完成！");
    }

    /*
    ===================================================================================================================
      三、将 match_dict 表中的数据获取到本地，加载到HanLP分词器的自定义词典中（未生效可以先删除custom/CustomDictionary.txt.bin）
    ===================================================================================================================
     */
    /**
     * 根据数据库的 match_dict 表，获取分词词典到本地，到 data/dict_for_segment 目录下
     */
    public static void getDBToSegmentDict() {

        logger.info("正在获取 match_dict 表到本地 data/dict_for_segment 目录下...");

        // 存（所属标签 - 词条）
        Map<String, Set<String>> map = new HashMap<>();
        // 获取match_dict表信息
        List<Result> matchers = DBKit.getSimpleMatchDict();
        for (Result matcher : matchers) {
            if (!map.containsKey(matcher.getLabel())) {
                map.put(matcher.getLabel(), new HashSet<>());
            }
            for (String alias : matcher.getAlias().split("\\|")) {
                map.get(matcher.getLabel()).add(alias);
            }
        }
        logger.info("获取 match_dict 成功.");

        // 存入本地txt
        for (String key : map.keySet()) {
            String filepath = rootpath + "data/dict_for_segment/" + key + ".txt";
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
                for (String s : map.get(key)) {
                    bw.write(s + "\n");
                }
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.info("获取 match_dict 表到本地 data/dict_for_segment 目录下成功！");
    }
}
