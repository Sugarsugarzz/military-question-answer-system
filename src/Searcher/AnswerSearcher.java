package Searcher;

import Model.Answer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class AnswerSearcher {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private Map<String, List<List<String>>> patterns = buildPatterns();

    /**
     * 初始化问句匹配模式
     * @return 模式字典
     */
    private Map<String, List<List<String>>> buildPatterns() {

        Map<String, List<List<String>>> patterns_map = new HashMap<>();

        // 模式 ：国家及类别名
        patterns_map.put("国家及类别名", new ArrayList<>());
        patterns_map.get("国家及类别名").add(Arrays.asList("n_country", "n_small"));
        patterns_map.get("国家及类别名").add(Arrays.asList("n_small", "n_country"));

        // 模式 ：单实体
        patterns_map.put("单实体", new ArrayList<>());
        patterns_map.get("单实体").add(Arrays.asList("n_entity"));
        patterns_map.get("单实体").add(Arrays.asList("n_country", "n_entity"));
        patterns_map.get("单实体").add(Arrays.asList("n_entity", "n_country"));

        // 模式 ：多实体
        patterns_map.put("多实体", new ArrayList<>());
        patterns_map.get("多实体").add(Arrays.asList("n_entity", "n_entity"));
        patterns_map.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity"));
        patterns_map.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity"));
        patterns_map.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity"));
        patterns_map.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity"));
        patterns_map.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity"));
        patterns_map.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity", "n_entity"));
        patterns_map.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity", "n_entity", "n_entity"));

        // 模式 ：单实体单属性/多属性
        patterns_map.put("单实体单属性/多属性", new ArrayList<>());
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        // 模式 ：多实体单属性/多属性
        patterns_map.put("多实体单属性/多属性", new ArrayList<>());
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        // 模式 ：单属性单类别单区间
        patterns_map.put("单属性单类别单区间", new ArrayList<>());
        patterns_map.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_small"));
        patterns_map.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit"));
        patterns_map.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_small"));
        patterns_map.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_small"));
        patterns_map.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time"));
        patterns_map.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare"));

        // 模式 ：单属性单类别多区间
        patterns_map.put("单属性单类别多区间", new ArrayList<>());
        patterns_map.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_compare", "n_unit", "n_small"));
        patterns_map.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_compare", "n_unit"));
        patterns_map.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_compare", "n_time", "n_small"));
        patterns_map.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_time", "n_compare", "n_small"));
        patterns_map.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time", "n_compare", "n_time"));
        patterns_map.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare", "n_time", "n_compare"));

        // 模式 ：多属性单类别多区间
        patterns_map.put("多属性单类别多区间", new ArrayList<>());
        patterns_map.get("多属性单类别多区间").add(Arrays.asList(""));

        // 模式 ：全类别属性最值
        patterns_map.put("全类别属性最值", new ArrayList<>());
        patterns_map.get("全类别属性最值").add(Arrays.asList("n_attr", "n_most"));
        patterns_map.get("全类别属性最值").add(Arrays.asList("n_most", "n_attr"));

        // 模式 ：单类别属性最值
        patterns_map.put("单类别属性最值", new ArrayList<>());
        patterns_map.get("单类别属性最值").add(Arrays.asList("n_small", "n_attr", "n_most"));
        patterns_map.get("单类别属性最值").add(Arrays.asList("n_small", "n_most", "n_attr"));
        patterns_map.get("单类别属性最值").add(Arrays.asList("n_attr", "n_most", "n_small"));
        patterns_map.get("单类别属性最值").add(Arrays.asList("n_attr", "n_small", "n_most"));


//        logger.info("问句模式匹配字典初始化完成！");
        return patterns_map;
    }

    /**
     * 判断问句模式，从数据库检索答案
     */
    public List<Answer> getAnswer(Map<String, List<String>> parser_dict) {

        // 存储答案
        List<Answer> answers = new ArrayList<>();

        // 模式匹配
        if (patterns.get("国家及类别名").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "国家及类别名"));
            String country = DictMapper.Country.get(parser_dict.get("n_country").get(0));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            // 数据库检索答案
            answers = DbSearcher.searchByCountryAndCategory(country, category);
        }

        else if (patterns.get("单实体").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单实体"));
            String entity = DictMapper.Entity.get(parser_dict.get("n_entity").get(0));
            // 数据库检索答案
            answers = DbSearcher.searchByEntity(entity);
        }

        else if (patterns.get("多实体").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "多实体"));
            for (String entity: parser_dict.get("n_entity")) {
                // 数据库检索答案
                answers.addAll(DbSearcher.searchByEntity(DictMapper.Entity.get(entity)));
            }
        }

        else if (patterns.get("单实体单属性/多属性").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单实体单属性/多属性"));
            String entity = DictMapper.Entity.get(parser_dict.get("n_entity").get(0));
            List<String> attrs = new ArrayList<>();
            for (String attr: parser_dict.get("n_attr")) {
                attrs.add(DictMapper.Attribute.get(attr));
            }
            // 数据库检索答案
            answers = DbSearcher.searchByEntityAndAttrs(entity, attrs);
        }

        else if (patterns.get("多实体单属性/多属性").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "多实体单属性/多属性"));
            for (String entity: parser_dict.get("n_entity")) {
                List<String> attrs = new ArrayList<>();
                for (String attr: parser_dict.get("n_attr")) {
                    attrs.add(DictMapper.Attribute.get(attr));
                }
                // 数据库检索答案
                answers.addAll(DbSearcher.searchByEntityAndAttrs(DictMapper.Entity.get(entity), attrs));
            }
        }

        else if (patterns.get("单属性单类别单区间").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单属性单类别单区间"));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            String operator = DictMapper.Compare.get(parser_dict.get("n_compare").get(0));
            String attr = DictMapper.Attribute.get(parser_dict.get("n_attr").get(0));
            List<String> time_items = DictMapper.processTime(parser_dict.get("n_time"));
            List<String> unit_items = DictMapper.processUnit(parser_dict.get("n_unit"));

            // 数据库检索答案
            if (time_items.isEmpty()) {
                answers = DbSearcher.searchInSingleRangeByUnit(category, attr, operator, unit_items.get(0));
            } else {
                answers = DbSearcher.searchInSingleRangeByTime(category, attr, operator, time_items.get(0));
            }
        }

        else if (patterns.get("单属性单类别多区间").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单属性单类别多区间"));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            String operator_1 = DictMapper.Compare.get(parser_dict.get("n_compare").get(0));
            String operator_2 = DictMapper.Compare.get(parser_dict.get("n_compare").get(1));
            String attr = DictMapper.Attribute.get(parser_dict.get("n_attr").get(0));
            List<String> time_items = DictMapper.processTime(parser_dict.get("n_time"));
            List<String> unit_items = DictMapper.processUnit(parser_dict.get("n_unit"));

            // 数据库检索答案
            if (time_items.isEmpty()) {
                answers = DbSearcher.searchInMultiRangeByUnit(category, attr, operator_1, unit_items.get(0),
                        operator_2, unit_items.get(1));
            } else {
                answers = DbSearcher.searchInMultiRangeByTime(category, attr, operator_1, time_items.get(0),
                        operator_2, time_items.get(1));
            }
        }

        else if (patterns.get("多属性单类别多区间").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "多属性单类别多区间"));
        }

        else if (patterns.get("全类别属性最值").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "全类别属性最值"));
            String type = DictMapper.Most.get(parser_dict.get("n_most").get(0));
            String attr = DictMapper.Attribute.get(parser_dict.get("n_attr").get(0));

            // 数据库检索答案
            if (type.equals("max"))
                answers.addAll(DbSearcher.searchMaxInAllCategory(attr));
            else if (type.equals("min"))
                answers.addAll(DbSearcher.searchMinInAllCategory(attr));
        }

        else if (patterns.get("单类别属性最值").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单类别属性最值"));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            String type = DictMapper.Most.get(parser_dict.get("n_most").get(0));
            String attr = DictMapper.Attribute.get(parser_dict.get("n_attr").get(0));

            // 数据库检索答案
            if (type.equals("max"))
                answers.addAll(DbSearcher.searchMaxInSingleCategory(category, attr));
            else if (type.equals("min"))
                answers.addAll(DbSearcher.searchMinInSingleCategory(category, attr));
        }

        else {
            logger.info("未找到相应问句模板！");
        }

        return answers;
    }

}
