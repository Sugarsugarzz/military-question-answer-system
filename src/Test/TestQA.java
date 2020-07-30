package Test;

import QA.MilitaryQA;
import QA.MultiMilitaryQA;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {

        MultiMilitaryQA QA = new MultiMilitaryQA();
        
        /*while (true) {
        	String question = new Scanner(System.in).next();
        	QA.qa_main(question);
        }*/

        // 多轮测试
//        QA.qa_main("我想找中国的神舟七号");
//        QA.qa_main("歼-10战斗机的长度");
//        QA.qa_main("神舟六号载人飞船的长度");
//        QA.qa_main("他们的生产商");

        // 国家及类别名
        QA.qa_main("中国的战斗机有哪些？");

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
//        QA.qa_main("战斗机里长度最短的是哪个？");

        // 单属性单类别区间
//        QA.qa_main("长度大于25米的战斗机有哪些？");



    }
}
