package Mapper;

import Model.Answer;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface AnswerMapper {

    public List<Answer> findByCountryAndCategory(@Param("country") String country, @Param("category") String category);

    public List<Answer> findByEntity(@Param("entity") String entity);

    public List<Answer> findByEntityAndAttrs(@Param("entity") String entity, @Param("attrs") List<String> attrs);

    public List<Answer> findMaxByAttrInAllCategory(@Param("attr") String attr);

    public List<Answer> findMinByAttrInAllCategory(@Param("attr") String attr);

    public List<Answer> findMaxByAttrInSingleCategory(@Param("category") String category, @Param("attr") String attr);

    public List<Answer> findMinByAttrInSingleCategory(@Param("category") String category, @Param("attr") String attr);

    public List<Answer> findInSingleRangeByUnit(@Param("category") String category, @Param("attr") String attr,
                                          @Param("operator") String operator, @Param("unit_item") String unit_item);

    public List<Answer> findInSingleRangeByTime(@Param("category") String category, @Param("attr") String attr,
                                                @Param("operator") String operator, @Param("time_item") String time_item);

    public List<Answer> findInMultiRangeByUnit(@Param("category") String category, @Param("attr") String attr, @Param("operator_1") String operator_1,
                                               @Param("item_1") String item_1, @Param("operator_2") String operator_2, @Param("item_2") String item_2);

    public List<Answer> findInMultiRangeByTime(@Param("category") String category, @Param("attr") String attr, @Param("operator_1") String operator_1,
                                               @Param("item_1") String item_1, @Param("operator_2") String operator_2, @Param("item_2") String item_2);
}
