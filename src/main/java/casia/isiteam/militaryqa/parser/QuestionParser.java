package casia.isiteam.militaryqa.parser;

import casia.isiteam.militaryqa.model.EaHistory;
import casia.isiteam.militaryqa.utils.ChineseNumberUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionParser {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static List<String> natures = new ArrayList<>();
    static List<String> keywords1 = new ArrayList<>();
    static List<String> keywords2 = new ArrayList<>();
    static Map<String, List<String>> parser_dict = new HashMap<>();
    // [0] 为前状态，[1] 为当前状态
    public static Map<String, boolean[]> isUsingPronounMap = new HashMap<>();

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
        keywords1.add("2者");
        keywords1.add("这些");
        // 单数指代名词
        keywords2.add("它");
        keywords2.add("他");
        keywords2.add("她");
        keywords2.add("这");
        keywords2.add("这儿");
        keywords2.add("这个");
        keywords2.add("这里");
    }

    /**
     * 初始化词性字典(每轮都调用初始化，防止模式多次叠加)
     */
    private static void buildParserDict() {
        for (String nature: natures) {
            parser_dict.put(nature, new ArrayList<>());
        }
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

        // 首先判断是否是 <热点> <期刊> <报告> 和 <直达> 问题，否则，转 <百科> 和 <对比> 查询
        boolean flag = checkQuestion(question);
        if (flag) {
            return parser_dict;
        }

        // 识别出问句中的<时间>，加入分词器词典
        Matcher m_time = Pattern.compile("[0-9]{4}年([0-9]{0,2}月)?([0-9]{0,2}日)?").matcher(question);
        while (m_time.find()) {
            CustomDictionary.add(m_time.group(), "n_time");
        }

        // 识别出问句中的<单位数值>，加入分词器词典
        String[] units = { "海里", "英里", "吨", "公里", "公里/节", "公里/小时", "毫米", "节", "克", "里", "米", "千克", "千米",
                "千米/时", "千米/小时", "千米每小时", "余英里", "约海里", "最大海里", "厘米", "分米", "人", "位"};
        String unit_regex = String.format("([0-9]+(.[0-9]+)?)(%s)+", String.join("|", units));
        Matcher m_unit = Pattern.compile(unit_regex).matcher(question);
        while (m_unit.find()) {
            CustomDictionary.add(m_unit.group(), "n_unit");
        }

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
     * 判断是否是 <热点>、<期刊>、<报告>、<直达> 问题，对于 <热点>、<期刊>、<报告> 问题提取时间和关键词
     * @param question 问句
     * @return 标志符
     */
    private static boolean checkQuestion(String question) {

        if (question.contains("热点") || question.contains("REDIAN")) {
            parser_dict.get("pattern").add("3");
            question = question.replace("热点", "").replace("REDIAN", "").replace("新闻", "").replace("想", "").replace("现在", "");
            extractTimeAndKeywords(question);
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：热点查询模式");
        }

        else if (question.contains("期刊") || question.contains("QIKAN")) {
            parser_dict.get("pattern").add("4");
            question = question.replace("期刊", "").replace("QIKAN", "").replace("新闻", "").replace("想", "").replace("现在", "");
            extractTimeAndKeywords(question);
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：期刊查询模式");
        }

        else if (question.contains("报告") || question.contains("BAOGAO")) {
            parser_dict.get("pattern").add("5");
            question = question.replace("报告", "").replace("BAOGAO", "").replace("新闻", "").replace("想", "").replace("现在", "");
            extractTimeAndKeywords(question);
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：报告查询模式");
        }

        else if (question.contains("头条") || question.contains("TOUTIAO")) {
            parser_dict.get("pattern").add("61");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 头条");
        }

        else if (question.contains("百科") || question.contains("BAIKE")) {
            parser_dict.get("pattern").add("62");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 百科");
        }

        else if (question.contains("订阅") || question.contains("DINGYUE")) {
            parser_dict.get("pattern").add("63");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 订阅");
        }

        else if (question.contains("我的收藏") || question.contains("WODESHOUCANG") || question.contains("收藏") || question.contains("SHOUCANG")) {
            parser_dict.get("pattern").add("64");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 我的收藏");
        }

        else if (question.contains("浏览历史") || question.contains("LIULANLISHI") || question.contains("浏览") || question.contains("LIULAN")) {
            parser_dict.get("pattern").add("65");
            logger.info("词性匹配情况：" + parser_dict);
            logger.info("问句模式：直达 - 浏览历史");
        }

        else {
            return false;
        }

        return true;
    }

    /**
     * 提取问句中的起始、结束时间，以及关键词
     * @param question 问句
     */
    private static void extractTimeAndKeywords(String question) {
        // 提取 起始时间 和 结束时间
        Map<String, TimeUnit> timeResults = new TimeNormalizer().parse(question);
        for (String key : timeResults.keySet()) {
            if (question.contains("神舟") && key.contains("号")) {
                continue;
            }
            question = question.replace(key, "");
            parser_dict.get("n_time").add(DateUtil.formatDateDefault(timeResults.get(key).getTime()));
        }
        // 提取 关键词
        parser_dict.get("keywords").addAll(HanLP.extractKeyword(question, 5));
        // 针对 n_time 长度为一的情况，将对应字段也加入，以便后面识别加入end_time
        if (parser_dict.get("n_time").size() == 1) {
            for (String key : timeResults.keySet()) {
                if (question.contains("神舟") && key.contains("号")) {
                    continue;
                }
                parser_dict.get("n_unit").add(key);
            }
        }
    }
    
    /**
     * 多轮问答中，处理问句中出现指代词的情况，将其替换为对应实体和属性
     * @return 构造的新问句
     */
    public static String anaphoraResolution(EaHistory eah, String question, String uid) {

        isUsingPronounMap.get(uid)[0] = isUsingPronounMap.get(uid)[1];
        isUsingPronounMap.get(uid)[1] = false;

        // 利用HanLP分词，遍历词和词性
        List<Term> terms = HanLP.segment(question);
        String newQuest = question;
        
        Boolean flagAttr = false, flagEntity = false;
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
                if (flagEntity && flagAttr) {
                	String str= String.join("，", eah.getHistEntities());
                	newQuest = question.replace(term.word, str);
                    isUsingPronounMap.get(uid)[1] = true;
                }
                // 问句中只出现的实体（如：神舟七号和它们的呢？）
                else if (flagEntity) {
                	ArrayList<String> strs = new ArrayList<>();
                	strs.addAll(eah.getHistEntities());
                	strs.addAll(eah.getHistAttrs());
                	String str= String.join("，", strs);
                	newQuest = question.replace(term.word, str);
                    isUsingPronounMap.get(uid)[1] = true;
                }
                // 问句中只出现的属性（如：它们的长度是多少？）
                else if (flagAttr) {
                	String str= String.join("，", eah.getHistEntities());
                	newQuest = question.replace(term.word, str);
                    isUsingPronounMap.get(uid)[1] = true;
                } else {
                    String str = String.join("，", eah.getHistEntities());
                    newQuest = question.replace(term.word, str);
                    isUsingPronounMap.get(uid)[1] = true;
                }
                break;
            }
            // 指示代词（单数）
            else if (keywords2.contains(term.word)) {
                // 问句同时出现实体和属性的情况（如：神舟七号和它的长度是多少？）
            	if (flagEntity && flagAttr) {
                	newQuest = question.replace(term.word, eah.getLastEntity());
                }
                // 问句中只出现的实体（如：神舟七号和它的呢？）
            	else if (flagEntity) {
                	newQuest = question.replace(term.word, eah.getLastEntity() + "，" + eah.getLastAttr());
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

    /**
     * 问句预处理
     * @return 将问句标准化，仅保留中文、数字和英文，并转化成大写，将文字转数字（如十转为10）
     */
    public static String preProcessQuestion(String question) {
        return ChineseNumberUtil.convertString(question.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "")).toUpperCase();
    }

}
