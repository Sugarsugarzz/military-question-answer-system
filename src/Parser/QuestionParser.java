package Parser;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;

public class QuestionParser {

    private String question;
    private List<String> natures = buildNatures();
    private Map<String, List<String>> parser_dict = buildParserDict();

    /**
     * 有参构造函数
     * @param question
     */
    public QuestionParser(String question) {
        this.question = question;
    }

    /**
     * 初始化词性列表
     * @return
     */
    private List<String> buildNatures() {
        List<String> nature_list = new ArrayList<>();
        nature_list.add("n_country");
        nature_list.add("n_weapon");
        nature_list.add("n_attr");
        nature_list.add("n_big");
        nature_list.add("n_small");
        nature_list.add("n_compare");
        nature_list.add("n_most");
        return nature_list;
    }

    /**
     * 初始化词性字典
     * @return
     */
    private Map<String, List<String>> buildParserDict() {
        Map<String, List<String>> parser_map = new HashMap<>();
        for (String nature: natures) {
            parser_map.put(nature, new ArrayList<>());
        }
        // 问句词性模式
        parser_map.put("pattern", new ArrayList<>());
        return parser_map;
    }

    /**
     * 问句解析器
     * 返回：词性字典
     */
    public Map<String, List<String>> parser() {

        // 利用HanLP分词，遍历词和词性
        List<Term> terms = HanLP.segment(question);
        for (Term term : terms) {
            if (natures.contains(term.nature.toString())) {
                parser_dict.get(term.nature.toString()).add(term.word);
                parser_dict.get("pattern").add(term.nature.toString());
            }
        }

        // 测试打印dict
        for (String key: parser_dict.keySet()) {
            System.out.println(key + ":" + parser_dict.get(key));
        }

        return parser_dict;
    }

}
