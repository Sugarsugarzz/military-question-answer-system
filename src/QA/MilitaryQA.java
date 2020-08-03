package QA;

import Model.Answer;
import Model.QA;
import Parser.QuestionParser;
import Searcher.AnswerSearcher;
import Searcher.DbSearcher;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;


public class MilitaryQA {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * 单轮问答主函数
     * @param originQuestion 原始问句
     * @param question 预处理后的问句
     * @param uid 用户id
     * @param q_time 提问时间
     * @return QA实体类，包含该轮问答的词性字典、问句和答案
     */
    public QA oqa_main(String originQuestion, String question, String uid, String q_time) {

        logger.info("Question is ：" + question);
        // 问句解析
        logger.info("Parsing Question...");
        Map<String, List<String>> parser_dict = QuestionParser.parser(question);
        // 答案检索
        logger.info("Searching Answer...");
        List<Answer> results = AnswerSearcher.getAnswer(parser_dict);
        // 打印答案
        logger.info("Answer is ：" + results);
        // 将该轮问答信息存入数据库
        DbSearcher.insertQAInfo(uid, q_time, originQuestion, assembleJSON(results));

        return new QA(parser_dict.get("n_entity"), parser_dict.get("n_attr"), question, results);
    }

    /**
     * 组装答案JSON串
     * @param results 答案实体列表
     * @return JSON
     */
    public String assembleJSON(List<Answer> results) {

        JSONArray jsonArray = new JSONArray();

        for (Answer answer: results) {
            JSONObject obj = new JSONObject();
            obj.put("entity_id", answer.getEntity_id());
            obj.put("entity_name", answer.getEntity_name());
            obj.put("attr_name", answer.getAttr_name());
            obj.put("attr_value", answer.getAttr_value());
            jsonArray.add(obj);
        }

        return jsonArray.toJSONString();
    }


}
