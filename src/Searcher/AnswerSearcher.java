package Searcher;

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

        // 模式 1
        patterns_map.put("Pattern_1", new ArrayList<>());
        patterns_map.get("Pattern_1").add(Arrays.asList("n_entity", "n_attr"));
        patterns_map.get("Pattern_1").add(Arrays.asList("n_entity", "n_attr", "n_attr"));
        patterns_map.get("Pattern_1").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("Pattern_1").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns_map.get("Pattern_1").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        logger.info("问句模式匹配字典初始化完成！");
        return patterns_map;
    }

    /**
     * 判断问句模式，从数据库检索答案
     */
    public List<Map<String, String>> getAnswer() {

        // 数据库答案检索工具类
        DbSearcher dbSearcher = new DbSearcher();

        // 存储结果
        List<Map<String, String>> res = new ArrayList<>();

        // 单实体多属性
        if (patterns.get("Pattern_1").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 问句模式匹配成功！", "Pattern_1"));
            String entity = Mapper.Entity.get(parser_dict.get("n_entity").get(0));
            List<String> attrs = new ArrayList<>();
            for (String attr: parser_dict.get("n_attr")) {
                attrs.add(Mapper.Attribute.get(attr));
            }
            // 数据库检索答案
            res = dbSearcher.search(entity, attrs);

        } else {
            logger.info("未找到相应问句模板！");
        }




        return res;
    }

}
