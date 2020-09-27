package casia.isiteam.qa.Utils;

import casia.isiteam.qa.Mapper.AnswerMapper;
import casia.isiteam.qa.Mapper.ResultMapper;
import casia.isiteam.qa.Model.Answer;
import casia.isiteam.qa.Model.DictMatcher;
import casia.isiteam.qa.Model.Result;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class DBKit {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

//    static AnswerMapper answerMapper;
//    static ResultMapper resultMapper;
//    static SqlSession sqlSession;

    private static SqlSessionFactory sqlSessionFactory = null;

    static {
        // 加载 MyBatis配置文件
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(System.getProperty("user.dir") + "/qa_config/mybatis-config.xml"));
        } catch (Exception e) {
            inputStream = DBKit.class.getClassLoader().getResourceAsStream("qa_config/mybatis-config.xml");
            if (inputStream == null) {
                inputStream = DBKit.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
            }
        }

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
//        answerMapper = sqlSession.getMapper(AnswerMapper.class);
//        resultMapper = sqlSession.getMapper(ResultMapper.class);
    }

    private static <T> T getMapper(Class<T> clazz) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.getMapper(clazz);
    }

    /**
     * 获取 match_dict 表中的信息
     * @return DictMatcher
     */
    public static List<DictMatcher> getMatchDict() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).getMatchDict();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 单轮问答信息存入数据库
     * @param uid 用户id
     * @param q_time 问答时间
     * @param question 问句
     * @param answer 答案JSON
     */
    public static void insertQAInfo(String uid, String q_time, String question, String answer) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getMapper(AnswerMapper.class).saveQAInfo(uid, q_time, question, answer);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 国家及实体类别
     * @param country 国家名
     * @param category 实体类别名
     * @return 答案
     */
    public static List<Answer> searchByCountryAndCategory(String country, String category) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findByCountryAndCategory(country, category);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 单大类别名
     * @param category 一级类别名
     * @return 二级类别名列表
     */
    public static List<Answer> searchByBigCategory(String category) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            String children = sqlSession.getMapper(AnswerMapper.class).findChildrenByBigCategory(category);
            return sqlSession.getMapper(AnswerMapper.class).findSmallCategoryByChildren(Arrays.asList(children.split(",")));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 单小类别名
     * @param category 二级类别名
     * @return 类别下所有实体列表
     */
    public static List<Answer> searchBySmallCategory(String category) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findBySmallCategory(category);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 单实体
     * @param entity 实体名
     * @return 答案
     */
    public static List<Answer> searchByEntity(String entity) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findByEntity(entity);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 单实体单属性/多属性模板查询
     * @param entity 实体名
     * @param attrs 属性列表
     * @return 答案
     */
    public static List<Answer> searchByEntityAndAttrs(String entity, List<String> attrs) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findByEntityAndAttrs(entity, attrs);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 全类别属性最大值
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMaxInAllCategory(String attr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findMaxByAttrInAllCategory(attr);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 全类别属性最小值
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMinInAllCategory(String attr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findMinByAttrInAllCategory(attr);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 单类别属性最大值
     * @param category 类别名
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMaxInSingleCategory(String category, String attr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findMaxByAttrInSingleCategory(category, attr);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 单类别属性最小值
     * @param category 类别名
     * @param attr 属性名
     * @return 答案
     */
    public static List<Answer> searchMinInSingleCategory(String category, String attr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findMinByAttrInSingleCategory(category, attr);
        } finally {
            sqlSession.close();
        }
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
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findInSingleRangeByUnit(category, attr, operator, item);
        } finally {
            sqlSession.close();
        }
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
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findInSingleRangeByTime(category, attr, operator, item);
        } finally {
            sqlSession.close();
        }
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
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findInMultiRangeByUnit(category, attr, operator_1, item_1, operator_2, item_2);
        } finally {
            sqlSession.close();
        }
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
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findInMultiRangeByTime(category, attr, operator_1, item_1, operator_2, item_2);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 多属性单类别单区间（数值型）
     * @param category 类别名
     * @param attr_1 属性名_1
     * @param operator_1 比较符_1
     * @param item_1 比较数值_1
     * @param attr_2 属性名_2
     * @param operator_2 比较符_2
     * @param item_2 比较数值_2
     * @return 答案
     */
    public static List<Answer> searchMultiAttrInSingleRangeByUnit(String category, String attr_1, String operator_1, String item_1, String attr_2, String operator_2, String item_2) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findMultiAttrInSingleRangeByUnit(category, attr_1, operator_1, item_1, attr_2, operator_2, item_2);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 多属性单类别单区间（时间数值混合型）
     * @param category 类别名
     * @param attr_1 属性名_1
     * @param operator_1 比较符_1
     * @param item_1 比较数值_1
     * @param attr_2 属性名_2
     * @param operator_2 比较符_2
     * @param item_2 比较数值_2
     * @return 答案
     */
    public static List<Answer> searchMultiAttrInSingleRangeByTimeAndUnit(String category, String attr_1, String operator_1, String item_1, String attr_2, String operator_2, String item_2) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(AnswerMapper.class).findMultiAttrInSingleRangeByTimeAndUnit(category, attr_1, operator_1, item_1, attr_2, operator_2, item_2);
        } finally {
            sqlSession.close();
        }
    }

    /*
    ================================================= Utils =================================================
     */

    /**
     * 获取 Concepts 表
     */
    public static List<Result> getConcepts() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(ResultMapper.class).getConcepts();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 清空 concept_sameas 表
     */
    public static void emptyConceptSameas() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getMapper(ResultMapper.class).emptyConceptSameas();
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 添加 concept 别名
     * @param alias 别名
     * @param sameasId 关联的id
     */
    public static void insertConceptSameas(String alias, Integer sameasId) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getMapper(ResultMapper.class).insertConceptSameas(alias, sameasId);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 获取 Entities 表
     */
    public static List<Result> getEntities() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(ResultMapper.class).getEntities();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 清空 entity_sameas 表
     */
    public static void emptyEntitySameas() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getMapper(ResultMapper.class).emptyEntitySameas();
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 添加 entity 别名
     * @param alias 别名
     * @param entity_id 对应实体id
     */
    public static void insertEntitySameas(String alias, int entity_id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getMapper(ResultMapper.class).insertEntitySameas(alias, entity_id);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据标签清理 match_dict 表
     * @param labels 标签
     */
    public static void emptyMatchDictByLabel(List<String> labels) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getMapper(ResultMapper.class).emptyMatchDictByLabel(labels);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 存入 match_dict 表
     * @param word 原词
     * @param alias 别名
     * @param label 标签
     */
    public static void insertMatchDict(String word, String alias, String label) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getMapper(ResultMapper.class).insertMatchDict(word, alias, label);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据 level 获取 concepts
     * @param levels comcept level
     */
    public static List<Result> getConceptsByLevel(List<Integer> levels) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(ResultMapper.class).getConceptsByLevel(levels);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 获取 concepts 别名
     */
    public static List<Result> getConceptsSameas() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(ResultMapper.class).getConceptsSameas();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 获取 entities 别名
     */
    public static List<Result> getEntitiesSameas() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(ResultMapper.class).getEntitiesSameas();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 获取 match_dict 的 alias 和 label
     */
    public static List<Result> getSimpleMatchDict() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.getMapper(ResultMapper.class).getSimpleMatchDict();
        } finally {
            sqlSession.close();
        }
    }
}
