package QA;

import Parser.QuestionParser;
import Searcher.AnswerSearcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;


public class MilitaryQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public void qa_main(String question) {

        logger.info("Question is ：" + question);
        // 问句解析
        logger.info("Parsing Question...");
        QuestionParser questionParser = new QuestionParser(question);
        Map<String, List<String>> parser_dict = questionParser.parser();
        // 答案搜索
        logger.info("Searching Answer...");
        AnswerSearcher answerSearcher = new AnswerSearcher(parser_dict);
        List<Map<String, String>> results = answerSearcher.getAnswer();
        // 打印答案
        logger.info("Answer is ：" + results);

    }
}
