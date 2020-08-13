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

    /*
    ===================================================================================================================
      一、将自定义的 concept 和 entity 别名上传到对应 sameas 表中的工具
    ===================================================================================================================
     */
    public static void addConceptAliasToDB() {

        // 获取 concepts 表中 concept_id 和 concept_name 键值关系
        String sql = "SELECT c_id, concept_name FROM concepts";
        ResultSet rs = getResultSet(sql);
        Map<String, Integer> map = new HashMap<>();
        try {
            while (rs.next()) {
                System.out.println(rs.getString("concept_name") + " - " + rs.getInt("c_id"));
                map.put(rs.getString("concept_name"), rs.getInt("c_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 清空 concept_sameas 表
        sql = "DELETE FROM concept_sameas";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 读取 data/dict_for_sameas 下的自定义别名库，上传到数据库对应 sameas 表中
        String filepath = "data/dict_for_sameas/concepts_alias.txt";
        sql = "INSERT INTO concept_sameas(concept_name, sameAs_id) VALUES ('%s', %s)";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = br.readLine()) != null) {
                String key = str.split("：")[0];
                String[] aliases = str.split("：")[1].split("\\|");
                for (String alias : aliases) {
                    statement.executeUpdate(String.format(sql, alias, map.get(key)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    ===================================================================================================================
      二、将country.txt、most.txt、compare.txt、entities(entity_sameas)表、concepts(concept_sameas)表的数据存入match_dict表中
    ===================================================================================================================
     */

    /**
     * 将 country.txt most.txt compare.txt 键值信息存入 match_dict 数据库表
     */
    public static void getCountryCompareMostToDB() {
        // 获取文件路径
        File file = new File("data/dict_for_basic");
        List<String> file_list = new ArrayList<>();
        for (String filename : file.list()) {
            if (filename.equals("country.txt") || filename.equals("most.txt") || filename.equals("compare.txt"))
            file_list.add("data/dict_for_basic/" + filename);
        }

        // 清空 compare、country、most 标签项
        String sql = "DELETE FROM match_dict WHERE label in ('compare', 'country', 'most')";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 存入数据库
        sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
        for (String filepath : file_list) {
            String[] names = filepath.split("/");
            String label = names[names.length - 1].replace(".txt", "");
            // 读键值
            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String str;
                while ((str = br.readLine()) != null) {
                    statement.executeUpdate(String.format(sql, str.split("：")[0], str.split("：")[1], label));
                    System.out.println(String.format(sql, str.split("：")[0], str.split("：")[1], label));
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
     * 将 big_category、small_category、attributes 存入match_dict 数据库表
     */
    public static void getConceptsAndSameasToDB() {

        Map<String, String[]> map = new HashMap<>();

        // 获取概念库
        String sql = "SELECT concept_name, level FROM concepts WHERE level in (0, 2, 3)";
        ResultSet rs = getResultSet(sql);
        try {
            while (rs.next()) {
                String label = null;
                String concept = rs.getString("concept_name");
                String[] tuple = new String[2];
                if (rs.getInt("level") == 0)
                    label = "attribute";
                else if (rs.getInt("level") == 2)
                    label = "big_category";
                else if (rs.getInt("level") == 3)
                    label = "small_category";
                tuple[0] = label;
                tuple[1] = concept;
                map.put(concept, tuple);
            }
        } catch (SQLException e) {
            logger.error("存储概念数据失败！", e);
        }

        // 再获取概念别名库
        sql = "SELECT c.concept_name AS concept1, cs.concept_name AS concept2 FROM concepts c, concept_sameas cs "
                + "WHERE c.c_id = cs.sameAs_id";
        rs = getResultSet(sql);
        try {
            while (rs.next()) {
                String concept1 = rs.getString("concept1");
                String concept2 = rs.getString("concept2");
                String[] tuple = map.get(concept1);
                tuple[1] = map.get(concept1)[1] + "|" + concept2.replace(" ", "");
                if (!map.containsKey(concept1)) {
                    // 打印出来的是有问题的，没有对应实体
                    System.out.println(concept1);
                } else {
                    map.put(concept1, tuple);
                }
            }
        } catch (SQLException e) {
            logger.error("读数据库出错！", e);
        }

        // 清空 attribute、big_category、small_category 标签项
        sql = "DELETE FROM match_dict WHERE label in ('attribute', 'big_category', 'small_category')";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 存入 match_dict 表
        sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
        for (String key : map.keySet()) {
            try {
                statement.executeUpdate(String.format(sql, key, map.get(key)[1], map.get(key)[0]));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将 entity及别名 存入 match_dict 数据库表
     */
    public static void getEntitiesAndSameasToDB() {

        Map<String, String> map = new HashMap<>();

        // 先获取实体库、再获取实体别名库
        String sql = "SELECT e.entity_name AS entity_name_1, s.entity_name AS entity_name_2 FROM entities e, entity_sameas s "
                + "WHERE e.entity_id = s.sameAs_id ORDER BY e.entity_id";
        ResultSet rs = getResultSet(sql);
        try {
            while (rs.next()) {
                if (!map.containsKey(rs.getString("entity_name_1"))) {
                    map.put(rs.getString("entity_name_1"), rs.getString("entity_name_1").replace(" ", ""));
                }
                map.put(rs.getString("entity_name_1"), map.get(rs.getString("entity_name_1")) + "|" + rs.getString("entity_name_2").replace(" ", ""));
            }
        } catch (SQLException e) {
            logger.error("读数据库出错！", e);
        }

        // 清空 attribute、big_category、small_category 标签项
        sql = "DELETE FROM match_dict WHERE label in ('entity')";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 存入 match_dict 表
        sql = "INSERT INTO match_dict(word, alias, label) VALUES ('%s', '%s', '%s')";
        for (String key : map.keySet()) {
            try {
                System.out.println(key + " - " + map.get(key));
                statement.executeUpdate(String.format(sql, key, map.get(key), "entity"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    ===================================================================================================================
      三、将 match_dict 表中的数据获取到本地，加载到HanLP分词器的自定义词典中（未生效需先删除custom/CustomDictionary.txt.bin）
    ===================================================================================================================
     */
    /**
     * 根据数据库的 match_dict 表，获取分词词典到本地，到 data/dict_for_segment 目录下
     */
    public static void getDBToSegmentDict() {

        // 获取match_dict表中键值信息
        String sql = "SELECT alias, label FROM match_dict";
        ResultSet rs = getResultSet(sql);
        Map<String, Set<String>> map = new HashMap<>();
        try {
            while (rs.next()) {
                String alias = rs.getString("alias");
                String label = rs.getString("label");
                if (!map.containsKey(label)) {
                    map.put(label, new HashSet<>());
                }
                for (String s : alias.split("\\|"))
                    map.get(label).add(s);
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
