package org.cospina.appmockito.ejemplos.services;

import org.cospina.appmockito.ejemplos.models.Exam;
import org.cospina.appmockito.ejemplos.repositories.ExamRepository;
import org.cospina.appmockito.ejemplos.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;
    private QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return examRepository.findAll()
                .stream()
                .filter(e -> e.getName().contains(name))
                .findFirst();

    }

    @Override
    public Exam findExamByNameWithQuestions(String name) {
        Optional<Exam> examOptional = findExamByName(name);
        Exam exam = null;
        if (examOptional.isPresent()) {
            exam = examOptional.orElseThrow();
            List<String> questions = questionRepository.finQuestionByExamId(exam.getId());
            exam.setQuestions(questions);
        }
        return exam;
    }

    @Override
    public Exam save(Exam exam) {
        if (!exam.getQuestions().isEmpty()) {
            questionRepository.saveSeveral(exam.getQuestions());
        }
        return  examRepository.save(exam);
    }
}
