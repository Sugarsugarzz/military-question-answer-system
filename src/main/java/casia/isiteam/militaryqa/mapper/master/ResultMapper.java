package casia.isiteam.militaryqa.mapper.master;


import casia.isiteam.militaryqa.model.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResultMapper {

    /**
     * 获取wiki_info处理游标
     */
    Long getCursor();

    /**
     * 更新wiki_info处理游标
     */
    void updateCursor(@Param("cursor") Long cursor);

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

    /**
     * 获取 match_dict 中的 concepts stop words
     */
    List<String> getConceptsStopWords();


    /**
     * 根据wiki数据名去实体表查看是否存在，存在则返回 entity_id
     * @param name 实体名
     * @return     entity_id
     */
    Integer getEntityIdByName(@Param("name") String name);

    /**
     * 获取 Entities表最大 entity_id
     */
    Long getEntitiesMaxId();

    /**
     * 获取 Concepts表最大 concept_id
     */
    Long getConceptsMaxId();

    /**
     * 保存到 Entities
     */
    void saveEntities(@Param("entity_id") Long entity_id,
                      @Param("entity_name") String entity_name,
                      @Param("concept_id") Long concept_id,
                      @Param("wiki_info_id") String wiki_info_id);

    /**
     * 保存到 EntitySameas
     */
    void saveEntitySameAs(@Param("entity_name") String entity_name,
                          @Param("sameAs_id") Long sameAs_id);

    /**
     * 保存到 EntityAttr
     */
    void saveEntityAttr(@Param("entity_id") Long entity_id,
                        @Param("attr_id") Long attr_id,
                        @Param("attr_value") String attr_value,
                        @Param("attr_value_reg") String attr_value_reg,
                        @Param("attr_value_reg_unit") String attr_value_reg_unit);

    /**
     * 根据 concept_name 获取 concept_id
     */
    Long getConceptIdByName(@Param("concept_name") String concept_name);

    /**
     * 保存到 Concepts 属性
     */
    void saveConcept(@Param("cid") Long cid,
                     @Param("concept_name") String concept_name);

    /**
     * 获取二级类别的类别名及对应c_id，用于新数据分类
     */
    List<Result> getSmallCategories();
}
