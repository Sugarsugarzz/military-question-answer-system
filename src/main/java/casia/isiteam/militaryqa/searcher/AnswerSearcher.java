package casia.isiteam.militaryqa.searcher;

import casia.isiteam.militaryqa.model.Answer;
import casia.isiteam.militaryqa.utils.DBKit;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class AnswerSearcher {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static Map<String, List<List<String>>> patterns = new HashMap<>();  // 问句匹配模式

    static int Q_type = 1;  // 问题类型 - 默认 百科
    static int A_type = 2;  // 答案类型 - 默认 实体

    /**
     * 初始化问句匹配模式
     */
    static {

        // 热点查询模式
        patterns.put("热点查询模式", new ArrayList<>());
        patterns.get("热点查询模式").add(Arrays.asList("3"));

        // 期刊查询模式
        patterns.put("期刊查询模式", new ArrayList<>());
        patterns.get("期刊查询模式").add(Arrays.asList("4"));

        // 报告查询模式
        patterns.put("报告查询模式", new ArrayList<>());
        patterns.get("报告查询模式").add(Arrays.asList("5"));

        // 直达模式：头条
        patterns.put("直达模式-头条", new ArrayList<>());
        patterns.get("直达模式-头条").add(Arrays.asList("61"));

        // 直达模式：百科
        patterns.put("直达模式-百科", new ArrayList<>());
        patterns.get("直达模式-百科").add(Arrays.asList("62"));

        // 直达模式：订阅
        patterns.put("直达模式-订阅", new ArrayList<>());
        patterns.get("直达模式-订阅").add(Arrays.asList("63"));

        // 直达模式：我的收藏
        patterns.put("直达模式-我的收藏", new ArrayList<>());
        patterns.get("直达模式-我的收藏").add(Arrays.asList("64"));

        // 直达模式：浏览历史
        patterns.put("直达模式-浏览历史", new ArrayList<>());
        patterns.get("直达模式-浏览历史").add(Arrays.asList("65"));

        // 辅助查询模式 ：大类别名，返回二级类别列表
        patterns.put("大类别名", new ArrayList<>());
        patterns.get("大类别名").add(Arrays.asList("n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_big", "n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_big", "n_big", "n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_country", "n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_big", "n_country"));

        // 百科模式 ：小类别名，返回类别下所有实体列表
        patterns.put("小类别名", new ArrayList<>());
        patterns.get("小类别名").add(Arrays.asList("n_small"));
        patterns.get("小类别名").add(Arrays.asList("n_small", "n_small"));
        patterns.get("小类别名").add(Arrays.asList("n_small", "n_small", "n_small"));

        // 百科模式 ：国家及类别名
        patterns.put("国家及类别名", new ArrayList<>());
        patterns.get("国家及类别名").add(Arrays.asList("n_country", "n_small"));
        patterns.get("国家及类别名").add(Arrays.asList("n_small", "n_country"));

        // 百科模式 ：单实体
        patterns.put("单实体", new ArrayList<>());
        patterns.get("单实体").add(Arrays.asList("n_entity"));
        patterns.get("单实体").add(Arrays.asList("n_entity", "n_small"));
        patterns.get("单实体").add(Arrays.asList("n_country", "n_entity"));
        patterns.get("单实体").add(Arrays.asList("n_entity", "n_country"));

        // 百科模式 ：多实体
        patterns.put("多实体", new ArrayList<>());
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity", "n_entity", "n_entity"));

        // 百科模式 ：单实体单属性/多属性
        patterns.put("单实体单属性/多属性", new ArrayList<>());
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        // 百科模式 ：多实体单属性/多属性
        patterns.put("多实体单属性/多属性", new ArrayList<>());
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        // 百科模式 ：单属性单类别单区间
        patterns.put("单属性单类别单区间", new ArrayList<>());
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_small"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_small"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare"));

        // 百科模式 ：单属性单类别多区间
        patterns.put("单属性单类别多区间", new ArrayList<>());
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_compare", "n_unit", "n_small"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_compare", "n_unit"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_compare", "n_time", "n_small"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_time", "n_compare", "n_small"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time", "n_compare", "n_time"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare", "n_time", "n_compare"));

        // 百科模式 ：多属性单类别单区间
        patterns.put("多属性单类别单区间", new ArrayList<>());
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_unit"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_time", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_attr", "n_time", "n_compare", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time", "n_attr", "n_compare", "n_unit"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare", "n_attr", "n_compare", "n_unit"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_time"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_attr", "n_time", "n_compare"));

        // 百科模式 ：全类别属性最值
        patterns.put("全类别属性最值", new ArrayList<>());
        patterns.get("全类别属性最值").add(Arrays.asList("n_attr", "n_most"));
        patterns.get("全类别属性最值").add(Arrays.asList("n_most", "n_attr"));

        // 百科模式 ：单类别属性最值
        patterns.put("单类别属性最值", new ArrayList<>());
        patterns.get("单类别属性最值").add(Arrays.asList("n_small", "n_attr", "n_most"));
        patterns.get("单类别属性最值").add(Arrays.asList("n_small", "n_most", "n_attr"));
        patterns.get("单类别属性最值").add(Arrays.asList("n_attr", "n_most", "n_small"));
        patterns.get("单类别属性最值").add(Arrays.asList("n_attr", "n_small", "n_most"));

