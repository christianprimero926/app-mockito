package org.cospina.appmockito.ejemplos.services;

import org.cospina.appmockito.ejemplos.models.Exam;

import java.util.Optional;

public interface ExamService {
    Optional<Exam> findExamByName(String name);
    Exam findExamByNameWithQuestions(String name);
    Exam save(Exam exam);
}
