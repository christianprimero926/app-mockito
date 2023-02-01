package org.cospina.appmockito.ejemplos.repositories;

import java.util.List;

public interface QuestionRepository {
    List<String> finQuestionByExamId(Long id);
    void saveSeveral(List<String> preguntas);
}
