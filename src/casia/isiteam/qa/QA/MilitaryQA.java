package casia.isiteam.qa.QA;

import casia.isiteam.qa.Parser.QuestionParser;
import casia.isiteam.qa.Searcher.AnswerSearcher;
import casia.isiteam.qa.Utils.DBKit;
import casia.isiteam.qa.Model.QA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;


public class MilitaryQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * 单轮问答主函数
     * @param originQuestion 原始问句
     * @param question 预处理后的问句
     * @param uid 用户id
     * @param q_time 提问时间
     * @return QA实体类，包含该轮问答的词性字典、问句和答案
     */
    public static QA oqa_main(String originQuestion, String question, String uid, String q_time) {

        logger.info("Original Question is : " + originQuestion);
        logger.info("Processed Question is ：" + question);
        // 问句解析
        logger.info("Parsing Question...");
        Map<String, List<String>> parser_dict = QuestionParser.parser(question);
        // 答案检索
        logger.info("Searching Answer...");
        String answer = AnswerSearcher.getAnswer(parser_dict);
        // 打印答案
        logger.info("Answer is ：" + answer);
        // 将该轮问答信息存入数据库
        DBKit.insertQAInfo(uid, q_time, originQuestion, answer);

        return new QA(parser_dict.get("n_entity"), parser_dict.get("n_attr"), question, answer);
    }
}
