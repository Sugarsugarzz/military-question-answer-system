package casia.isiteam.militaryqa.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 别名映射标准名
 */
public class AliasMapper {

    /** 国家 */
    public static Map<String, String> Country = new HashMap<>();

    /** 一级分类 */
    public static Map<String, String> BigCategory = new HashMap<>();

    /** 二级分类 */
    public static Map<String, String> SmallCategory = new HashMap<>();

    /** 实体 */
    public static Map<String, Set<String>> Entity = new HashMap<>();

    /** 实体属性 */
    public static Map<String, String> Attribute = new HashMap<>();

    /** 比较词 */
    public static Map<String, String> Compare = new HashMap<>();

    /** 最值 */
    public static Map<String, String> Most = new HashMap<>();

    /** 核心概念词，不可重复 */
    public static Set<String> conceptsStopWords = new HashSet<>();
}
