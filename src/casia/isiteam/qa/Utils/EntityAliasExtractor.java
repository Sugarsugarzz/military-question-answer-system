package casia.isiteam.qa.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityAliasExtractor {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static Connection conn;
    static PreparedStatement statement;
    static Pattern pattern;
    static Matcher matcher;

    static Set<String> entityStopWords = new HashSet<>();
    static Set<String> conceptsStopWords = new HashSet<>();

    static {
        // 1. 数据库
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.10.231:3307/military_qa?characterEncoding=UTF-8", "bj", "bj2016");
            String sql = "INSERT INTO entity_sameas(entity_name, sameAs_id) VALUES (?, ?)";
            statement = conn.prepareStatement(sql);
        } catch (Exception e) {
            logger.error("数据库连接发生错误： ", e);
        }

        // 2. 加载 entity 停用词
//        String path = "data/dict_for_basic/entity_stopwords.txt";
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(path));
//            String word;
//            while ((word = br.readLine()) != null) {
//                entityStopWords.add(word);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 3. 加载 concepts 列表
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
     * 从数据库读取数据
     * @param sql 查询sql
     * @return 查询结果
     */
    private static ResultSet getResultSet(String sql) {
        ResultSet rs = null;
        try {
            Statement s = conn.createStatement();
            rs = s.executeQuery(sql);
        } catch (Exception e) {
            logger.error("数据库读取发生错误： ", e);
        }
        return rs;
    }

    /**
     * 调用主方法
     * 读取所有实体名，调用 实体别名提取方法 getEntityAlias()
     */
    public static void addEntityAliasToDB() {

        String sql = "SELECT entity_id, entity_name FROM entities";
        ResultSet rs = getResultSet(sql);
        try {
            while (rs.next()) {
                // 根据 entity_name ，提取所有别名
                Set<String> aliases = getEntityAlias(rs.getInt("entity_id"), rs.getString("entity_name"));
                saveEntityAlias(rs.getInt("entity_id"), rs.getString("entity_name"), aliases);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public static Set<String> getEntityAlias(int entity_id, String entity_name) {

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
    public static void saveEntityAlias(int entity_id, String entity_name, Set<String> entity_aliases) {

        Set<String> aliases = new HashSet<>();

        // 过滤 概念名词
        for (String stopWord : conceptsStopWords) {
            entity_aliases.remove(stopWord);
        }

        try {
            for (String alias : entity_aliases) {

                // 最终过滤
                if (alias.length() < 2)
                    continue;
                if (alias.matches("^[\\d.-]+$"))
                    continue;
                if (alias.matches("^[IV型号级]+$"))
                    continue;

                aliases.add(alias);

                statement.setString(1, alias);
                statement.setInt(2, entity_id);
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("--------------------------------------------------");
        System.out.println(entity_name + " - " + aliases);
    }

    public static void main(String[] args) {
//        addEntityAliasToDB();
//        Set<String> aliases = getEntityAlias(1, "鹰击82-潜射反舰导弹(YJ-82/C-802)");
//        saveEntityAlias(1, aliases);
        getEntityAlias(1, "麦克唐纳F-15E“打击鹰” 双座双发打击战斗机");
//        getEntityAlias(1, "麦道DC-8“SuperSixty” 4发涡轮风扇远程客机");
//        getEntityAlias(1, "韦伯利0.455英寸MarkIV转轮手枪");
//        getEntityAlias(1, "BAe146/Avro RJ4发涡扇短程支线飞机");
    }
}