//        logger.info("问句模式匹配字典初始化完成！");
    }

    /**
     * 判断问句模式，从数据库检索答案
     */
    public static String getAnswer(Map<String, List<String>> parser_dict) {

        // 初始化
        Q_type = 1;
        A_type = 2;

        // 存储从数据库获取的答案
        List<Answer> answers = new ArrayList<>();

        // 模式匹配
        if (patterns.get("热点查询模式").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "热点查询模式"));
            Q_type = 3;
        }

        else if (patterns.get("期刊查询模式").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "期刊查询模式"));
            Q_type = 4;
        }

        else if (patterns.get("报告查询模式").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "报告查询模式"));
            Q_type = 5;
        }

        else if (patterns.get("直达模式-头条").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "直达模式-头条"));
            Q_type = 6;
            A_type = 1;
        }

        else if (patterns.get("直达模式-百科").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "直达模式-百科"));
            Q_type = 6;
            A_type = 2;
        }

        else if (patterns.get("直达模式-订阅").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "直达模式-订阅"));
            Q_type = 6;
            A_type = 3;
        }

        else if (patterns.get("直达模式-我的收藏").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "直达模式-我的收藏"));
            Q_type = 6;
            A_type = 4;
        }

        else if (patterns.get("直达模式-浏览历史").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "直达模式-浏览历史"));
            Q_type = 6;
            A_type = 5;
        }

        else if (patterns.get("大类别名").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "大类别名"));
            Q_type = 7;
            for (String category : parser_dict.get("n_big")) {
                // 数据库检索答案
                answers.addAll(DBKit.searchByBigCategory(DictMapper.BigCategory.get(category)));
            }
        }

        else if (patterns.get("小类别名").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "小类别名"));
            for (String category : parser_dict.get("n_small")) {
                // 数据库检索答案
                answers.addAll(DBKit.searchBySmallCategory(DictMapper.SmallCategory.get(category)));
            }
        }

        else if (patterns.get("国家及类别名").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "国家及类别名"));
            String country = DictMapper.Country.get(parser_dict.get("n_country").get(0));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            // 数据库检索答案
            answers = DBKit.searchByCountryAndCategory(country, category);
        }

        else if (patterns.get("单实体").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单实体"));
            Set<String> entities = DictMapper.Entity.get(parser_dict.get("n_entity").get(0));
            for (String entity : entities) {
                // 数据库检索答案
                answers.addAll(DBKit.searchByEntity(entity));
            }
        }

        else if (patterns.get("多实体").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "多实体"));
            Q_type = 2;
            for (String entity: parser_dict.get("n_entity")) {
                for (String enty : DictMapper.Entity.get(entity)) {
                    // 数据库检索答案
                    answers.addAll(DBKit.searchByEntity(enty));
                }
            }
        }

        else if (patterns.get("单实体单属性/多属性").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单实体单属性/多属性"));
            A_type = 1;
            Set<String> entities = DictMapper.Entity.get(parser_dict.get("n_entity").get(0));
            for (String entity : entities) {
                List<String> attrs = new ArrayList<>();
                for (String attr: parser_dict.get("n_attr")) {
                    attrs.add(DictMapper.Attribute.get(attr));
                }
                // 数据库检索答案
                answers.addAll(DBKit.searchByEntityAndAttrs(entity, attrs));
            }
        }
        /* 4 */
        else if (patterns.get("多实体单属性/多属性").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "多实体单属性/多属性"));
            A_type = 1;
            for (String entity: parser_dict.get("n_entity")) {
                List<String> attrs = new ArrayList<>();
                for (String attr: parser_dict.get("n_attr")) {
                    attrs.add(DictMapper.Attribute.get(attr));
                }
                for (String enty : DictMapper.Entity.get(entity)) {
                    // 数据库检索答案
                    answers.addAll(DBKit.searchByEntityAndAttrs(enty, attrs));
                }
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
                answers = DBKit.searchInSingleRangeByUnit(category, attr, operator, unit_items.get(0));
            } else {
                answers = DBKit.searchInSingleRangeByTime(category, attr, operator, time_items.get(0));
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
                answers = DBKit.searchInMultiRangeByUnit(category, attr, operator_1, unit_items.get(0),
                        operator_2, unit_items.get(1));
            } else {
                answers = DBKit.searchInMultiRangeByTime(category, attr, operator_1, time_items.get(0),
                        operator_2, time_items.get(1));
            }
        }

        else if (patterns.get("多属性单类别单区间").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "多属性单类别单区间"));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            String operator_1 = DictMapper.Compare.get(parser_dict.get("n_compare").get(0));
            String operator_2 = DictMapper.Compare.get(parser_dict.get("n_compare").get(1));
            String attr_1 = DictMapper.Attribute.get(parser_dict.get("n_attr").get(0));
            String attr_2 = DictMapper.Attribute.get(parser_dict.get("n_attr").get(1));
            List<String> time_items = DictMapper.processTime(parser_dict.get("n_time"));
            List<String> unit_items = DictMapper.processUnit(parser_dict.get("n_unit"));

            // 数据库检索答案
            if (time_items.isEmpty()) {
                answers = DBKit.searchMultiAttrInSingleRangeByUnit(category, attr_1, operator_1, unit_items.get(0),
                        attr_2, operator_2, unit_items.get(1));
            } else {
                // 区分一下时间属性和数值属性的顺序
                if (parser_dict.get("pattern").indexOf("n_unit") > parser_dict.get("pattern").indexOf("n_time")) {
                    answers = DBKit.searchMultiAttrInSingleRangeByTimeAndUnit(category, attr_2, operator_2, unit_items.get(0),
                            attr_1, operator_1, time_items.get(0));
                } else {
                    answers = DBKit.searchMultiAttrInSingleRangeByTimeAndUnit(category, attr_1, operator_1, unit_items.get(0),
                            attr_2, operator_2, time_items.get(0));
                }
            }
        }

        else if (patterns.get("全类别属性最值").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "全类别属性最值"));
            String type = DictMapper.Most.get(parser_dict.get("n_most").get(0));
            String attr = DictMapper.Attribute.get(parser_dict.get("n_attr").get(0));

            // 数据库检索答案
            if (type.equals("max"))
                answers.addAll(DBKit.searchMaxInAllCategory(attr));
            else if (type.equals("min"))
                answers.addAll(DBKit.searchMinInAllCategory(attr));
        }

        else if (patterns.get("单类别属性最值").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单类别属性最值"));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            String type = DictMapper.Most.get(parser_dict.get("n_most").get(0));
            String attr = DictMapper.Attribute.get(parser_dict.get("n_attr").get(0));

            // 数据库检索答案
            if (type.equals("max"))
                answers.addAll(DBKit.searchMaxInSingleCategory(category, attr));
            else if (type.equals("min"))
                answers.addAll(DBKit.searchMinInSingleCategory(category, attr));
        }

        else {
            logger.info("未找到相应问句模板！");
        }

        // 组装答案JSON
        JSONObject jsonObject = assembleJSON(parser_dict, answers);

        return jsonObject.toJSONString();
    }

    /**
     * 组装JSON
     * @param answers 答案实体列表
     * @return JSON
     */
    public static JSONObject assembleJSON(Map<String, List<String>> parser_dict, List<Answer> answers) {

        JSONObject obj = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        switch (Q_type) {

            // 百科模式
            case 1:
                obj.put("Q_type", Q_type);
                obj.put("A_type", A_type);
                for (Answer answer: answers) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("entity_id", answer.getEntity_id());
                    jsonObject.put("entity_name", answer.getEntity_name());
                    jsonObject.put("attr_name", answer.getAttr_name());
                    jsonObject.put("attr_value", answer.getAttr_value());
                    jsonArray.add(jsonObject);
                }
                obj.put("Answer", jsonArray);
                break;

            // 对比模式
            case 2:
                obj.put("Q_type", Q_type);
                for (Answer answer: answers) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("entity_id", answer.getEntity_id());
                    jsonObject.put("entity_name", answer.getEntity_name());
                    jsonArray.add(jsonObject);
                }
                obj.put("Answer", jsonArray);
                break;

            // 热点查询模式
            case 3:

            // 期刊查询模式
            case 4:

            // 报告查询模式
            case 5:
                obj.put("Q_type", Q_type);
                obj.put("Q_content", parser_dict.get("keywords"));

                if (parser_dict.get("n_time").size() == 0) {
                    obj.put("Q_start_time", null);
                    obj.put("Q_end_time", null);
                } else if (parser_dict.get("n_time").size() == 1) {
                    String start_time = parser_dict.get("n_time").get(0);
                    String end_time = null;
                    String word = parser_dict.get("n_unit").get(0);
                    // 长度为1的在处理下，补充一个结束时间
                    try {
                        if (word.contains("年"))
                            end_time = addTime(start_time, 4);
                        else if (word.contains("月"))
                            end_time = addTime(start_time, 3);
                        else if (word.contains("周"))
                            end_time = addTime(start_time, 2);
                        else if (word.contains("日") || word.contains("天") || word.contains("号"))
                            end_time = addTime(start_time, 1);
                    } catch (ParseException e) {
                        logger.error("查询模式补充结束时间出现错误：" + e);
                    }
                    obj.put("Q_start_time", start_time);
                    obj.put("Q_end_time", end_time);
                } else {
                    Collections.sort(parser_dict.get("n_time"));
                    obj.put("Q_start_time", parser_dict.get("n_time").get(0));
                    obj.put("Q_end_time", parser_dict.get("n_time").get(parser_dict.get("n_time").size() - 1));
                }
                break;

            // 直达模式
            case 6:
                obj.put("Q_type", Q_type);
                obj.put("Answer", A_type);
                break;

            // 辅助模式
            case 7:
                obj.put("Q_type", Q_type);
                List<String> categories = new ArrayList<>();
                for (Answer answer : answers)
                    categories.add(answer.getEntity_name());
                obj.put("Answer", categories);
                break;
        }

        return obj;
    }

    /**
     * 根据语义补充一个结束日期
     * @param date 日期字符串
     * @param flag 天：1  周：2  月：3  年：4
     * @return 增加后的日期字符串
     */
    private static String addTime(String date, int flag) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);

        switch (flag) {
            // 天
            case 1:  calendar.add(Calendar.DAY_OF_MONTH, 1); break;
            // 周
            case 2:  calendar.add(Calendar.DAY_OF_MONTH, 7); break;
            // 月
            case 3:  calendar.add(Calendar.MONTH, 1); break;
            // 年
            case 4:  calendar.add(Calendar.YEAR, 1); break;
        }

        Date dt1 = calendar.getTime();
        return sdf.format(dt1);
    }
}
