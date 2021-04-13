package casia.isiteam.militaryqa.main;

import casia.isiteam.militaryqa.model.EaHistory;
import casia.isiteam.militaryqa.model.Qa;
import casia.isiteam.militaryqa.parser.QuestionParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class MultiMilitaryQA {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static Map<String, ArrayList<Qa>> QAs = new HashMap<>();

	/**
	 * 多轮问答的主函数
	 * 在调用单轮问答基础上实现，加一层对问句的预处理过程
	 * @param question 问句
	 */
	public String qa_main(String uid, String question) {

    	Qa qa;
		//将原问题标准化
		String standQuestion = QuestionParser.preProcessQuestion(question);
    	if (QAs.get(uid).size() == 0) {
			qa = MilitaryQA.oqa_main(uid, standQuestion, question);
		} else {
    		// 多轮，获取历史Entity和Attr，将问句中的指代词替换为对应实体名
			String newQuestion = QuestionParser.anaphoraResolution(getHistory(uid), standQuestion, uid);
			qa = MilitaryQA.oqa_main(uid, newQuestion, question);
		}
    	// 多轮，用户提问由复数代词变成其他方式时，清空QAs
		if (QuestionParser.isUsingPronounMap.get(uid)[0] && !QuestionParser.isUsingPronounMap.get(uid)[1]) {
			QAs.get(uid).clear();
		}

		QAs.get(uid).add(qa);
    	return qa.getAnswer();
	}

	/**
	 * 获取多轮问答过程中的历史信息，包括（最近一个属性、最近一个实体、历史所有属性、历史所有实体）
	 * @return 历史信息存储实体类 EAHistory
	 */
	private EaHistory getHistory(String uid) {
		int numQas = QAs.get(uid).size();
    	Set<String> histAttrs = new LinkedHashSet<>();
    	Set<String> histEntities = new LinkedHashSet<>();
    	String lastAttr = "", lastEntity = "";

    	for (int i = numQas - 1; i >= 0; i--) {
			histEntities.addAll(QAs.get(uid).get(i).getEntities());
			histAttrs.addAll(QAs.get(uid).get(i).getAttrs());
    	}
    	if (histAttrs.size() > 0) {
			lastAttr = histAttrs.iterator().next();
		}
    	if (histEntities.size() > 0) {
			lastEntity = histEntities.iterator().next();
		}
    	return new EaHistory(lastAttr, lastEntity, histAttrs, histEntities);
    }
}
