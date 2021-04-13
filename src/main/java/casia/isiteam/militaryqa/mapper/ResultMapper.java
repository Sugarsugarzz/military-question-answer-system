package casia.isiteam.militaryqa.mapper;


import casia.isiteam.militaryqa.model.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResultMapper {

    /**
     * 获取 Concepts 表
     */
    List<Result> getConcepts();

    /**
     * 清空 concept_sameas 表
     */
    void emptyConceptSameas();

    /**
     * 添加 concept 别名
     * @param alias 别名
     * @param sameasId 关联的id
     */
    void insertConceptSameas(@Param("alias") String alias,
                             @Param("sameasId") Integer sameasId);

    /**
     * 获取 Entities 表
     */
    List<Result> getEntities();

    /**
     * 清空 entity_sameas 表
     */
    void emptyEntitySameas();

    /**
     * 添加 entity 别名
     * @param alias 别名
     * @param entity_id 对应实体id
     */
    void insertEntitySameas(@Param("alias") String alias,
                            @Param("entity_id") int entity_id);

    /**
     * 根据标签清理 match_dict 表
     * @param labels 标签
     */
    void emptyMatchDictByLabel(@Param("labels") List<String> labels);

    /**
     * 存入 match_dict 表
     * @param word 原词
     * @param alias 别名
     * @param label 标签
     */
    void insertMatchDict(@Param("word") String word,
                         @Param("alias") String alias,
                         @Param("label") String label);

    /**
     * 根据 level 获取 concepts
     * @param levels comcept level
     */
    List<Result> getConceptsByLevel(@Param("levels") List<Integer> levels);

    /**
     * 获取 concepts 别名
     */
    List<Result> getConceptsSameas();

    /**
     * 获取 entities 别名
     */
    List<Result> getEntitiesSameas();

    /**
     * 获取 match_dict 的 alias 和 label
     */
    List<Result> getSimpleMatchDict();
}
