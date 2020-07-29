package QA;

import Model.Answer;
import Model.EAHistory;
import Model.QA;
import Parser.QuestionParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.glass.ui.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MultiMilitaryQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static ArrayList<QA> QAs = new ArrayList<QA>();

    public void qa_main(String question) {
    	
    	int numQA = QAs.size(); 
    	MilitaryQA oQA = new MilitaryQA();
    	if(numQA==0)
    		QAs.add(oQA.oqa_main(question));
    	else {
    		QuestionParser questionParser = new QuestionParser();
    		EAHistory eah = getHistory();
    		String newQuestion = questionParser.preProcessQuestion(eah, question);
    		System.out.println(" new question: ");
    		System.out.println(newQuestion);
    		QAs.add(oQA.oqa_main(newQuestion));
    	}
        //System.out.println(parser_dict);
    }
    
    public EAHistory getHistory() {
    	int numQA = QAs.size();
    	System.out.println(" num QAs : "+ numQA);
    	ArrayList<String> attrs = new ArrayList<String>();
    	ArrayList<String> entitys = new ArrayList<String>();
    	String attr = "";
    	String entity = "";
    	for (int i = numQA - 1; i >= 0; i--) {
    		Map<String, List<String>> parser_dict = QAs.get(i).getParser_dict();
    		//List<Answer> answer = QAs.get(i).getAnswer();
    		attrs.addAll(parser_dict.get("n_attr"));
    		entitys.addAll(parser_dict.get("n_entity"));
    	}
    	if (attrs.size()>0)
    		attr = attrs.get(0);
    	if (entitys.size()>0)
    		entity = entitys.get(0);
    	
    	System.out.println(attr);
    	System.out.println(entity);
    	System.out.println(attrs.size());
    	System.out.println(entitys.size());
    	System.out.println(attrs);
    	System.out.println(entitys);
    	
    	EAHistory eah = new EAHistory(attr, entity, attrs, entitys);
    	return eah;
    }
}
