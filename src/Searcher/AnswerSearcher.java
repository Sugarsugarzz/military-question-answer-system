package Searcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
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
        patterns_map.put("model1", new ArrayList<>());
        patterns_map.get("model1").add(Arrays.asList("n_weapon", "n_attr"));
        patterns_map.get("model1").add(Arrays.asList("n_attr", "n_weapon"));

        logger.info("问句模式匹配字典初始化完成！");
        return patterns_map;
    }

    /**
     * 判断问句模式，从数据库检索答案
     */
    public ResultSet getAnswer() {

        if (patterns.get("model1").contains(parser_dict.get("pattern"))) {
            logger.info(String.format("与 %s 模式匹配成功！", "model1"));
            // 思路：从entities表获取weapon的id，从concepts表获取attr的id，在entity_attr表中查询结果
            System.out.println(parser_dict.get("n_weapon").get(0));
        }




        return null;
    }

}
