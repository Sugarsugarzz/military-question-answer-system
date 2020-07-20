package Mapper;

import Model.Answer;

import java.util.Collection;
import java.util.List;

public interface AnswerMapper {

    public List<Answer> findByCountryAndCategory(String country, String category);

    public List<Answer> findByEntity(String entity);

    public List<Answer> findByEntityAndAttrs(String entity, List<String> attr);

    public List<Answer> findMaxByAttrInAllCategory(String attr);

    public List<Answer> findMinByAttrInAllCategory(String attr);

    public List<Answer> findMaxByAttrInSingleCategory(String category, String attr);

    public List<Answer> findMinByAttrInSingleCategory(String category, String attr);

    public List<Answer> findInSingleRange(String category, String attr, String type, String item);
}
