package casia.isiteam.militaryqa;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

public class TestHanlp {

    public static void main(String[] args) {

        CustomDictionary.add("神舟五号和神舟六号", "hahahahahha");
        List<Term> res = HanLP.segment("神舟五号和神舟六号和神舟七号和神舟十号的长度、发射地点、原产国？");
        for (Term term: res) {
            // 返回 词 和 词性
            System.out.println(term.word + " ： " + term.nature);
        }

        System.out.println("===");
        CustomDictionary.remove("哈哈哈fdsafsa哈哈");
        CustomDictionary.add("神舟五号和神舟六号", "xixiixixixi");
        res = HanLP.segment("神舟五号和神舟六号和神舟七号和神舟十号的长度、发射地点、原产国？");
        for (Term term: res) {
            // 返回 词 和 词性
            System.out.println(term.word + " ： " + term.nature);
        }
    }
}
