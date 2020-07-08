package Searcher;

import Utils.FileOperator;

import java.util.Map;

public class Mapper {

    /**
     * 构建（问句中涉及的词形式，数据库中标准词形式）的映射关系
     * 用于在数据库中检索答案
     */

    // 国家
    public static Map<String, String> Country = buildCountry();
    // 一级分类
    public static Map<String, String> BigCategory = buildBigCategory();
    // 二级分类
    public static Map<String, String> SmallCategory = buildSmallCategory();
    // 武器实体
    public static Map<String, String> Weapon = buildWeapon();
    // 实体属性
    public static Map<String, String> Attribute = buildAttribute();
    // 比较词
    public static Map<String, String> Compare = buildCompare();
    // 最值
    public static Map<String, String> Most = buildMost();


    private static Map<String, String> buildCountry() {
        String filepath = "data/dict_for_match_query/country.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        return fileOperator.matchFileToMap();
    }

    private static Map<String, String> buildBigCategory() {
        String filepath = "data/dict_for_match_query/big_category.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        return fileOperator.matchFileToMap();
    }

    private static Map<String, String> buildSmallCategory() {
        String filepath = "data/dict_for_match_query/small_category.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        return fileOperator.matchFileToMap();
    }

    private static Map<String, String> buildWeapon() {
        String filepath = "data/dict_for_match_query/weapon.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        return fileOperator.matchFileToMap();
    }

    private static Map<String, String> buildAttribute() {
        String filepath = "data/dict_for_match_query/attribute.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        return fileOperator.matchFileToMap();
    }

    private static Map<String, String> buildCompare() {
        String filepath = "data/dict_for_match_query/compare.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        return fileOperator.matchFileToMap();
    }

    private static Map<String, String> buildMost() {
        String filepath = "data/dict_for_match_query/most.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        return fileOperator.matchFileToMap();
    }


}
