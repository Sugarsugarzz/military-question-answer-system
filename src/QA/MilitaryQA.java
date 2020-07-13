package QA;

import Model.Answer;
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

    public void qa_main(String question) {

        logger.info("Question is ：" + question);
        // 问句解析
        logger.info("Parsing Question...");
        Map<String, List<String>> parser_dict = questionParser.parser(question);
        // 答案检索
        logger.info("Searching Answer...");
        List<Answer> results = answerSearcher.getAnswer(parser_dict);
        // 打印答案
        logger.info("Answer is ：" + results);

    }
}
