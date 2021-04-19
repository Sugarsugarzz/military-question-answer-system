package casia.isiteam.militaryqa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WikiInfo {

    /**
     * wiki_info表 auto_id
     */
    private Long autoId;

    /**
     * wiki_info表 wikiID
     */
    private String wikiId;

    /**
     * wiki_info表 name
     */
    private String name;

    /**
     * wiki_info表 summary
     */
    private String summary;

    /**
     * wiki_info表 othernames + transnames + name衍生别名
     */
    private Set<String> aliases;

    /**
     * wiki_info表 infoBox
     */
    private Map<String, Object> attrBox;
}
