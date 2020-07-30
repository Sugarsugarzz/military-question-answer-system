package QA;

import Model.Answer;
import Model.QA;
import Parser.QuestionParser;
import Searcher.AnswerSearcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;


public class MilitaryQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private QuestionParser questionParser = new QuestionParser();
    private AnswerSearcher answerSearcher = new AnswerSearcher();

    /**
     * 单轮问答主函数
     * @param question 问句
     * @return QA实体类，包含该轮问答的词性字典、问句和答案
     */
    public QA oqa_main(String question) {

        logger.info("Question is ：" + question);
        // 问句解析
        logger.info("Parsing Question...");
        Map<String, List<String>> parser_dict = questionParser.parser(question);
        // 答案检索
        logger.info("Searching Answer...");
        List<Answer> results = answerSearcher.getAnswer(parser_dict);
        // 打印答案
        logger.info("Answer is ：" + results);
        return new QA(parser_dict, question, results);
    }
}
