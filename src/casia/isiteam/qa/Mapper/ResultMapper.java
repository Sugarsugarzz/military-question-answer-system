package casia.isiteam.qa.Mapper;


import casia.isiteam.qa.Model.Result;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResultMapper {

    List<Result> getConcepts();

    void emptyConceptSameas();

    void insertConceptSameas(@Param("alias") String alias, @Param("sameasId") Integer sameasId);

    List<Result> getEntities();

    void emptyEntitySameas();

    void insertEntitySameas(@Param("alias") String alias, @Param("entity_id") int entity_id);

    void emptyMatchDictByLabel(@Param("labels") List<String> labels);

    void insertMatchDict(@Param("word") String word, @Param("alias") String alias, @Param("label") String label);

    List<Result> getConceptsByLevel(@Param("levels") List<Integer> levels);

    List<Result> getConceptsSameas();

    List<Result> getEntitiesSameas();

    List<Result> getSimpleMatchDict();
}
