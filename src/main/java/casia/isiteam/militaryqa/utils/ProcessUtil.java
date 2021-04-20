package casia.isiteam.militaryqa.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class ProcessUtil {

    /**
     * 将<时间>处理成标准形式
     * @param times 问句中的时间项
     * @return 标准形式列表
     */
    public static List<String> processTime(List<String> times) {
        // 处理成 YYYY-mm-dd 的标准形式
        return times.stream().map(ProcessUtil::processExplicitTime).collect(Collectors.toList());
    }

    public static String processExplicitTime(String item) {
        Matcher mYear = Pattern.compile("(\\d{3,4})年").matcher(item);
        Matcher mMonth = Pattern.compile("(\\d{1,2})月").matcher(item);
        Matcher mDay = Pattern.compile("(\\d{1,2})日").matcher(item);
        String year = "", month = "", day = "", date = "";
        if (mYear.find()) {
            year = mYear.group(1);
            if (mMonth.find()) {
                month = mMonth.group(1);
            }
            if (mDay.find()) {
                day = mDay.group(1);
            }
            date = year + "-" + dateCompletion(month) + "-" + dateCompletion(day);
        }
        return date;
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
        if (StringUtils.isEmpty(date)) {
            return "01";
        }
        try {
            if (Integer.parseInt(date) < 10 && date.length() < 2) {
                return "0" + date;
            }
        } catch (Exception e) {
            log.error("日期补全 date 类型不为整型！");
            return "01";
        }
        return date;
    }
}
