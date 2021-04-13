package casia.isiteam.militaryqa.common;

import java.util.*;

public class Constant {

    /**
     * 单数代词
     */
    public static List<String> singularPronouns = new ArrayList<>();
    /**
     * 复数代词
     */
    public static List<String> pluralPronouns = new ArrayList<>();
    /**
     * 词性列表
     */
    public static List<String> natures = new ArrayList<>();
    /**
     * 问句匹配模板
     */
    public static Map<String, List<List<String>>> patterns = new HashMap<>();

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
        natures.add("n_country");
        natures.add("n_entity");
        natures.add("n_attr");
        natures.add("n_big");
        natures.add("n_small");
        natures.add("n_compare");
        natures.add("n_most");
        natures.add("n_time");
        natures.add("n_unit");
        natures.add("keywords");

        // 初始化问句模板
        // 热点查询模式
        patterns.put("热点查询模式", new ArrayList<>());
        patterns.get("热点查询模式").add(Arrays.asList("3"));
        // 期刊查询模式
        patterns.put("期刊查询模式", new ArrayList<>());
        patterns.get("期刊查询模式").add(Arrays.asList("4"));

        // 报告查询模式
        patterns.put("报告查询模式", new ArrayList<>());
        patterns.get("报告查询模式").add(Arrays.asList("5"));

        // 直达模式：头条
        patterns.put("直达模式-头条", new ArrayList<>());
        patterns.get("直达模式-头条").add(Arrays.asList("61"));

        // 直达模式：百科
        patterns.put("直达模式-百科", new ArrayList<>());
        patterns.get("直达模式-百科").add(Arrays.asList("62"));

        // 直达模式：订阅
        patterns.put("直达模式-订阅", new ArrayList<>());
        patterns.get("直达模式-订阅").add(Arrays.asList("63"));

        // 直达模式：我的收藏
        patterns.put("直达模式-我的收藏", new ArrayList<>());
        patterns.get("直达模式-我的收藏").add(Arrays.asList("64"));

        // 直达模式：浏览历史
        patterns.put("直达模式-浏览历史", new ArrayList<>());
        patterns.get("直达模式-浏览历史").add(Arrays.asList("65"));

        // 辅助查询模式 ：大类别名，返回二级类别列表
        patterns.put("大类别名", new ArrayList<>());
        patterns.get("大类别名").add(Arrays.asList("n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_big", "n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_big", "n_big", "n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_country", "n_big"));
        patterns.get("大类别名").add(Arrays.asList("n_big", "n_country"));

        // 百科模式 ：小类别名，返回类别下所有实体列表
        patterns.put("小类别名", new ArrayList<>());
        patterns.get("小类别名").add(Arrays.asList("n_small"));
        patterns.get("小类别名").add(Arrays.asList("n_small", "n_small"));
        patterns.get("小类别名").add(Arrays.asList("n_small", "n_small", "n_small"));

        // 百科模式 ：国家及类别名
        patterns.put("国家及类别名", new ArrayList<>());
        patterns.get("国家及类别名").add(Arrays.asList("n_country", "n_small"));
        patterns.get("国家及类别名").add(Arrays.asList("n_small", "n_country"));

        // 百科模式 ：单实体
        patterns.put("单实体", new ArrayList<>());
        patterns.get("单实体").add(Arrays.asList("n_entity"));
        patterns.get("单实体").add(Arrays.asList("n_entity", "n_small"));
        patterns.get("单实体").add(Arrays.asList("n_country", "n_entity"));
        patterns.get("单实体").add(Arrays.asList("n_entity", "n_country"));

        // 百科模式 ：多实体
        patterns.put("多实体", new ArrayList<>());
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity", "n_entity"));
        patterns.get("多实体").add(Arrays.asList("n_country", "n_entity", "n_entity", "n_entity", "n_entity", "n_entity"));

        // 百科模式 ：单实体单属性/多属性
        patterns.put("单实体单属性/多属性", new ArrayList<>());
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("单实体单属性/多属性").add(Arrays.asList("n_country", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        // 百科模式 ：多实体单属性/多属性
        patterns.put("多实体单属性/多属性", new ArrayList<>());
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));
        patterns.get("多实体单属性/多属性").add(Arrays.asList("n_entity", "n_entity", "n_entity", "n_entity", "n_entity", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr", "n_attr"));

        // 百科模式 ：单属性单类别单区间
        patterns.put("单属性单类别单区间", new ArrayList<>());
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_small"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_small"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time"));
        patterns.get("单属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare"));

        // 百科模式 ：单属性单类别多区间
        patterns.put("单属性单类别多区间", new ArrayList<>());
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_compare", "n_unit", "n_small"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_compare", "n_unit"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_compare", "n_time", "n_small"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_time", "n_compare", "n_small"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time", "n_compare", "n_time"));
        patterns.get("单属性单类别多区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare", "n_time", "n_compare"));

        // 百科模式 ：多属性单类别单区间
        patterns.put("多属性单类别单区间", new ArrayList<>());
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_unit"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_time", "n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_time", "n_compare", "n_attr", "n_compare", "n_unit", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_time", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_attr", "n_compare", "n_unit", "n_attr", "n_time", "n_compare", "n_small"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_time", "n_attr", "n_compare", "n_unit"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_time", "n_compare", "n_attr", "n_compare", "n_unit"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_attr", "n_compare", "n_time"));
        patterns.get("多属性单类别单区间").add(Arrays.asList("n_small", "n_attr", "n_compare", "n_unit", "n_attr", "n_time", "n_compare"));

        // 百科模式 ：全类别属性最值
        patterns.put("全类别属性最值", new ArrayList<>());
        patterns.get("全类别属性最值").add(Arrays.asList("n_attr", "n_most"));
        patterns.get("全类别属性最值").add(Arrays.asList("n_most", "n_attr"));

        // 百科模式 ：单类别属性最值
        patterns.put("单类别属性最值", new ArrayList<>());
        patterns.get("单类别属性最值").add(Arrays.asList("n_small", "n_attr", "n_most"));
        patterns.get("单类别属性最值").add(Arrays.asList("n_small", "n_most", "n_attr"));
        patterns.get("单类别属性最值").add(Arrays.asList("n_attr", "n_most", "n_small"));
        patterns.get("单类别属性最值").add(Arrays.asList("n_attr", "n_small", "n_most"));
    }
}
