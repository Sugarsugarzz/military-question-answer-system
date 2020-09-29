package casia.isiteam.militaryqa.model;


public class Result {

    // concepts表 c_id
    private int c_id;
    // concepts表 concept_name
    private String concept_name;
    // concepts表 level
    private int level;
    // concepts表 concept1
    private String concept1;
    // concepts_sameas表 concept2;
    private String concept2;
    // entities表 entity_id
    private int entity_id;
    // entities表 entity_name
    private String entity_name;
    // entities表 entity_name_1
    private String entity_name_1;
    // entity_sameas表 entity_name_2
    private String entity_name_2;
    // match_dict表 alias
    private String alias;
    // match_dict表 label
    private String label;

    public Result() {
    }

    public Result(int c_id, String concept_name, int level, String concept1, String concept2, int entity_id, String entity_name, String entity_name_1, String entity_name_2, String alias, String label) {
        this.c_id = c_id;
        this.concept_name = concept_name;
        this.level = level;
        this.concept1 = concept1;
        this.concept2 = concept2;
        this.entity_id = entity_id;
        this.entity_name = entity_name;
        this.entity_name_1 = entity_name_1;
        this.entity_name_2 = entity_name_2;
        this.alias = alias;
        this.label = label;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getConcept_name() {
        return concept_name;
    }

    public void setConcept_name(String concept_name) {
        this.concept_name = concept_name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getConcept1() {
        return concept1;
    }

    public void setConcept1(String concept1) {
        this.concept1 = concept1;
    }

    public String getConcept2() {
        return concept2;
    }

    public void setConcept2(String concept2) {
        this.concept2 = concept2;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public String getEntity_name_1() {
        return entity_name_1;
    }

    public void setEntity_name_1(String entity_name_1) {
        this.entity_name_1 = entity_name_1;
    }

    public String getEntity_name_2() {
        return entity_name_2;
    }

    public void setEntity_name_2(String entity_name_2) {
        this.entity_name_2 = entity_name_2;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Result{" +
                "c_id=" + c_id +
                ", concept_name='" + concept_name + '\'' +
                ", level=" + level +
                ", concept1='" + concept1 + '\'' +
                ", concept2='" + concept2 + '\'' +
                ", entity_id=" + entity_id +
                ", entity_name='" + entity_name + '\'' +
                ", entity_name_1='" + entity_name_1 + '\'' +
                ", entity_name_2='" + entity_name_2 + '\'' +
                ", alias='" + alias + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
