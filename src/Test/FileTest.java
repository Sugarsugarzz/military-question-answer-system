package Test;

import Utils.FileOperator;

import java.util.Map;

public class FileTest {

    public static void main(String[] args) {

        // 查看 （问句中涉及的词形式，数据库中标准形式）对
        String filepath = "data/dict_for_match_query/country.txt";
        FileOperator fileOperator = new FileOperator(filepath);
        Map<String, String> res = fileOperator.matchFileToMap();
//        for (Map.Entry<String, String> entry: res.entrySet()) {
//            System.out.println("Key：" + entry.getKey() + ", Value：" + entry.getValue());
//        }

        // 将 问句中所有可能涉及的实体词 加入到分词器的词典中
        fileOperator.matchFileToSegFile();

    }
}
