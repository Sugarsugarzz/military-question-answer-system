package Test;

import QA.MilitaryQA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {

        MilitaryQA QA = new MilitaryQA();
        // 国家实体类别
        QA.qa_main("中国的战斗机有哪些？");
        // 单实体单属性/多属性
//        QA.qa_main("神舟七号的长度、发射地点、生产商、原产国、简介？");


    }
}
