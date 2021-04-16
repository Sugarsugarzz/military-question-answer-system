package casia.isiteam.militaryqa.controller;

import casia.isiteam.militaryqa.common.QaStatusCache;
import casia.isiteam.militaryqa.mapper.master.AnswerMapper;
import casia.isiteam.militaryqa.model.Qa;
import casia.isiteam.militaryqa.service.QuestionParserService;
import casia.isiteam.militaryqa.service.AnswerSearcherService;
import casia.isiteam.militaryqa.service.PreprocessService;
import casia.isiteam.militaryqa.utils.MultiQaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@Slf4j
@RestController
public class IndexController {

    @Autowired
    AnswerMapper answerMapper;
    @Autowired
    PreprocessService preprocessService;
    @Autowired
    QuestionParserService questionParserService;
    @Autowired
    AnswerSearcherService answerSearcherService;


    @RequestMapping("/qa")
    public String index(@RequestParam("uid") String uid, @RequestParam("q") String q) {

        // 初始化该用户提问状态
        if (!QaStatusCache.Qas.containsKey(uid)) {
            QaStatusCache.Qas.put(uid, new ArrayList<>());
        }
        if (!QaStatusCache.isUsingPronounMap.containsKey(uid)) {
            QaStatusCache.isUsingPronounMap.put(uid, new boolean[] {false, false});
        }
        return multiQaMain(uid, q);
    }

    /**
     * 多轮问答的主函数
     * 在调用单轮问答基础上实现，加一层对问句的预处理过程
     * @param question 问句
     */
    public String multiQaMain(String uid, String question) {
        Qa qa;
        //将原问题标准化
        String standQuestion = MultiQaUtil.preProcessQuestion(question);
        if (QaStatusCache.Qas.get(uid).size() == 0) {
            qa = singleQaMain(uid, standQuestion, question);
        } else {
            // 多轮，获取历史Entity和Attr，将问句中的指代词替换为对应实体名
            String newQuestion = MultiQaUtil.anaphoraResolution(MultiQaUtil.getHistory(uid), standQuestion, uid);
            qa = singleQaMain(uid, newQuestion, question);
        }
        // 多轮，用户提问由复数代词变成其他方式时，清空QAs
        if (QaStatusCache.isUsingPronounMap.get(uid)[0] && !QaStatusCache.isUsingPronounMap.get(uid)[1]) {
            QaStatusCache.Qas.get(uid).clear();
        }

        QaStatusCache.Qas.get(uid).add(qa);
        return qa.getAnswer();
    }

    /**
     * 单轮问答主函数
     * @param uid 用户id
     * @param question 预处理后的问句
     * @param originQuestion 原始问句
     * @return QA实体类，包含该轮问答的词性字典、问句和答案
     */
    public Qa singleQaMain(String uid, String question, String originQuestion) {

        log.info("Original Question is : {}", originQuestion);
        log.info("Processed Question is ：{}", question);
        // 问句解析
        log.info("Parsing Question...");
        Map<String, List<String>> parserDict = questionParserService.parser(question);
        // 答案检索
        log.info("Searching Answer...");
        String answer = answerSearcherService.getAnswer(parserDict);
        // 打印答案
        log.info("Answer is ：{}", answer);
        // 将该轮问答信息存入数据库
        answerMapper.saveQaInfo(uid, originQuestion, answer);

        return new Qa(parserDict.get("n_entity"), parserDict.get("n_attr"), question, answer);
    }

    @RequestMapping("/getAttrs")
    public Object getAttrsByEntityName(@RequestParam("id") Long id) {
        return answerMapper.getAttrsByEntityName(id);
    }
}
