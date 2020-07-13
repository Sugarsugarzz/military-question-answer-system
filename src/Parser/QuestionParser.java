package Parser;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class QuestionParser {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private List<String> natures = buildNatures();
    private Map<String, List<String>> parser_dict = buildParserDict();

    /**
     * 初始化已定义的词性列表
     * @return 初始已定义词性列表
     */
    private List<String> buildNatures() {
        List<String> nature_list = new ArrayList<>();
        nature_list.add("n_country");
        nature_list.add("n_entity");
        nature_list.add("n_attr");
        nature_list.add("n_big");
        nature_list.add("n_small");
        nature_list.add("n_compare");
        nature_list.add("n_most");
        logger.info("词性列表初始化完成！");
        return nature_list;
    }

    /**
     * 初始化词性字典
     * @return 初始词性字典
     */
    private Map<String, List<String>> buildParserDict() {
        Map<String, List<String>> parser_map = new HashMap<>();
        for (String nature: natures) {
            parser_map.put(nature, new ArrayList<>());
        }
        // pattern存问句词性模式
        parser_map.put("pattern", new ArrayList<>());
        logger.info("词性字典初始化完成！");
        return parser_map;
    }

    /**
     * 问句模式解析
     * @return 词性字典
     */
    public Map<String, List<String>> parser(String question) {

        // 利用HanLP分词，遍历词和词性
        List<Term> terms = HanLP.segment(question);
        for (Term term : terms) {
            if (natures.contains(term.nature.toString())) {
                parser_dict.get(term.nature.toString()).add(term.word);
                parser_dict.get("pattern").add(term.nature.toString());
            }
        }

        logger.info("问句解析完成。");
        logger.info("词性匹配情况：" + parser_dict);
        logger.info("问句模式：" + parser_dict.get("pattern"));

        return parser_dict;
    }

}
