package casia.isiteam.militaryqa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Result {

    /**
     * concepts表 c_id
     */
    private Long c_id;
    /**
     * concepts表 concept_name
     */
    private String concept_name;
    /**
     * concepts表 level
     */
    private Long level;
    /**
     * concepts表 concept1
     */
    private String concept1;
    /**
     * concepts_sameas表 concept2;
     */
    private String concept2;
    /**
     * entities表 entity_id
     */
    private int entity_id;
    /**
     * entities表 entity_name
     */
    private String entity_name;
    /**
     * entities表 entity_name_1
     */
    private String entity_name_1;
    /**
     * entity_sameas表 entity_name_2
     */
    private String entity_name_2;
    /**
     * match_dict表 word
     */
    private String word;
    /**
     * match_dict表 alias
     */
    private String alias;
    /**
     * match_dict表 label
     */
    private String label;
}
