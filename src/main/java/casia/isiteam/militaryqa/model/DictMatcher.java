package casia.isiteam.militaryqa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DictMatcher {

    /**
     * 存在库中的标准名
     */
    private String word;
    /**
     * 别名
     */
    private String alias;
    /**
     * 标签
     */
    private String label;
}
