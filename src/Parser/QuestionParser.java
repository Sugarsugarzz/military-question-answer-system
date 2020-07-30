package Parser;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;

import Model.EAHistory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionParser {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private List<String> natures = buildNatures();
    private List<String> keywords1 = buildKey1();
    private List<String> keywords2 = buildKey2();
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
        nature_list.add("n_time");
        nature_list.add("n_unit");
//        logger.info("词性列表初始化完成！");
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
//        logger.info("词性字典初始化完成！");
        return parser_map;
    }

    /**
     * 问句模式解析
     * @return 词性字典
     */
    public Map<String, List<String>> parser(String question) {

        // 初始化词性字典
        parser_dict = buildParserDict();

        // 识别出问句中的<时间>，加入分词器词典
        Matcher m_time = Pattern.compile("[0-9]{4}年([0-9]{0,2}月)?([0-9]{0,2}日)?").matcher(question);
        while (m_time.find())
            CustomDictionary.add(m_time.group(), "n_time");

        // 识别出问句中的<单位数值>，加入分词器词典
        String[] units = { "海里", "英里", "吨", "公里", "公里/节", "公里/小时", "毫米", "节", "克", "里", "米", "千克", "千米",
                "千米/时", "千米/小时", "千米每小时", "余英里", "约海里", "最大海里", "厘米", "分米", "人", "位"};
        String unit_regex = String.format("([0-9]+(.[0-9]+)?)(%s)+", String.join("|", units));
        Matcher m_unit = Pattern.compile(unit_regex).matcher(question);
        while (m_unit.find())
            CustomDictionary.add(m_unit.group(), "n_unit");

        // 利用HanLP分词，遍历词和词性
        List<Term> terms = HanLP.segment(question);
        for (Term term : terms) {
            if (natures.contains(term.nature.toString())) {
                parser_dict.get(term.nature.toString()).add(term.word);
                parser_dict.get("pattern").add(term.nature.toString());
            }
        }

//        logger.info("问句解析完成。");
        logger.info("词性匹配情况：" + parser_dict);
        logger.info("问句模式：" + parser_dict.get("pattern"));

        return parser_dict;
    }
    
    
    /**
     * 多轮问答中，处理问句中出现指代词的情况，将其替换为对应实体和属性
     * @return 构造的新问句
     */
    public String preProcessQuestion(EAHistory eah, String question) {

        // 利用HanLP分词，遍历词和词性
        List<Term> terms = HanLP.segment(question);
        String newQuest = question;
        
        Boolean flagAttr = false;
        Boolean flagEntity = false;
        for (Term term : terms) {
            if (term.nature.toString().equals("n_entity")) {
                flagEntity = true;
            }
            if (term.nature.toString().equals("n_attr")) {
            	flagAttr = true;
            }
        }
        
        for (Term term : terms) {
            // 指示代词（复数）
            if (keywords1.contains(term.word)) {
                // 问句同时出现实体和属性的情况（如：神舟七号和它们的长度是多少？）
                if(flagEntity && flagAttr) {
                	String str= String.join(" ", eah.getHistEntity());
                	newQuest = question.replace(term.word, str);
                }
                // 问句中只出现的实体（如：神舟七号和它们的呢？）
                else if (flagEntity) {
                	ArrayList<String> strs = new ArrayList<>();
                	strs.addAll(eah.getHistEntity());
                	strs.addAll(eah.getHistAttr());
                	String str= String.join(" ", strs);
                	newQuest = question.replace(term.word, str);
                }
                // 问句中只出现的属性（如：它们的长度是多少？）
                else if (flagAttr) {
                	String str= String.join(" ", eah.getHistEntity());
                	newQuest = question.replace(term.word, str);
                }
                break;
            }
            // 指示代词（单数）
            else if (keywords2.contains(term.word)) {
                // 问句同时出现实体和属性的情况（如：神舟七号和它的长度是多少？）
            	if(flagEntity && flagAttr) {
                	newQuest = question.replace(term.word, eah.getLastEntity());
                }
                // 问句中只出现的实体（如：神舟七号和它的呢？）
            	else if (flagEntity) {
                	newQuest = question.replace(term.word, eah.getLastEntity() + " " + eah.getLastAttr());
                }
                // 问句中只出现的属性（如：它的长度是多少？）
            	else if (flagAttr) {
                	newQuest = question.replace(term.word, eah.getLastEntity());
                }
            	break;
            }
        }

        return newQuest;
    }

    private List<String> buildKey1() {
        List<String> nature_list = new ArrayList<>();
        nature_list.add("它们");
        nature_list.add("他们");
        nature_list.add("她们");
        return nature_list;
    }
    
    private List<String> buildKey2() {
        List<String> nature_list = new ArrayList<>();
        nature_list.add("它");
        nature_list.add("他");
        nature_list.add("她");
        nature_list.add("这");
        nature_list.add("这儿");
        nature_list.add("这个");
        nature_list.add("这里");
        return nature_list;
    }

}
