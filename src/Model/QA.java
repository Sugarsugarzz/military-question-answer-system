package Model;

import java.util.List;

public class QA {

    // 历史实体
    private List<String> entities;
    // 历史属性
	private List<String> attrs;
    // 问题
    private String question;
    // 回答
    private List<Answer> answer;

	public QA(List<String> entities, List<String> attrs, String question, List<Answer> answer) {
		this.entities = entities;
		this.attrs = attrs;
		this.question = question;
		this.answer = answer;
	}

	public List<String> getEntities() {
		return entities;
	}

	public void setEntities(List<String> entities) {
		this.entities = entities;
	}

	public List<String> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<String> attrs) {
		this.attrs = attrs;
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
		return "QA{" +
				"entities=" + entities +
				", attrs=" + attrs +
				", question='" + question + '\'' +
				", answer=" + answer +
				'}';
	}
}
