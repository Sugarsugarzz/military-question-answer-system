package casia.isiteam.militaryqa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Answer {

    /**
     * 实体id
     */
    private int entity_id;
    /**
     * 实体名
     */
    private String entity_name;
    /**
     * 属性名
     */
    private String attr_name;
    /**
     * 属性值
     */
    private String attr_value;
}
