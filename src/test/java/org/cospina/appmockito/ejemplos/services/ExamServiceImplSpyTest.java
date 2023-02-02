package org.cospina.appmockito.ejemplos.services;

import org.cospina.appmockito.ejemplos.Data;
import org.cospina.appmockito.ejemplos.models.Exam;
import org.cospina.appmockito.ejemplos.repositories.ExamRepository;
import org.cospina.appmockito.ejemplos.repositories.ExamRepositoryImpl;
import org.cospina.appmockito.ejemplos.repositories.QuestionRepository;
import org.cospina.appmockito.ejemplos.repositories.QuestionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplSpyTest {

    @Spy
    ExamRepositoryImpl repository;
    @Spy
    QuestionRepositoryImpl questionRepository;
    @InjectMocks
    ExamServiceImpl service;

    @Test
    void testSpy() {
        List<String> questions = Arrays.asList("aritmetica");
        //when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(questions);
        doReturn(questions).when(questionRepository).finQuestionByExamId(anyLong());

        Exam exam = service.findExamByNameWithQuestions("Matematicas");

        assertEquals(5L, exam.getId());
        assertEquals("Matematicas", exam.getName());
        assertEquals(1, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("aritmetica"));

        verify(repository).findAll();
        verify(questionRepository).finQuestionByExamId(anyLong());
    }
}