package Searcher;

import Model.Answer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class AnswerSearcher {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private Map<String, List<String>> parser_dict;
    private Map<String, List<List<String>>> patterns = buildPatterns();

    /**
     * 有参构造函数
     * @param parser_dict 词性字典
     */
    public AnswerSearcher(Map<String, List<String>> parser_dict) {
        this.parser_dict = parser_dict;
    }

    /**
     * 初始化问句匹配模式
     * @return 模式字典
     */
    private Map<String, List<List<String>>> buildPatterns() {

        Map<String, List<List<String>>> patterns_map = new HashMap<>();

        // 模式 1：国家实体类别
        patterns_map.put("国家及实体类别", new ArrayList<>());
        patterns_map.get("国家及实体类别").add(Arrays.asList("n_country", "n_small"));


        // 模式 1：单实体多属性
        patterns_map.put("单实体单属性/多属性", new ArrayList<>());
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        // 模式 2：


        logger.info("问句模式匹配字典初始化完成！");
        return patterns_map;
    }

    /**
     * 判断问句模式，从数据库检索答案
     */
    public List<Answer> getAnswer() {

        // 存储结果
        List<Answer> answers = new ArrayList<>();

        // 模式匹配
        if (patterns.get("国家及实体类别").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "国家及实体类别"));
            String country = DictMapper.Country.get(parser_dict.get("n_country").get(0));
            String category = DictMapper.SmallCategory.get(parser_dict.get("n_small").get(0));
            // 数据库检索答案
            answers = DbSearcher.searchByCountryAndCategory(country, category);

        } else if (patterns.get("单实体单属性/多属性").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "单实体单属性/多属性"));
            String entity = DictMapper.Entity.get(parser_dict.get("n_entity").get(0));
            List<String> attrs = new ArrayList<>();
            for (String attr: parser_dict.get("n_attr")) {
                attrs.add(DictMapper.Attribute.get(attr));
            }
            // 数据库检索答案
            answers = DbSearcher.searchByEntityAndAttrs(entity, attrs);

        } else {
            logger.info("未找到相应问句模板！");
        }




        return answers;
    }

}
