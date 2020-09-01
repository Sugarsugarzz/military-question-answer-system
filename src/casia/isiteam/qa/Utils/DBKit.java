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

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class DBKit {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static AnswerMapper answerMapper;
    static ResultMapper resultMapper;
    static SqlSession sqlSession;

    static {
        // 加载 MyBatis配置文件
        InputStream inputStream = DBKit.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);

        sqlSession = sqlSessionFactory.openSession();
        answerMapper = sqlSession.getMapper(AnswerMapper.class);
        resultMapper = sqlSession.getMapper(ResultMapper.class);
    }

    /**
     * 获取 match_dict 表中的信息
     * @return DictMatcher
     */
    public static List<DictMatcher> getMatchDict() {

        return answerMapper.getMatchDict();
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
     * 单大类别名
     * @param category 一级类别名
     * @return 二级类别名列表
     */
    public static List<Answer> searchByBigCategory(String category) {

        String children = answerMapper.findChildrenByBigCategory(category);

        return answerMapper.findSmallCategoryByChildren(Arrays.asList(children.split(",")));
    }

    /**
     * 单小类别名
     * @param category 二级类别名
     * @return 类别下所有实体列表
     */
    public static List<Answer> searchBySmallCategory(String category) {

        return answerMapper.findBySmallCategory(category);
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

        return answerMapper.findMultiAttrInSingleRangeByUnit(category, attr_1, operator_1, item_1, attr_2, operator_2, item_2);
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

        return answerMapper.findMultiAttrInSingleRangeByTimeAndUnit(category, attr_1, operator_1, item_1, attr_2, operator_2, item_2);
    }

    /*
    ================================================= Utils =================================================
     */

    /**
     * 获取 Concepts 表
     */
    public static List<Result> getConcepts() {
        return resultMapper.getConcepts();
    }

    /**
     * 清空 concept_sameas 表
     */
    public static void emptyConceptSameas() {
        resultMapper.emptyConceptSameas();
        sqlSession.commit();
    }

    /**
     * 添加 concept 别名
     * @param alias 别名
     * @param sameasId 关联的id
     */
    public static void insertConceptSameas(String alias, Integer sameasId) {
        resultMapper.insertConceptSameas(alias, sameasId);
        sqlSession.commit();
    }

    /**
     * 获取 Entities 表
     */
    public static List<Result> getEntities() {
        return resultMapper.getEntities();
    }

    /**
     * 清空 entity_sameas 表
     */
    public static void emptyEntitySameas() {
        resultMapper.emptyEntitySameas();
        sqlSession.commit();
    }

    /**
     * 添加 entity 别名
     * @param alias 别名
     * @param entity_id 对应实体id
     */
    public static void insertEntitySameas(String alias, int entity_id) {
        resultMapper.insertEntitySameas(alias, entity_id);
        sqlSession.commit();
    }

    /**
     * 根据标签清理 match_dict 表
     * @param labels 标签
     */
    public static void emptyMatchDictByLabel(List<String> labels) {
        resultMapper.emptyMatchDictByLabel(labels);
        sqlSession.commit();
    }

    /**
     * 存入 match_dict 表
     * @param word 原词
     * @param alias 别名
     * @param label 标签
     */
    public static void insertMatchDict(String word, String alias, String label) {
        resultMapper.insertMatchDict(word, alias, label);
        sqlSession.commit();
    }

    /**
     * 根据 level 获取 concepts
     * @param levels comcept level
     */
    public static List<Result> getConceptsByLevel(List<Integer> levels) {
        return resultMapper.getConceptsByLevel(levels);
    }

    /**
     * 获取 concepts 别名
     */
    public static List<Result> getConceptsSameas() {
        return resultMapper.getConceptsSameas();
    }

    /**
     * 获取 entities 别名
     */
    public static List<Result> getEntitiesSameas() {
        return resultMapper.getEntitiesSameas();
    }

    /**
     * 获取 match_dict 的 alias 和 label
     */
    public static List<Result> getSimpleMatchDict() {
        return resultMapper.getSimpleMatchDict();
    }
}
