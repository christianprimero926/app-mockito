package org.cospina.appmockito.ejemplos.repositories;

import org.cospina.appmockito.ejemplos.models.Exam;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamRepositoryOther implements ExamRepository{
    @Override
    public List<Exam> findAll() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
