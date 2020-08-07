package casia.isiteam.qa.Test;

import casia.isiteam.qa.Utils.DbOperator;

public class TestOperator {
    public static void main(String[] args) {

        // 将自定义的 concept 和 entity 别名上传到对应 sameas 表中
//        DbOperator.addConceptAliasToDB();

        // 将 match file 存入数据库的 match_dict 表，以后直接从数据库读，不从本地读
//        DbOperator.getCountryCompareMostToDB();
//        DbOperator.getConceptsAndSameasToDB();
//        DbOperator.getEntitiesAndSameasToDB();

        // 根据数据库的 match_dict 表，获取分词词典到本地，加载到分词器中
//        DbOperator.getDBToSegmentDict();

    }
}
