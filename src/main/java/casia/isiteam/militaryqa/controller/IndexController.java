package casia.isiteam.militaryqa.controller;

import casia.isiteam.militaryqa.main.MultiMilitaryQA;
import casia.isiteam.militaryqa.parser.QuestionParser;
import casia.isiteam.militaryqa.searcher.DictMapper;
import casia.isiteam.militaryqa.utils.DbFieldUpdater;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
public class IndexController {

    private final MultiMilitaryQA qa = new MultiMilitaryQA();

    @RequestMapping("/qa")
    public String index(@RequestParam("uid") String uid, @RequestParam("q") String q) {

        // 初始化
        if (!MultiMilitaryQA.QAs.containsKey(uid)) {
            MultiMilitaryQA.QAs.put(uid, new ArrayList<>());
        }
        if (!QuestionParser.isUsingPronounMap.containsKey(uid)) {
            QuestionParser.isUsingPronounMap.put(uid, new boolean[] {false, false});
        }
        return qa.qa_main(uid, q);
    }

    @RequestMapping("/updateDbField")
    public String updateDb() {
        try {
            DbFieldUpdater.addEntityAliasToDB(); // entity  6000 entities for 15min

            DbFieldUpdater.getConceptsAndSameasToDB();
            DbFieldUpdater.getEntitiesAndSameasToDB();

            DbFieldUpdater.getDBToCustomDictionary();
            DictMapper.initDictMapper();
            return "success";
        } catch (Exception e) {
            return "fail - " + e;
        }
    }

    @RequestMapping("/updateDict")
    public String update() {
        try {
            DbFieldUpdater.getDBToCustomDictionary();
            DictMapper.initDictMapper();
            return "success";
        } catch (Exception e) {
            return "fail - " + e;
        }
    }
}
