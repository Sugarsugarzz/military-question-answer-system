package com.isiteam.qa.Test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

public class TestHanlp {

    public static void main(String[] args) {

        List<Term> res = HanLP.segment("神舟五号和神舟六号的长度是多少？");
        res = HanLP.segment("神舟五号和神舟六号和神舟七号和神舟十号的长度、发射地点、原产国？");
        for (Term term: res) {
            // 返回 词 和 词性
            System.out.println(term.word + " ： " + term.nature);
        }
    }
}
