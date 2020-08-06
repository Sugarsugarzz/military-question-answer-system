package com.isiteam.qa.Model;

public class DictMatcher {

    // 标准名
    private String word;
    // 别名
    private String alias;
    // 标签
    private String label;

    public DictMatcher(String word, String alias, String label) {
        this.word = word;
        this.alias = alias;
        this.label = label;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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
        return "Matcher{" +
                "word='" + word + '\'' +
                ", alias='" + alias + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
