package casia.isiteam.militaryqa.common;

import java.util.*;

public class Constant {

    /** 单数代词 */
    public static List<String> singularPronouns = new ArrayList<>();

    /** 复数代词 */
    public static List<String> pluralPronouns = new ArrayList<>();

    /** 词性列表 */
    public static List<String> natures = new ArrayList<>();

    /** 词性：国家 */
    public static final String Nature_Country = "n_country";

    /** 词性：实体 */
    public static final String Nature_Entity = "n_entity";

    /** 词性：实体属性 */
    public static final String Nature_Attribute = "n_attr";

    /** 词性：一级分类 */
    public static final String Nature_Big_Category = "n_big";

    /** 词性：二级分类 */
    public static final String Nature_Small_Category = "n_small";

    /** 词性：比较词 */
    public static final String Nature_Compare = "n_compare";

    /** 词性：最值 */
    public static final String Nature_Most = "n_most";

    /** 词性：时间 */
    public static final String Nature_Time = "n_time";

    /** 词性：单位 */
    public static final String Nature_Unit = "n_unit";

    /** 词性：关键词 */
    public static final String Nature_Keywords = "keywords";

    /** 二级类别与c_id映射，用于新数据分类 */
    public static Map<String, Long> smallCategoriesMapping = new HashMap<>();


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
        natures.add(Nature_Country);
        natures.add(Nature_Entity);
        natures.add(Nature_Attribute);
        natures.add(Nature_Big_Category);
        natures.add(Nature_Small_Category);
        natures.add(Nature_Compare);
        natures.add(Nature_Most);
        natures.add(Nature_Time);
        natures.add(Nature_Unit);
        natures.add(Nature_Keywords);
    }
}
