package casia.isiteam.militaryqa.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessUtil {

    /**
     * 将<时间>处理成标准形式
     * @param times 问句中的时间项
     * @return 标准形式列表
     */
    public static List<String> processTime(List<String> times) {
        List<String> timeItems = new ArrayList<>();
        for (String item: times) {
            // 处理成 YYYY-mm-dd 的标准形式
            Matcher mYear = Pattern.compile("\\d{4}年").matcher(item);
            Matcher mMonth = Pattern.compile("\\d{1,2}月").matcher(item);
            Matcher mDay = Pattern.compile("\\d{1,2}日").matcher(item);
            String year = "", month = "", day = "", date = "";
            if (mYear.find()) {
                year = mYear.group().replace("年", "");
                if (mMonth.find()) {
                    month = mMonth.group().replace("月", "");
                }
                if (mDay.find()) {
                    day = mDay.group().replace("日", "");
                }
                date = year + "-" + dateCompletion(month) + "-" + dateCompletion(day);
            }
            timeItems.add(date);
        }
        return timeItems;
    }

    /**
     * 将<单位数值>处理成标准形式
     * @param units 问句中的单位数值项
     * @return 标准形式列表
     */
    public static List<String> processUnit(List<String> units) {
        List<String> unitItems = new ArrayList<>();
        for (String item: units) {
            Matcher mUnit = Pattern.compile("[0-9]+(.[0-9]+)?").matcher(item);
            while (mUnit.find()) {
                unitItems.add(mUnit.group());
            }
        }
        return unitItems;
    }

    /**
     * 日期补全，一位的前面补0
     * @param date 原始日期
     * @return 处理后的日期
     */
    private static String dateCompletion(String date) {
        if ("".equals(date)) {
            date = "01";
        }
        if (Integer.parseInt(date) < 10 && date.length() < 2) {
            date = "0" + date;
        }
        return date;
    }
}
