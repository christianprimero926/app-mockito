package org.cospina.appmockito.ejemplos.repositories;

import org.cospina.appmockito.ejemplos.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestionRepositoryImpl implements QuestionRepository{
    @Override
    public List<String> finQuestionByExamId(Long id) {
        System.out.println("QuestionRepositoryImpl.finQuestionByExamId");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Data.QUESTIONS;
    }

    @Override
    public void saveSeveral(List<String> preguntas) {
        System.out.println("QuestionRepositoryImpl.saveSeveral");
    }
}
