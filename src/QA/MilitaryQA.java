package QA;

import Parser.QuestionParser;
import Searcher.AnswerSearcher;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class MilitaryQA {

    public void qa_main(String question) {

        System.out.println("输入的问题是：" + question);
        // 问句解析
        QuestionParser questionParser = new QuestionParser(question);
        Map<String, List<String>> parser_dict = questionParser.parser();
        // 答案搜索
        AnswerSearcher answerSearcher = new AnswerSearcher(parser_dict);
        ResultSet results = answerSearcher.getAnswer();
        // 打印答案

    }
}
