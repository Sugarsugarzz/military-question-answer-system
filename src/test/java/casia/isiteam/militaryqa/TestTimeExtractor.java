package casia.isiteam.militaryqa;

import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;

import java.util.Map;

public class TestTimeExtractor {
    public static void main(String[] args) {

        TimeNormalizer normalizer = new TimeNormalizer();

        String sentence = "今年5月1日到5月3日的热点新闻";
        sentence = "我想看本月的热点新闻";  // 只有起始时间
        sentence = "我想看昨天和今天的热点新闻";
        sentence = "我想看5月1日到3日的热点新闻";
        sentence = "我想看5.1到5.3的热点新闻";
        sentence = "我想看5-1到5-3的热点新闻";
        sentence = "我想看上周的热点新闻";  // 只有起始时间
        sentence = "我想看当月的热点新闻";  // 只有起始时间
        sentence = "我想看上个月的热点新闻";  // 只有起始时间
        sentence = "我想看9月的热点新闻";  // 只有起始时间
        sentence = "我想看三年前5.4的热点新闻";
        sentence = "我想看2020年5月4日到9月1日的热点新闻";
        sentence = "我想看2020年5月到9月的热点新闻";

        Map<String, TimeUnit> map = normalizer.parse(sentence);
        for (String key : map.keySet()) {
            System.out.println(key + " - " + DateUtil.formatDateDefault(map.get(key).getTime()));
        }
    }
}
