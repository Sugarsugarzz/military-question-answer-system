package Searcher;

import Mapper.AnswerMapper;
import Model.Answer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public class DbSearcher {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static AnswerMapper answerMapper;

    static {
        // 加载 MyBatis配置文件
        InputStream inputStream = DbSearcher.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        answerMapper = sqlSession.getMapper(AnswerMapper.class);
    }

    /**
     * 国家及实体类别
     * @param country 国家名
     * @param category 实体类别名
     * @return
     */
    public static List<Answer> searchByCountryAndCategory(String country, String category) {

        return answerMapper.findByCountryAndCategory(country, category);
    }

    /**
     * 单实体
     * @param entity 实体名
     * @return
     */
    public static List<Answer> searchByEntity(String entity) {

        return answerMapper.findByEntity(entity);
    }

    /**
     * 单实体单属性/多属性模板查询
     * @param entity 实体名
     * @param attrs 属性列表
     * @return
     */
    public static List<Answer> searchByEntityAndAttrs(String entity, List<String> attrs) {

        return answerMapper.findByEntityAndAttrs(entity, attrs);
    }

    /**
     * 全类别属性最大值
     * @param attr 属性名
     * @return
     */
    public static List<Answer> searchMaxInAllCategory(String attr) {

        return answerMapper.findMaxByAttrInAllCategory(attr);
    }

    /**
     * 全类别属性最小值
     * @param attr 属性名
     * @return
     */
    public static List<Answer> searchMinInAllCategory(String attr) {

        return answerMapper.findMinByAttrInAllCategory(attr);
    }

    /**
     * 单类别属性最大值
     * @param category 类别名
     * @param attr 属性名
     * @return
     */
    public static List<Answer> searchMaxInSingleCategory(String category, String attr) {

        return answerMapper.findMaxByAttrInSingleCategory(category, attr);
    }

    /**
     * 单类别属性最小值
     * @param category 类别名
     * @param attr 属性名
     * @return
     */
    public static List<Answer> searchMinInSingleCategory(String category, String attr) {

        return answerMapper.findMinByAttrInSingleCategory(category, attr);
    }
}
