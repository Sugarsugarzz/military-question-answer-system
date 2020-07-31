package Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbOperator {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static Connection conn;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.10.231:3307/military_qa?characterEncoding=UTF-8", "bj", "bj2016");
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
                map.put(rs.getString("entity_name"), rs.getString("entity_name"));
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
                    map.put(rs.getString("entity_name_1"), map.get(rs.getString("entity_name_1")) + "|" + rs.getString("entity_name_2"));
                }
            }
        } catch (SQLException e) {
            logger.error("读数据库出错！", e);
        }

        return map;
    }
}
