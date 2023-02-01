package org.cospina.appmockito.ejemplos.repositories;

import org.cospina.appmockito.ejemplos.models.Exam;

import java.util.List;

public interface ExamRepository {
    Exam save(Exam exam);
    List<Exam> findAll();
}
