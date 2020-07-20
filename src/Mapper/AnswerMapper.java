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

    public List<Answer> findInSingleRange(@Param("category") String category, @Param("attr") String attr,
                                          @Param("type") String type, @Param("item") String item);
}
