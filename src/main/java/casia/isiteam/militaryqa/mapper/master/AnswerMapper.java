package casia.isiteam.militaryqa.mapper.master;

import casia.isiteam.militaryqa.model.Answer;
import casia.isiteam.militaryqa.model.DictMatcher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AnswerMapper {

    /**
     * 获取 match_dict 表中的信息
     * @return DictMatcher
     */
    List<DictMatcher> getMatchDict();

    /**
     * 国家及实体类别
     * @param country 国家名
     * @param category 实体类别名
     * @return 答案
     */
    List<Answer> findByCountryAndCategory(@Param("country") String country,
                                          @Param("category") String category);

    /**
     * 根据一级分类查找二级分类id列表
     * @param category 一级分类
     * @return 二级分类id列表，以逗号隔开
     */
    String findChildrenByBigCategory(@Param("category") String category);

    /**
     * 二级分类名列表
     * @param children id列表，以逗号隔开
     * @return 列表
     */
    List<Answer> findSmallCategoryByChildren(@Param("children") List<String> children);

    /**
     * 单小类别名
     * @param category 二级类别名
     * @return 类别下所有实体列表
     */
    List<Answer> findBySmallCategory(@Param("category") String category);

    /**
     * 单实体
     * @param entity 实体名
     * @return 答案
     */
    List<Answer> findByEntity(@Param("entity") String entity);

    /**
     * 单实体单属性/多属性模板查询
     * @param entity 实体名
     * @param attrs 属性列表
     * @return 答案
     */
    List<Answer> findByEntityAndAttrs(@Param("entity") String entity,
                                      @Param("attrs") List<String> attrs);

    /**
     * 全类别属性最大值
     * @param attr 属性名
     * @return 答案
     */
    List<Answer> findMaxByAttrInAllCategory(@Param("attr") String attr);

    /**
     * 全类别属性最小值
     * @param attr 属性名
     * @return 答案
     */
    List<Answer> findMinByAttrInAllCategory(@Param("attr") String attr);

    /**
     * 单类别属性最大值
     * @param category 类别名
     * @param attr 属性名
     * @return 答案
     */
    List<Answer> findMaxByAttrInSingleCategory(@Param("category") String category,
                                               @Param("attr") String attr);

    /**
     * 单类别属性最小值
     * @param category 类别名
     * @param attr 属性名
     * @return 答案
     */
    List<Answer> findMinByAttrInSingleCategory(@Param("category") String category,
                                               @Param("attr") String attr);

    /**
     * 单属性单类别单区间（数值型）
     * @param category 类别名
     * @param attr 属性名
     * @param operator 比较符
     * @param unit_item 比较数值
     * @return 答案
     */
    List<Answer> findInSingleRangeByUnit(@Param("category") String category,
                                         @Param("attr") String attr,
                                         @Param("operator") String operator,
                                         @Param("unit_item") String unit_item);

    /**
     * 单属性单类别单区间（时间型）
     * @param category 类别名
     * @param attr 属性名
     * @param operator 比较符
     * @param time_item 比较数值
     * @return 答案
     */
    List<Answer> findInSingleRangeByTime(@Param("category") String category,
                                         @Param("attr") String attr,
                                         @Param("operator") String operator,
                                         @Param("time_item") String time_item);

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
    List<Answer> findInMultiRangeByUnit(@Param("category") String category,
                                        @Param("attr") String attr,
                                        @Param("operator_1") String operator_1,
                                        @Param("item_1") String item_1,
                                        @Param("operator_2") String operator_2,
                                        @Param("item_2") String item_2);

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
    List<Answer> findInMultiRangeByTime(@Param("category") String category,
                                        @Param("attr") String attr,
                                        @Param("operator_1") String operator_1,
                                        @Param("item_1") String item_1,
                                        @Param("operator_2") String operator_2,
                                        @Param("item_2") String item_2);

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
    List<Answer> findMultiAttrInSingleRangeByUnit(@Param("category") String category,
                                                  @Param("attr_1") String attr_1,
                                                  @Param("operator_1") String operator_1,
                                                  @Param("item_1") String item_1,
                                                  @Param("attr_2") String attr_2,
                                                  @Param("operator_2") String operator_2,
                                                  @Param("item_2") String item_2);

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
    List<Answer> findMultiAttrInSingleRangeByTimeAndUnit(@Param("category") String category,
                                                         @Param("attr_1") String attr_1,
                                                         @Param("operator_1") String operator_1,
                                                         @Param("item_1") String item_1,
                                                         @Param("attr_2") String attr_2,
                                                         @Param("operator_2") String operator_2,
                                                         @Param("item_2") String item_2);

    /**
     * 单轮问答信息存入数据库
     * @param uid 用户id
     * @param question 问句
     * @param answer 答案JSON
     */
    void saveQaInfo(@Param("uid") String uid,
                    @Param("question") String question,
                    @Param("answer") String answer);

    /**
     * 根据实体ID查询实体所有属性名
     * @param entity_id 实体ID
     * @return 属性列表
     */
    List<Map<String, Object>> getAttrsByEntityName(@Param("entity_id") Long entity_id);

}
