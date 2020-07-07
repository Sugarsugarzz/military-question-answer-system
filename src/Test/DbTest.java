package Test;

import Utils.DbOperator;
import Utils.FileOperator;

import java.util.List;

public class DbTest {

    public static void main(String[] args) {

        // 初始化数据库连接
        DbOperator dbOperator = new DbOperator();

        // 获取 Weapon
        List<String> weapons = dbOperator.getWeapons();
        // 利用文件处理工具类将List存入词典文件中
        FileOperator fileOperator = new FileOperator();
        String filepath = "data/dict_for_match_query/weapon.txt";
        fileOperator.entitiesToFile(weapons, filepath);

        // 获取 Big Category
        List<String> bigCategories = dbOperator.getBigCategory();
        // 利用文件处理工具类将List存入词典文件中
        filepath = "data/dict_for_match_query/big_category.txt";
        fileOperator.entitiesToFile(bigCategories, filepath);

        // 获取 Small Category
        List<String> smallCategories = dbOperator.getSmallCategory();
        // 利用文件处理工具类将List存入词典文件中
        filepath = "data/dict_for_match_query/small_category.txt";
        fileOperator.entitiesToFile(smallCategories, filepath);

        // 获取 Attribute
        List<String> attributes = dbOperator.getAttributes();
        // 利用文件处理工具类将List存入词典文件中
        filepath = "data/dict_for_match_query/attribute.txt";
        fileOperator.entitiesToFile(attributes, filepath);


    }
}
