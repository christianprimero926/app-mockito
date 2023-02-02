package org.cospina.appmockito.ejemplos.repositories;

import org.cospina.appmockito.ejemplos.Data;
import org.cospina.appmockito.ejemplos.models.Exam;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamRepositoryImpl implements ExamRepository{
    @Override
    public Exam save(Exam exam) {
        System.out.println("ExamRepositoryImpl.save");
        return Data.EXAM;
    }

    @Override
    public List<Exam> findAll() {
        System.out.println("ExamRepositoryImpl.findAll");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Data.EXAMS;
    }
}
