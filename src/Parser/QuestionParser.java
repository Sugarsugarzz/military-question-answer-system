package Parser;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;

import Model.EAHistory;

import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionParser {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static List<String> natures = new ArrayList<>();
    static List<String> keywords1 = new ArrayList<>();
    static List<String> keywords2 = new ArrayList<>();
    static Map<String, List<String>> parser_dict = new HashMap<>();
    static Map<String, Integer> chineseMap = new HashMap<>();

    static {
        // 初始化已定义的词性列表
        natures.add("n_country");
        natures.add("n_entity");
        natures.add("n_attr");
        natures.add("n_big");
        natures.add("n_small");
        natures.add("n_compare");
        natures.add("n_most");
        natures.add("n_time");
        natures.add("n_unit");
        natures.add("keywords");
        // 复数指代名词
        keywords1.add("它们");
        keywords1.add("他们");
        keywords1.add("她们");
        // 单数指代名词
        keywords2.add("它");
        keywords2.add("他");
        keywords2.add("她");
        keywords2.add("这");
        keywords2.add("这儿");
        keywords2.add("这个");
        keywords2.add("这里");
        //中文数字转阿拉伯数字
        chineseMap.put("一", 1);
        chineseMap.put("二", 2);
        chineseMap.put("三", 3);
        chineseMap.put("四", 4);
        chineseMap.put("五", 5);
        chineseMap.put("六", 6);
        chineseMap.put("七", 7);
        chineseMap.put("八", 8);
        chineseMap.put("九", 9);
        chineseMap.put("十", 10);
    }

    /**
     * 初始化词性字典(每轮都调用初始化，防止模式多次叠加)
     */
    private static void buildParserDict() {
        for (String nature: natures)
            parser_dict.put(nature, new ArrayList<>());
        // pattern存问句词性模式
        parser_dict.put("pattern", new ArrayList<>());
    }

    /**
     * 问句模式解析
     * @return 词性字典
     */
    public static Map<String, List<String>> parser(String question) {

        // 初始化词性字典，防止多次查询模式叠加
        buildParserDict();

        // 首先判断是否是 <热点> 和 <直达> 问题，否则，转 <百科> 和 <对比> 查询
        boolean isHotOrNonstop = checkQuestion(question);
        if (isHotOrNonstop)
            return parser_dict;

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
//            System.out.println(term.nature + " - " + term.word);
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
     * 判断是否是 <热点> 或 <直达> 问题，对于 <热点> 问题提取时间和关键词
     * @param question 问句
     * @return 标志符
     */
    private static boolean checkQuestion(String question) {

        if (question.contains("热点") || question.contains("REDIAN")) {
            parser_dict.get("pattern").add("3");
            // 提取 起始时间 和 结束时间
            question = question.replace("热点", "").replace("新闻", "").replace("想", "").replace("REDIAN", "");
            Map<String, TimeUnit> TimeResults = new TimeNormalizer().parse(question);
            for (String key : TimeResults.keySet()) {
                question = question.replace(key, "");
                parser_dict.get("n_time").add(DateUtil.formatDateDefault(TimeResults.get(key).getTime()));
            }
            // 提取 关键词
            parser_dict.get("keywords").addAll(HanLP.extractKeyword(question, 5));
            // 针对 n_time 长度为一的情况，将对应字段也加入，以便后面识别加入end_time
            if (parser_dict.get("n_time").size() == 1) {
                for (String key : TimeResults.keySet())
                    parser_dict.get("n_unit").add(key);
            }

            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：热点查询");
        }

        else if (question.contains("头条") || question.contains("TOUTIAO")) {
            parser_dict.get("pattern").add("41");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 头条");
        }

        else if (question.contains("百科") || question.contains("BAIKE")) {
            parser_dict.get("pattern").add("42");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 百科");
        }

        else if (question.contains("订阅") || question.contains("DINGYUE")) {
            parser_dict.get("pattern").add("43");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 订阅");
        }

        else if (question.contains("我的收藏") || question.contains("WODESHOUCANG") || question.contains("SHOUCANG")) {
            parser_dict.get("pattern").add("44");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 我的收藏");
        }

        else if (question.contains("浏览历史") || question.contains("LIULANLISHI")) {
            parser_dict.get("pattern").add("45");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 浏览历史");
        }

        else if (question.contains("无内容") || question.contains("WUNEIRONG")) {
            parser_dict.get("pattern").add("46");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 无内容");
        }

        else {
            return false;
        }

        return true;
    }
    
    /**
     * 多轮问答中，处理问句中出现指代词的情况，将其替换为对应实体和属性
     * @return 构造的新问句
     */
    public static String anaphoraResolution(EAHistory eah, String question) {

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
                	String str= String.join(" ", eah.getHistEntities());
                	newQuest = question.replace(term.word, str);
                }
                // 问句中只出现的实体（如：神舟七号和它们的呢？）
                else if (flagEntity) {
                	ArrayList<String> strs = new ArrayList<>();
                	strs.addAll(eah.getHistEntities());
                	strs.addAll(eah.getHistAttrs());
                	String str= String.join(" ", strs);
                	newQuest = question.replace(term.word, str);
                }
                // 问句中只出现的属性（如：它们的长度是多少？）
                else if (flagAttr) {
                	String str= String.join(" ", eah.getHistEntities());
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
        System.out.println(newQuest);
        return newQuest;
    }

    /**
     * 问句预处理
     * @return 将问句标准化，删除特殊符号等无用信息，将字母转化成大写，将文字转数字（如十转为10）
     */
    public static String preProcessQuestion(String question) {

        String standQuest = question.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");  //去除中文、数字、英文、之外的内容
        standQuest = standQuest.toUpperCase();//字母全部大写

        //中文数字转阿拉伯数字
        while (standQuest.contains("十")) { //判断问句中是否包含 “十”
            StringBuilder sBuilder = new StringBuilder(standQuest);
            int num = 10;
            int index_ = standQuest.indexOf("十");
            int start = index_;
            int end = index_+1;
            //判断“十”前面的字符是不是 “一”到“九”中的数字
            if (index_>0 && chineseMap.containsKey(String.valueOf(standQuest.charAt(index_-1)))) {
                num = chineseMap.get(String.valueOf(standQuest.charAt(index_-1)))*10;
                start -= 1;
            }
            //判断“十”后面的字符是不是 “一”到“九”中的数字
            if(index_<standQuest.length()-1 && chineseMap.containsKey(String.valueOf(standQuest.charAt(index_+1)))) {
                num += chineseMap.get(String.valueOf(standQuest.charAt(index_+1)));
                end += 1;
            }
            //将中文数字用阿拉伯数字替换
            standQuest = sBuilder.replace(start, end, Integer.toString(num)).toString();
        }
        //不含“十”的情况，直接用chineseMap 中的键用值替换
        for (Map.Entry<String, Integer> entry : chineseMap.entrySet()) {
            standQuest = standQuest.replaceAll(entry.getKey(), Integer.toString(entry.getValue()));
        }
        return standQuest;
    }

}
