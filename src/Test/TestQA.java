package Test;

import QA.MilitaryQA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {

        MilitaryQA QA = new MilitaryQA();
        // 国家及类别名
//        QA.qa_main("中国的战斗机有哪些？");

        // 单实体
//        QA.qa_main("我想找中国的神舟七号");

        // 多实体
//        QA.qa_main("我想找中国的神舟五号和神舟七号");

        // 单实体单属性/多属性
//        QA.qa_main("神舟七号的长度、发射地点、生产商、原产国、简介？");

        // 多实体单属性/多属性
//        QA.qa_main("神舟五号、神舟七号和歼-20战斗机的长度、发射地点、生产商、原产国和简介？");

        // 全类别属性最值
//        QA.qa_main("武器装备里长度最长的是哪个？");

        // 单类别属性最值
        QA.qa_main("战斗机里长度最短的是哪个？");
    }
}
