package casia.isiteam.militaryqa.utils;

import casia.isiteam.militaryqa.mapper.ResultMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class EntityAliasExtractor {

    @Autowired
    ResultMapper resultMapper;

    static Pattern pattern;
    static Matcher matcher;

    static Set<String> conceptsStopWords = new HashSet<>();

    static {
        // 加载 concepts 列表
        String[] paths = {
                "data/dict_for_segment/attribute.txt",
                "data/dict_for_segment/big_category.txt",
                "data/dict_for_segment/small_category.txt",
                "data/dict_for_segment/compare.txt",
                "data/dict_for_segment/country.txt",
                "data/dict_for_segment/most.txt"};
        for (String pa : paths) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(pa));
                String word;
                while ((word = br.readLine()) != null) {
                    conceptsStopWords.add(word);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 实体别名提取方法
     *  1. 先获取实体名在处理后的不同格式
     *  2. 对不同格式的实体名利用正则进行别名抽取
     * @param entity_id 实体id
     * @param entity_name 实体名
     * @return 实体别名列表
     */
    public Set<String> getEntityAlias(int entity_id, String entity_name) {

        // 实体名的不同格式
        Set<String> origin_words = new HashSet<>();
        // 实体最终的所有别名
        Set<String> aliases = new HashSet<>();

        // 无论如何，先添加一个跟问句预处理相同处理的别名
        aliases.add(ChineseNumberUtil.convertString(entity_name.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "")).toUpperCase());

        /* 一、添加预处理后的原生词条不同格式 - origin_words*/
        origin_words.add(entity_name);
        // 将汉字处理为对应数字
        origin_words.add(ChineseNumberUtil.convertString(entity_name));
        // 去掉 -
        origin_words.add(entity_name.replace("-", "").replace("－", ""));
        origin_words.add(ChineseNumberUtil.convertString(entity_name.replace("-", "").replace("－", "")));
        // 去掉 空格
        origin_words.add(entity_name.replace(" ", ""));
        origin_words.add(ChineseNumberUtil.convertString(entity_name.replace(" ", "")));
        // 去掉除了中文、数字和英文的所有符号
//        origin_words.add(ChineseNumberUtil.convertString(entity_name.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "")));

        /* 二、添加原生词条处理后的不同词条部分 - part_words */
        // 1、原生词条直接添加
        Set<String> part_words = new HashSet<>(origin_words);

        // 2、提取原生词条中的不同内容
        // 2.1 提取 括号 和 引号 内的内容
        String[] patterns = {
                "(.*)[(（](.*)[)）](.*)",
                "(.*)[“”\"](.*)[“”\"](.*)"
        };

        for (String word : origin_words) {
            for (String p : patterns) {
                pattern = Pattern.compile(p);
                matcher = pattern.matcher(word);
                if (matcher.find()) {
                    aliases.add(ChineseNumberUtil.convertString(matcher.group(2).replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "")).toUpperCase()); // 引号或括号内的直接添加到最终别名
                    part_words.add(matcher.group(1));
                    part_words.add(matcher.group(2));
                    part_words.add(matcher.group(3));
                    part_words.add(matcher.group(1) + matcher.group(3));
                }
            }
        }

        // 2.2 过滤结尾停用词 - part_words （结尾停用词还未标注完成，目前不需要过滤结尾停用词效果也可以）
//        for (String word : part_words) {
//            for (String entityStopWord : entityStopWords) {
//                if (word.endsWith(entityStopWord))
//                    word = word.replace(entityStopWord, "");
//            }
//        }

        // 2.3 以 / 划分 - part_words
        Set<String> set = new HashSet<>();
        for (String word : part_words) {
            set.addAll(Arrays.asList(word.split("/")));
            set.addAll(Arrays.asList(word.split(" ")));
        }
        part_words.addAll(set);

        /* 三、正则提取，保留别名字段 */
        patterns = new String[]{
                "[a-zA-Z\\d\\s.·]+-?[a-zA-Z\\d.·]+(型|式|级|号|系列|)",
                "^\\w+-?[a-zA-Z\\d.·]+(型|式|级|号|系列|)",
                "^\\w+-?[a-zA-Z\\d.·]+\\w*(型|式|级|号|系列)",
                "^[a-zA-Z.·]+(型|式|级|号|系列|)",
                "[a-zA-Z.·]+-?[a-zA-Z0-9.·]+",
                "^[\\u4e00-\\u9fa5]+",
                "^[\\u4e00-\\u9fa5a-zA-Z\\d\\s.·]+-?[a-zA-Z\\d.·]+",
                "^[\\u4e00-\\u9fa5a-zA-Z]+-?[\\d]+",
                "[a-zA-Z]+[0-9]*"
        };

        for (String word : part_words) {
            for (String p : patterns) {
                pattern = Pattern.compile(p);
                matcher = pattern.matcher(word);
                if (matcher.find()) {
                    aliases.add(ChineseNumberUtil.convertString(matcher.group().replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "")).toUpperCase());
                }
            }
        }

        return aliases;
    }

    /**
     * 实体别名入库主方法
     * 对不符合入库条件的别名做过滤
     * @param entity_id 实体id
     * @param entity_aliases 实体别名列表
     */
    public void saveEntityAlias(int entity_id, String entity_name, Set<String> entity_aliases) {
        // 过滤 概念名词
        List<String> tempList = new ArrayList<>();
        entity_aliases.forEach(alias -> { if (conceptsStopWords.contains(alias)) tempList.add(alias); });
        tempList.forEach(entity_aliases::remove);

        for (String alias : entity_aliases) {
            // 最终过滤
            if (alias.equals("MM") || alias.length() < 2 || alias.matches("^[\\d.-]+$") || alias.matches("^[IV型号级]+$")) {
                continue;
            }
            resultMapper.insertEntitySameas(alias, entity_id);
        }
    }

    public static void main(String[] args) {
        EntityAliasExtractor extractor = new EntityAliasExtractor();
//        extractor.addEntityAliasToDB();
//        Set<String> aliases = extractor.getEntityAlias(1, "鹰击82-潜射反舰导弹(YJ-82/C-802)");
//        extractor.saveEntityAlias(1, aliases);
        extractor.getEntityAlias(1, "麦克唐纳F-15E“打击鹰” 双座双发打击战斗机");
//        extractor.getEntityAlias(1, "麦道DC-8“SuperSixty” 4发涡轮风扇远程客机");
//        extractor.getEntityAlias(1, "韦伯利0.455英寸MarkIV转轮手枪");
//        extractor.getEntityAlias(1, "BAe146/Avro RJ4发涡扇短程支线飞机");
    }
}
