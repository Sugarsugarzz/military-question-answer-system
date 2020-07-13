package Mapper;

import Model.Answer;

import java.util.List;

public interface AnswerMapper {

    public List<Answer> findByEntityAndAttrs(String entity, List<String> attr);
}
