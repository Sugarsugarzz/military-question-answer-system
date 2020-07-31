package Searcher;

import Mapper.AnswerMapper;
import Model.Answer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.List;

public class DbSearcher {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static AnswerMapper answerMapper;
    static SqlSession sqlSession;

    static {
        // 加载 MyBatis配置文件
        InputStream inputStream = DbSearcher.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);

        sqlSession = sqlSessionFactory.openSession();
        answerMapper = sqlSession.getMapper(AnswerMapper.class);
    }

    /**
     * 单轮问答信息存入数据库
     * @param uid 用户id
     * @param q_time 问答时间
     * @param question 问句
     * @param answer 答案JSON
     */
    public static void insertQAInfo(String uid, String q_time, String question, String answer) {

        answerMapper.saveQAInfo(uid, q_time, question, answer);
        sqlSession.commit();
    }

    /**
     * 国家及实体类别
     * @param country 国家名
     * @param category 实体类别名
     * @return 答案
     */
    public static List<Answer> searchByCountryAndCategory(String country, String category) {

        return answerMapper.findByCountryAndCategory(country, category);
    }

    /**
     * 单类别名
     * @param category 实体类别名
     * @return 答案
     */
    public static List<Answer> searchByCategory(String category) {

        return answerMapper.findByCategory(category);
    }

    /**
     * 单实体
     * @param entity 实体名
     * @return 答案
     */
    public static List<Answer> searchByEntity(String entity) {

        return answerMapper.findByEntity(entity);
    }

    /**
     * 单实体单属性/多属性模板查询
     * @param entity 实体名
     * @param attrs 属性列表
     * @return 答案
     */
    public static List<Answer> searchByEntityAndAttrs(String entity, List<String> attrs) {

        return answerMapper.findByEntityAndAttrs(entity, attrs);
    }

    /**
     * 全类别属性最大值
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMaxInAllCategory(String attr) {

        return answerMapper.findMaxByAttrInAllCategory(attr);
    }

    /**
     * 全类别属性最小值
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMinInAllCategory(String attr) {

        return answerMapper.findMinByAttrInAllCategory(attr);
    }

    /**
     * 单类别属性最大值
     * @param category 类别名
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMaxInSingleCategory(String category, String attr) {

        return answerMapper.findMaxByAttrInSingleCategory(category, attr);
    }

    /**
     * 单类别属性最小值
     * @param category 类别名
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMinInSingleCategory(String category, String attr) {

        return answerMapper.findMinByAttrInSingleCategory(category, attr);
    }

    /**
     * 单属性单类别单区间（数值型）
     * @param category 类别名
     * @param attr 属性名
     * @param operator 比较符
     * @param item 比较数值
     * @return 答案
     */
    public static List<Answer> searchInSingleRangeByUnit(String category, String attr, String operator, String item) {

        return answerMapper.findInSingleRangeByUnit(category, attr, operator, item);
    }

    /**
     * 单属性单类别单区间（时间型）
     * @param category 类别名
     * @param attr 属性名
     * @param operator 比较符
     * @param item 比较数值
     * @return 答案
     */
    public static List<Answer> searchInSingleRangeByTime(String category, String attr, String operator, String item) {

        return answerMapper.findInSingleRangeByTime(category, attr, operator, item);
    }

    /**
     * 单属性单类别单区间（数值型）
     * @param category 类别名
     * @param attr 属性名
     * @param operator_1 比较符_1
     * @param item_1 比较数值_1
     * @param operator_2 比较符_2
     * @param item_2 比较数值_2
     * @return 答案
     */
    public static List<Answer> searchInMultiRangeByUnit(String category, String attr, String operator_1, String item_1, String operator_2, String item_2) {

        return answerMapper.findInMultiRangeByUnit(category, attr, operator_1, item_1, operator_2, item_2);
    }

    /**
     * 单属性单类别单区间（时间型）
     * @param category 类别名
     * @param attr 属性名
     * @param operator_1 比较符_1
     * @param item_1 比较数值_1
     * @param operator_2 比较符_2
     * @param item_2 比较数值_2
     * @return 答案
     */
    public static List<Answer> searchInMultiRangeByTime(String category, String attr, String operator_1, String item_1, String operator_2, String item_2) {

        return answerMapper.findInMultiRangeByTime(category, attr, operator_1, item_1, operator_2, item_2);
    }
}
