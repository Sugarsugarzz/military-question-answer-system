package casia.isiteam.militaryqa.main;

import casia.isiteam.militaryqa.common.Constant;
import casia.isiteam.militaryqa.model.EaHistory;
import casia.isiteam.militaryqa.model.Qa;
import casia.isiteam.militaryqa.utils.ChineseNumberUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class MultiMilitaryQA {

    // 存储多轮信息
    public static Map<String, ArrayList<Qa>> Qas = new HashMap<>();
	// [0] 为前状态，[1] 为当前状态
	public static Map<String, boolean[]> isUsingPronounMap = new HashMap<>();

	/**
	 * 多轮问答的主函数
	 * 在调用单轮问答基础上实现，加一层对问句的预处理过程
	 * @param question 问句
	 */
	public String qa_main(String uid, String question) {
    	Qa qa;
		//将原问题标准化
		String standQuestion = preProcessQuestion(question);
    	if (Qas.get(uid).size() == 0) {
			qa = MilitaryQA.oqa_main(uid, standQuestion, question);
		} else {
    		// 多轮，获取历史Entity和Attr，将问句中的指代词替换为对应实体名
			String newQuestion = anaphoraResolution(getHistory(uid), standQuestion, uid);
			qa = MilitaryQA.oqa_main(uid, newQuestion, question);
		}
    	// 多轮，用户提问由复数代词变成其他方式时，清空QAs
		if (isUsingPronounMap.get(uid)[0] && !isUsingPronounMap.get(uid)[1]) {
			Qas.get(uid).clear();
		}

		Qas.get(uid).add(qa);
    	return qa.getAnswer();
	}

	/**
	 * 问句预处理
	 * @return 将问句标准化，仅保留中文、数字和英文，并转化成大写，将文字转数字（如十转为10）
	 */
	public static String preProcessQuestion(String question) {
		return ChineseNumberUtil.convertString(question.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "")).toUpperCase();
	}

	/**
	 * 多轮问答中，处理问句中出现指代词的情况，将其替换为对应实体和属性
	 * @return 构造的新问句
	 */
	public static String anaphoraResolution(EaHistory eah, String question, String uid) {

		isUsingPronounMap.get(uid)[0] = isUsingPronounMap.get(uid)[1];
		isUsingPronounMap.get(uid)[1] = false;

		// 利用HanLP分词，遍历词和词性
		List<Term> terms = HanLP.segment(question);
		String newQuest = question;

		Boolean flagAttr = false, flagEntity = false;
		for (Term term : terms) {
			if (term.nature.toString().equals("n_entity")) {
				flagEntity = true;
			}
			if (term.nature.toString().equals("n_attr")) {
				flagAttr = true;
			}
		}

		for (Term term : terms) {
			// 指示代词（复数）
			if (Constant.pluralPronouns.contains(term.word)) {
				// 问句同时出现实体和属性的情况（如：神舟七号和它们的长度是多少？）
				if (flagEntity && flagAttr) {
					String str= String.join("，", eah.getHistEntities());
					newQuest = question.replace(term.word, str);
					isUsingPronounMap.get(uid)[1] = true;
				}
				// 问句中只出现的实体（如：神舟七号和它们的呢？）
				else if (flagEntity) {
					ArrayList<String> strs = new ArrayList<>();
					strs.addAll(eah.getHistEntities());
					strs.addAll(eah.getHistAttrs());
					String str= String.join("，", strs);
					newQuest = question.replace(term.word, str);
					isUsingPronounMap.get(uid)[1] = true;
				}
				// 问句中只出现的属性（如：它们的长度是多少？）
				else if (flagAttr) {
					String str= String.join("，", eah.getHistEntities());
					newQuest = question.replace(term.word, str);
					isUsingPronounMap.get(uid)[1] = true;
				} else {
					String str = String.join("，", eah.getHistEntities());
					newQuest = question.replace(term.word, str);
					isUsingPronounMap.get(uid)[1] = true;
				}
				break;
			}
			// 指示代词（单数）
			else if (Constant.singularPronouns.contains(term.word)) {
				// 问句同时出现实体和属性的情况（如：神舟七号和它的长度是多少？）
				if (flagEntity && flagAttr) {
					newQuest = question.replace(term.word, eah.getLastEntity());
				}
				// 问句中只出现的实体（如：神舟七号和它的呢？）
				else if (flagEntity) {
					newQuest = question.replace(term.word, eah.getLastEntity() + "，" + eah.getLastAttr());
				}
				// 问句中只出现的属性（如：它的长度是多少？）
				else if (flagAttr) {
					newQuest = question.replace(term.word, eah.getLastEntity());
				}
				break;
			}
		}
		return newQuest;
	}

	/**
	 * 获取多轮问答过程中的历史信息，包括（最近一个属性、最近一个实体、历史所有属性、历史所有实体）
	 * @return 历史信息存储实体类 EAHistory
	 */
	private EaHistory getHistory(String uid) {
		int numQas = Qas.get(uid).size();
    	Set<String> histAttrs = new LinkedHashSet<>();
    	Set<String> histEntities = new LinkedHashSet<>();
    	String lastAttr = "", lastEntity = "";

    	for (int i = numQas - 1; i >= 0; i--) {
			histEntities.addAll(Qas.get(uid).get(i).getEntities());
			histAttrs.addAll(Qas.get(uid).get(i).getAttrs());
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
