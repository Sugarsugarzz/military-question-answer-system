package Model;

import java.util.List;
import java.util.Map;

public class QA {

    // 问句解析
    private Map<String, List<String>> parser_dict;
    // 问题
    private String question;
    // 回答
    private List<Answer> answer;
    
	public QA() {
		super();
	}
	
	public QA(Map<String, List<String>> parser_dict, String question, List<Answer> answer) {
		this.parser_dict = parser_dict;
		this.question = question;
		this.answer = answer;
	}
	
	public Map<String, List<String>> getParser_dict() {
		return parser_dict;
	}
	public void setParser_dict(Map<String, List<String>> parser_dict) {
		this.parser_dict = parser_dict;
	}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public List<Answer> getAnswer() {
		return answer;
	}
	public void setAnswer(List<Answer> answer) {
		this.answer = answer;
	}
	
	@Override
	public String toString() {
		return "QA [parser_dict=" + parser_dict + ", question=" + question + ", answer=" + answer + "]";
	}

}
