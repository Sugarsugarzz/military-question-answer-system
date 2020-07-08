package Searcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbSearcher {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private Connection conn;

    /**
     * 无参构造函数
     * 初始化数据库连接
     */
    public DbSearcher() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.10.231:3307/military_qa?characterEncoding=UTF-8", "bj", "bj2016");
        } catch (Exception e) {
            logger.error("数据库连接发生错误： ", e);
        }
    }

    /**
     * 从数据库读取数据
     * @param sql 查询问句
     * @return 查询结果
     */
    private ResultSet getResultSet(String sql) {
        ResultSet rs = null;
        try {
            Statement s = conn.createStatement();
            rs = s.executeQuery(sql);
        } catch (Exception e) {
            logger.error("数据库读取发生错误： ", e);
            e.printStackTrace();
        }
        return rs;
    }

    public List<Map<String, String>> search(String entity, List<String> attrs) {

        // 存结果
        List<Map<String, String>> res = new ArrayList<>();

        String base_sql = "SELECT attr_value FROM entity_attr " +
                          "WHERE entity_id = (SELECT entity_id FROM entities WHERE entity_name = '"+ entity +"')";
        for (String attr: attrs) {
            String sql = base_sql + " and attr_id = (SELECT c_id FROM concepts WHERE concept_name = '" + attr + "') ";
            ResultSet rs = getResultSet(sql);
            try {
                rs.first();
                Map<String, String> map = new HashMap<>();
                map.put(attr, rs.getString("attr_value"));
                res.add(map);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return res;
    }
}
