package casia.isiteam.militaryqa.test;

import casia.isiteam.militaryqa.utils.DbFieldUpdater;

public class TestOperator {
    public static void main(String[] args) {

        // 将自定义的 concept 和 entity 别名更新到对应 sameas 别名表中
//        DbFieldUpdater.addConceptAliasToDB();  // concept 2minutes
        DbFieldUpdater.addEntityAliasToDB(); // entity  6000 entities for 15min

        // 将 文件或数据库中原名与别名映射关系 存入数据库的 match_dict 表
//        DbFieldUpdater.getCountryCompareMostToDB();  // 5s
        DbFieldUpdater.getConceptsAndSameasToDB();    // 5s
        DbFieldUpdater.getEntitiesAndSameasToDB();  // 4minutes

        // 根据数据库的 match_dict 表，获取分词词典到本地，加载到分词器中
//        DbFieldUpdater.getDBToSegmentDict();

    }
}
