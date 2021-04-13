package casia.isiteam.militaryqa.main;

import casia.isiteam.militaryqa.model.Qa;
import casia.isiteam.militaryqa.parser.QuestionParser;
import casia.isiteam.militaryqa.searcher.AnswerSearcher;
import casia.isiteam.militaryqa.utils.DBKit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;


public class MilitaryQA {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * 单轮问答主函数
     * @param uid 用户id
     * @param question 预处理后的问句
     * @param originQuestion 原始问句
     * @return QA实体类，包含该轮问答的词性字典、问句和答案
     */
    public static Qa oqa_main(String uid, String question, String originQuestion) {

        logger.info("Original Question is : " + originQuestion);
        logger.info("Processed Question is ：" + question);
        // 问句解析
        logger.info("Parsing Question...");
        Map<String, List<String>> parserDict = QuestionParser.parser(question);
        // 答案检索
        logger.info("Searching Answer...");
        String answer = AnswerSearcher.getAnswer(parserDict);
        // 打印答案
        logger.info("Answer is ：" + answer);
        // 将该轮问答信息存入数据库
        DBKit.insertQAInfo(uid, originQuestion, answer);

        return new Qa(parserDict.get("n_entity"), parserDict.get("n_attr"), question, answer);
    }
}
