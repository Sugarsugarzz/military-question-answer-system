package Test;

import casia.basic.analysisgrop.es.transport.EsClientUtil;
import casia.basic.analysisgrop.es.transport.EsSearchClient;
import casia.basic.analysisgrop.es.transport.common.ISIOperator;
import casia.basic.analysisgrop.es.transport.common.KeywordsCombine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestES {

    public static void main(String[] args) {

        // 初始化
        EsClientUtil.initEsConfig("isiteam-es-zh-nodes", "192.168.6.123", 9300);
        EsSearchClient client = new EsSearchClient(new String[] {"entities", "concepts", "entity_attr"});

        // 1、测试 短语查询
        // 设置返回的字段
//        String[] fields = new String[1];
//        fields[0] = "entity_name";
//
//        // 设置筛选条件
//        client.addMultFieldsPhrasePrefixQuery(new String[] {"entity_name"}, "直升机", ISIOperator.MUST);
////        client.addPhrasePrefixQuery("entity_name", "直升机", " ", 0, ISIOperator.SHOULD);
//        // 执行
//        client.execute(fields);
//
//        // 打印结果
//        List<String[]> results = client.getResults();
//        for (String[] strings: results) {
//            for (String s: strings) {
//                System.out.print(s + " - ");
//            }
//            System.out.println();
//        }

        // 2、测试 关键词查询
        // 返回字段
//        String[] fields = new String[2];
//        fields[0] = "concept_name";
//        fields[1] = "c_id";
//
//        client.addKeywordsQuery("concept_name", "战斗机", ISIOperator.MUST, KeywordsCombine.AND);
//        client.execute(fields);
//        //        // 打印结果
//        List<String[]> results = client.getResults();
//        for (String[] strings: results) {
//            System.out.println(Arrays.toString(strings));
//            for (String s: strings) {
//                System.out.print(s + " - ");
//            }
//            System.out.println();

        // 3、问答 测试
        // Question: 中国的战斗机有哪些？
        // （1）查到 战斗机 对应的 id
        client = new EsSearchClient(new String[] {"concepts"});
        client.addPrimitiveTermQuery("concept_name", new String[] {"战斗机"}, ISIOperator.MUST);
        client.setRow(1);
        client.execute();
        String c_id = client.getResults().get(0)[1];
        System.out.println(c_id);

        // （2）根据战斗机的id，从实体列表找到所有战斗机
        client = new EsSearchClient(new String[] {"entities"});
        client.addPrimitiveTermQuery("concept_id", c_id, ISIOperator.MUST);
        client.setRow(500);
        client.execute(new String[] {"entity_id", "entity_name"});
        List<String> entity_ids = new ArrayList<>();
        for (String[] strings: client.getResults()) {
            System.out.println(Arrays.toString(strings));
            entity_ids.add(strings[1]);
        }
        System.out.println(entity_ids);

        // （3）根据所有战斗机id，筛出所有产国为中国的战斗机
        client = new EsSearchClient(new String[] {"entity_attr"});
        client.addPrimitiveTermQuery("entity_id", entity_ids, ISIOperator.MUST);
        client.addPrimitiveTermQuery("attr_value", "中国", ISIOperator.MUST);
        client.setRow(100);
        client.execute(new String[] {"entity_id", "attr_value"});
        List<String> lst = new ArrayList<>();
        for (String[] strings: client.getResults()) {
            System.out.println(Arrays.toString(strings));
            lst.add(strings[1]);
        }
        System.out.println(lst);

        // （4）根据筛出的战斗机id，再返回实体列表找出对应实体
        client = new EsSearchClient(new String[] {"entities"});
        client.addPrimitiveTermQuery("entity_id", lst, ISIOperator.MUST);
        client.setRow(100);
        client.execute(new String[] {"entity_id", "entity_name"});
        for (String[] strings: client.getResults()) {
            System.out.println(Arrays.toString(strings));
        }


    }
}

