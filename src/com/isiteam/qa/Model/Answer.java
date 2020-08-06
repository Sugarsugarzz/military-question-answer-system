package com.isiteam.qa.Model;

public class Answer {

    // 实体id
    private int entity_id;
    // 实体名
    private String entity_name;
    // 属性名
    private String attr_name;
    // 属性值
    private String attr_value;

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

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public String getAttr_value() {
        return attr_value;
    }

    public void setAttr_value(String attr_value) {
        this.attr_value = attr_value;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "entity_id=" + entity_id +
                ", entity_name='" + entity_name + '\'' +
                ", attr_name='" + attr_name + '\'' +
                ", attr_value='" + attr_value + '\'' +
                '}';
    }
}
