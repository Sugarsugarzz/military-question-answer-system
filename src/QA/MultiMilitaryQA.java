package QA;

import Model.EAHistory;
import Model.QA;
import Parser.QuestionParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MultiMilitaryQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static ArrayList<QA> QAs = new ArrayList<QA>();

	/**
	 * 多轮问答的主函数
	 * 在调用单轮问答基础上实现，加一层对问句的预处理过程
	 * @param question 问句
	 */
	public void qa_main(String uid, String question, String q_time) {
    	
    	int numQA = QAs.size(); 
    	MilitaryQA oQA = new MilitaryQA();
    	if(numQA == 0)
    		QAs.add(oQA.oqa_main(question, question, uid, q_time));
    	else {
    		// 多轮，获取历史Entity和Attr，将问句中的指代词替换为对应实体名
    		QuestionParser questionParser = new QuestionParser();
    		EAHistory eah = getHistory();
    		String newQuestion = questionParser.preProcessQuestion(eah, question);
    		QAs.add(oQA.oqa_main(question, newQuestion, uid, q_time));
    	}
    }

	/**
	 * 获取多轮问答过程中的历史信息，包括（最近一个属性、最近一个实体、历史所有属性、历史所有实体）
	 * @return 历史信息存储实体类
	 */
	public EAHistory getHistory() {
    	int numQA = QAs.size();
    	ArrayList<String> attrs = new ArrayList<>();
    	ArrayList<String> entitys = new ArrayList<>();
    	String attr = "";
    	String entity = "";
    	for (int i = numQA - 1; i >= 0; i--) {
    		Map<String, List<String>> parser_dict = QAs.get(i).getParser_dict();
    		attrs.addAll(parser_dict.get("n_attr"));
    		entitys.addAll(parser_dict.get("n_entity"));
    	}
    	if (attrs.size() > 0)
    		attr = attrs.get(0);
    	if (entitys.size() > 0)
    		entity = entitys.get(0);

    	return new EAHistory(attr, entity, attrs, entitys);
    }
}
