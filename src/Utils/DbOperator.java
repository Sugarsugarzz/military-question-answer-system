package Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbOperator {

    private Connection conn;

    /**
     * 无参构造函数
     * 初始化数据库连接
     */
    public DbOperator() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.10.231:3307/military_qa?characterEncoding=UTF-8", "bj", "bj2016");
        } catch (Exception e) {
            System.out.println("数据库连接发生错误");
            e.printStackTrace();
        }
    }

    /**
     * 从数据库读取数据
     * @param sql
     * @return
     */
    public ResultSet getResultSet(String sql) {
        ResultSet rs = null;
        try {
            Statement s = conn.createStatement();
            rs = s.executeQuery(sql);
        } catch (Exception e) {
            System.out.println("数据库读取发生错误");
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 获取 Entities 表中的武器实体
     * @return
     */
    public List<String> getWeapons() {
        String sql = "SELECT entity_name FROM entities";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("entity_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取 Concepts 表中的一级分类
     * @return
     */
    public List<String> getBigCategory() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 2";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("concept_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取 Concepts 表中的二级分类
     * @return
     */
    public List<String> getSmallCategory() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 3";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("concept_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取 Concepts 表中的属性
     * @return
     */
    public List<String> getAttributes() {
        String sql = "SELECT concept_name FROM concepts WHERE level = 0";
        ResultSet rs = getResultSet(sql);

        List<String> res = new ArrayList<>();
        try {
            while (rs.next()) {
                res.add(rs.getString("concept_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
