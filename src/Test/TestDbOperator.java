package Test;

import Utils.DbOperator;
import Utils.FileOperator;

import java.util.List;

public class TestDbOperator {

    public static void main(String[] args) {

        // 初始化数据库连接
        DbOperator dbOperator = new DbOperator();

        // 获取 Entity
        List<String> entities = dbOperator.getEntities();
        // 利用文件处理工具类将List存入词典文件中
        FileOperator fileOperator = new FileOperator();
        String filepath = "data/dict_for_match_query/entity.txt";
        fileOperator.entitiesToFile(entities, filepath);

        // 获取 Big Category
        List<String> bigCategories = dbOperator.getBigCategory();
        filepath = "data/dict_for_match_query/big_category.txt";
        fileOperator.entitiesToFile(bigCategories, filepath);

        // 获取 Small Category
        List<String> smallCategories = dbOperator.getSmallCategory();
        filepath = "data/dict_for_match_query/small_category.txt";
        fileOperator.entitiesToFile(smallCategories, filepath);

        // 获取 Attribute
        List<String> attributes = dbOperator.getAttributes();
        filepath = "data/dict_for_match_query/attribute.txt";
        fileOperator.entitiesToFile(attributes, filepath);


    }
}
