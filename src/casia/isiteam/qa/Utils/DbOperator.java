package casia.isiteam.qa.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DbOperator {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static Connection conn;
    private static Statement statement;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.10.231:3307/military_qa?characterEncoding=UTF-8", "bj", "bj2016");
            statement = conn.createStatement();
        } catch (Exception e) {
            logger.error("数据库连接发生错误： ", e);
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
     * 获取 Entities 表中的实体
     * @return 实体列表
     */
    public static List<String> getEntities() {
        String sql = "SELECT entity_name FROM entities";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("entity_name"));
            }
        } catch (SQLException e) {
            logger.error("获取实体列表失败！", e);
        }
        return res;
    }

    /**
     * 获取 Concepts 表中的一级分类
     * @return 一级分类列表
     */
    public static List<String> getBigCategory() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 2";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("concept_name"));
            }
        } catch (SQLException e) {
            logger.error("获取一级分类失败！", e);
        }
        return res;
    }

    /**
     * 获取 Concepts 表中的二级分类
     * @return 二级分类列表
     */
    public static List<String> getSmallCategory() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 3";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("concept_name"));
            }
        } catch (SQLException e) {
            logger.error("获取二级分类失败！", e);
        }
        return res;
    }

    /**
     * 获取 Concepts 表中的属性
     * @return 属性列表
     */
    public static List<String> getAttributes() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 0";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("concept_name"));
            }
        } catch (SQLException e) {
            logger.error("获取属性失败！", e);
        }
        return res;
    }

    /**
     * 根据实体库及实体别名库，生成匹配列表（实体：实体别名列表）
     * @return 实体别名：实体 列表
     */
    public static Map<String, String> getEntitiesAndSameas() {

        Map<String, String> map = new HashMap<>();

        // 先获取实体库
        String sql = "SELECT entity_name FROM entities";
        ResultSet rs = getResultSet(sql);
        try {
            while (rs.next()) {
                map.put(rs.getString("entity_name"), rs.getString("entity_name").replace(" ", ""));
            }
        } catch (SQLException e) {
            logger.error("读数据库出错！", e);
        }

        // 再获取实体别名库
        sql = "SELECT e.entity_name AS entity_name_1, s.entity_name AS entity_name_2 FROM entities e, entity_sameas s "
                    + "WHERE e.entity_id = s.sameAs_id ORDER BY e.entity_id";
        rs = getResultSet(sql);
        try {
            while (rs.next()) {
                if (!map.containsKey(rs.getString("entity_name_1"))) {
                    // 打印出来额是有问题的，没有对应实体
                    System.out.println(rs.getString("entity_name_1"));
                } else {
                    map.put(rs.getString("entity_name_1"), map.get(rs.getString("entity_name_1")) + "|" + rs.getString("entity_name_2").replace(" ", ""));
                }
            }
        } catch (SQLException e) {
            logger.error("读数据库出错！", e);
        }

        return map;
    }

    /*
    ===================================================================================================================
                              上面的方法基本弃用，本地match txt已存入数据库，从数据库读取。
    ===================================================================================================================
     */

    /**
     * 将 country.txt most.txt compare.txt 键值信息存入 match_dict 数据库表
     */
    public static void getMatchFileToDB() {
        // 获取文件路径
        File file = new File("data/dict_for_match_query");
        List<String> file_list = new ArrayList<>();
        for (String filename : file.list()) {
            if (filename.equals("country.txt") || filename.equals("most.txt") || filename.equals("compare.txt"))
            file_list.add("data/dict_for_match_query/" + filename);
        }

        // 存入数据库
        for (String filepath : file_list) {
            String[] names = filepath.split("/");
            String name = names[names.length - 1].replace(".txt", "");
            String sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
            // 读键值
            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String str;
                while ((str = br.readLine()) != null) {
                    System.out.println(String.format(sql, str.split("：")[0], str.split("：")[1], name));
                    statement.executeUpdate(String.format(sql, str.split("：")[0], str.split("：")[1], name));
                }
            } catch (FileNotFoundException e) {
                logger.error(filepath + " - 写文件未找到！", e);
            } catch (IOException e) {
                logger.error(filepath + " - 写文件发生错误！", e);
            } catch (SQLException e) {
                logger.error(filepath + " - 写数据库发生错误！", e);
            }
        }
    }

    /**
     * 将 big_category 存入 match_dict 数据库表
     */
    public static void getBigCategoryToDB() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 2";
        ResultSet rs = getResultSet(sql);
        sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
        try {
            while (rs.next()) {
                statement.executeUpdate(String.format(sql, rs.getString("concept_name"), rs.getString("concept_name"), "big_category"));
            }
        } catch (SQLException e) {
            logger.error("存储一级分类失败！", e);
        }
    }

    /**
     * 将 small_category 存入 match_dict 数据库表
     */
    public static void getSmallCategoryToDB() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 3";
        ResultSet rs = getResultSet(sql);
        sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
        try {
            while (rs.next()) {
                statement.executeUpdate(String.format(sql, rs.getString("concept_name"), rs.getString("concept_name"), "small_category"));
            }
        } catch (SQLException e) {
            logger.error("存储二级分类失败！", e);
        }
    }

    /**
     * 将 attrbutes 存入 match_dict 数据库表
     */
    public static void getAttributesToDB() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 0";
        ResultSet rs = getResultSet(sql);
        sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
        try {
            while (rs.next()) {
                statement.executeUpdate(String.format(sql, rs.getString("concept_name"), rs.getString("concept_name"), "attribute"));
            }
        } catch (SQLException e) {
            logger.error("存储属性失败！", e);
        }
    }

    /**
     * 将 entity及别名 存入 match_dict 数据库表
     */
    public static void getEntitiesAndSameasToDB() {

        Map<String, String> map = new HashMap<>();

        // 先获取实体库
        String sql = "SELECT entity_name FROM entities";
        ResultSet rs = getResultSet(sql);
        try {
            while (rs.next()) {
                map.put(rs.getString("entity_name"), rs.getString("entity_name").replace(" ", ""));
            }
        } catch (SQLException e) {
            logger.error("读数据库出错！", e);
        }

        // 再获取实体别名库
        sql = "SELECT e.entity_name AS entity_name_1, s.entity_name AS entity_name_2 FROM entities e, entity_sameas s "
                + "WHERE e.entity_id = s.sameAs_id ORDER BY e.entity_id";
        rs = getResultSet(sql);
        try {
            while (rs.next()) {
                if (!map.containsKey(rs.getString("entity_name_1"))) {
                    // 打印出来额是有问题的，没有对应实体
                    System.out.println(rs.getString("entity_name_1"));
                } else {
                    map.put(rs.getString("entity_name_1"), map.get(rs.getString("entity_name_1")) + "|" + rs.getString("entity_name_2").replace(" ", ""));
                }
            }
        } catch (SQLException e) {
            logger.error("读数据库出错！", e);
        }

        sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
        for (String key : map.keySet()) {
            try {
                statement.executeUpdate(String.format(sql, key, map.get(key), "entity"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据数据库的 match_dict 表，获取分词词典到本地
     */
    public static void getDBToSegmentDict() {

        // 获取match_dict表中键值信息
        String sql = "SELECT * FROM match_dict";
        ResultSet rs = getResultSet(sql);
        Map<String, List<String>> map = new HashMap<>();
        try {
            while (rs.next()) {
                String alias = rs.getString("alias");
                String label = rs.getString("label");
                if (map.containsKey(label)) {
                    for (String s : alias.split("\\|"))
                        map.get(label).add(s);
                } else {
                    map.put(label, new ArrayList<>());
                    for (String s : alias.split("\\|"))
                        map.get(label).add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 存入本地txt
        for (String key : map.keySet()) {
            String filepath = "data/dict_for_segment/" + key + ".txt";
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
                for (String s : map.get(key)) {
                    bw.write(s + "\n");
                }
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
