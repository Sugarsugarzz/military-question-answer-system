package casia.isiteam.militaryqa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Qa {

	/**
	 * 单轮问答中的实体
	 */
    private List<String> entities;
	/**
	 * 单轮问答中的属性
	 */
	private List<String> attrs;
	/**
	 * 问句
	 */
    private String question;
	/**
	 * 答案 JSON
	 */
	private String answer;
}
