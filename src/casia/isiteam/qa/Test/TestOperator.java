package casia.isiteam.qa.Test;

import casia.isiteam.qa.Utils.DbOperator;
import casia.isiteam.qa.Utils.EntityAliasExtractor;

public class TestOperator {
    public static void main(String[] args) {

        // 将自定义的 concept 和 entity 别名更新到对应 sameas 别名表中
        DbOperator.addConceptAliasToDB();  // concept 2minutes
        DbOperator.addEntityAliasToDB(); // entity  10minutes

        // 将 文件或数据库中原名与别名映射关系 存入数据库的 match_dict 表
        DbOperator.getCountryCompareMostToDB();  // 5s
        DbOperator.getConceptsAndSameasToDB();    // 5s
        DbOperator.getEntitiesAndSameasToDB();  // 4minutes

        // 根据数据库的 match_dict 表，获取分词词典到本地，加载到分词器中
        DbOperator.getDBToSegmentDict();

    }
}
