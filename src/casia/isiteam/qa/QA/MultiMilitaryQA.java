package casia.isiteam.qa.QA;

import casia.isiteam.qa.Parser.QuestionParser;
import casia.isiteam.qa.Model.EAHistory;
import casia.isiteam.qa.Model.QA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public class MultiMilitaryQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static ArrayList<QA> QAs = new ArrayList<>();

	/**
	 * 多轮问答的主函数
	 * 在调用单轮问答基础上实现，加一层对问句的预处理过程
	 * @param question 问句
	 */
	public String qa_main(String uid, String question, String q_time) {
    	
    	int numQA = QAs.size();
    	QA qa;
    	if (numQA == 0) {
			question = QuestionParser.preProcessQuestion(question); //将原问题标准化
			System.out.println("处理后的Question - " + question);
			qa = MilitaryQA.oqa_main(question, question, uid, q_time);
		}
    	else {
			String standQuestion = QuestionParser.preProcessQuestion(question); //将原问题标准化
    		// 多轮，获取历史Entity和Attr，将问句中的指代词替换为对应实体名
			String newQuestion = QuestionParser.anaphoraResolution(getHistory(), standQuestion); //指代消解
			System.out.println("处理后的Question - " + question);
			qa = MilitaryQA.oqa_main(question, newQuestion, uid, q_time);
		}
		QAs.add(qa);
    	return qa.getAnswer();
	}

	/**
	 * 获取多轮问答过程中的历史信息，包括（最近一个属性、最近一个实体、历史所有属性、历史所有实体）
	 * @return 历史信息存储实体类 EAHistory
	 */
	private EAHistory getHistory() {
    	int numQA = QAs.size();
    	ArrayList<String> histAttrs = new ArrayList<>();
    	ArrayList<String> histEntities = new ArrayList<>();
    	String lastAttr = "";
    	String lastEntity = "";

    	for (int i = numQA - 1; i >= 0; i--) {
			histEntities.addAll(QAs.get(i).getEntities());
			histAttrs.addAll(QAs.get(i).getAttrs());
    	}
    	if (histAttrs.size() > 0)
			lastAttr = histAttrs.get(0);
    	if (histEntities.size() > 0)
			lastEntity = histEntities.get(0);

    	return new EAHistory(lastAttr, lastEntity, histAttrs, histEntities);
    }
}
