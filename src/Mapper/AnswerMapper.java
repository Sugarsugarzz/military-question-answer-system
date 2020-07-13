package Mapper;

import Model.Answer;

import java.util.List;

public interface AnswerMapper {

    public List<Answer> findByCountryAndCategory(String country, String category);

    public List<Answer> findByEntity(String entity);

    public List<Answer> findByEntityAndAttrs(String entity, List<String> attr);
}
