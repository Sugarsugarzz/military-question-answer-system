package casia.isiteam.militaryqa.common;

import java.util.*;

public class Constant {

    /** 单数代词 */
    public static List<String> singularPronouns = new ArrayList<>();

    /** 复数代词 */
    public static List<String> pluralPronouns = new ArrayList<>();

    /** 词性列表 */
    public static List<String> natures = new ArrayList<>();

    /** match_dict表中label与词性的映射关系 */
    public static Map<String, String> labelNatureMap = new HashMap<>();


    static {
        // 单数代词
        singularPronouns.add("它");
        singularPronouns.add("他");
        singularPronouns.add("她");
        singularPronouns.add("这");
        singularPronouns.add("这儿");
        singularPronouns.add("这个");
        singularPronouns.add("这里");

        // 复数代词
        pluralPronouns.add("它们");
        pluralPronouns.add("他们");
        pluralPronouns.add("她们");
        pluralPronouns.add("2者");
        pluralPronouns.add("这些");

        // 初始化已定义的词性列表
        natures.add("n_country");
        natures.add("n_entity");
        natures.add("n_attr");
        natures.add("n_big");
        natures.add("n_small");
        natures.add("n_compare");
        natures.add("n_most");
        natures.add("n_time");
        natures.add("n_unit");
        natures.add("keywords");

        // label -> nature
        labelNatureMap.put("country", "n_country");
        labelNatureMap.put("entity", "n_entity");
        labelNatureMap.put("attribute", "n_attr");
        labelNatureMap.put("big_category", "n_big");
        labelNatureMap.put("small_category", "n_small");
        labelNatureMap.put("compare", "n_compare");
        labelNatureMap.put("most", "n_most");
    }
}
