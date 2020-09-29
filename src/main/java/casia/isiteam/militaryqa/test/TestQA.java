package casia.isiteam.militaryqa.test;

import casia.isiteam.militaryqa.main.MultiMilitaryQA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class TestQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {

        MultiMilitaryQA QA = new MultiMilitaryQA();
        String answer;

        while (true) {
        	String question = new Scanner(System.in).nextLine();
        	QA.qa_main("999", question);
        }

        // 多轮测试
//        answer = com.isitem.qa.QA.qa_main("小黑", "我想找中国的神舟七号", sf.format(new Date()));
//        answer = com.isitem.qa.QA.qa_main("小黑", "歼10的长度", sf.format(new Date()));
//        answer = com.isitem.qa.QA.qa_main("小黑", "神舟六号的长度", sf.format(new Date()));  // 神舟六号没有长度属性lol
//        answer = com.isitem.qa.QA.qa_main("小黑", "他们的生产商", sf.format(new Date()));

        // 大类别名
//        answer = com.isitem.qa.QA.qa_main("小明", "枪械和军用飞机", sf.format(new Date()));

        // 小类别名
//        answer = com.isitem.qa.QA.qa_main("小明", "战斗机有哪些？", sf.format(new Date()));

        // 国家及类别名
//        answer = com.isitem.qa.QA.qa_main("小明", "中国的战斗机有哪些？", sf.format(new Date()));

        // 单实体
//        answer = com.isitem.qa.QA.qa_main("小明", "我想找中国的神舟七号", sf.format(new Date()));

        // 多实体
//        answer = com.isitem.qa.QA.qa_main("小明", "我想找中国的神舟五号和神舟七号", sf.format(new Date()));

        // 单实体单属性/多属性
//        answer = com.isitem.qa.QA.qa_main("小明", "神舟七号的长度、发射地点、生产商、原产国、简介？", sf.format(new Date()));

        // 多实体单属性/多属性
//        answer = com.isitem.qa.QA.qa_main("小明", "神舟五号、神舟七号和歼-20战斗机的长度、发射地点、生产商、原产国和简介？", sf.format(new Date()));

        // 全类别属性最值
//        answer = com.isitem.qa.QA.qa_main("小明", "长度最长的是哪个？", sf.format(new Date()));

        // 单类别属性最值
//        answer = com.isitem.qa.QA.qa_main("小明", "战斗机里长度最短的是哪个？", sf.format(new Date()));

        // 单属性单类别单区间
//        answer = com.isitem.qa.QA.qa_main("小明", "长度大于25米的战斗机有哪些？", sf.format(new Date()));
//        answer = com.isitem.qa.QA.qa_main("小明", "发射日期大于2011年的宇宙飞船？", sf.format(new Date()));
//        answer = com.isitem.qa.QA.qa_main("小明", "宇宙飞船里发射日期在2011年之后的有哪些？", sf.format(new Date()));

        // 单属性单类别多区间
//        answer = com.isitem.qa.QA.qa_main("小明", "发射日期小于2010年且大于2005年的宇宙飞船", sf.format(new Date()));

        // 多属性单类别多区间
//        answer = com.isitem.qa.QA.qa_main("小明", "长度大于25米且高度大于5米的战斗机有哪些？", sf.format(new Date()));
//        answer = com.isitem.qa.QA.qa_main("小明", "首飞时间晚于2010年且高度大于4米的战斗机有哪些？", sf.format(new Date()));

        // 热点模式 测试
//        answer = com.isitem.qa.QA.qa_main("小明", "我想看上周有关歼20战斗机的热点新闻", sf.format(new Date()));
//        answer = com.isitem.qa.QA.qa_main("小明", "我想看去年五月有关歼20战斗机的热点新闻", sf.format(new Date()));

        // 直达模式 测试
//        answer = com.isitem.qa.QA.qa_main("小明", "打开浏览历史", sf.format(new Date()));
//        answer = com.isitem.qa.QA.qa_main("小明", "前往我的收藏", sf.format(new Date()));

    }
}
