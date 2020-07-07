package Searcher;

import java.sql.ResultSet;
import java.util.*;

public class AnswerSearcher {

    private Map<String, List<String>> parser_dict;
    private Map<String, List<List<String>>> patterns = buildPatterns();

    /**
     * 有参构造函数
     * @param parser_dict
     */
    public AnswerSearcher(Map<String, List<String>> parser_dict) {
        this.parser_dict = parser_dict;
    }

    private Map<String, List<List<String>>> buildPatterns() {

        Map<String, List<List<String>>> patterns_map = new HashMap<>();

        // 模式 1
        patterns_map.put("model1", new ArrayList<>());
        patterns_map.get("model1").add(Arrays.asList("n_weapon", "n_attr"));
        patterns_map.get("model1").add(Arrays.asList("n_attr", "n_weapon"));


        return patterns_map;
    }

    /**
     * 判断问句模式，从数据库检索答案
     */
    public ResultSet getAnswer() {


        if (patterns.get("model1").contains(parser_dict.get("pattern")))
            System.out.println("模式匹配！");


        return null;
    }

}
