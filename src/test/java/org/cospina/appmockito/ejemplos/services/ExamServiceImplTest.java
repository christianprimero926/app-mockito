package org.cospina.appmockito.ejemplos.services;

import org.cospina.appmockito.ejemplos.models.Exam;
import org.cospina.appmockito.ejemplos.repositories.ExamRepository;
import org.cospina.appmockito.ejemplos.repositories.ExamRepositoryOther;
import org.cospina.appmockito.ejemplos.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    ExamRepository repository;
    @Mock
    QuestionRepository questionRepository;
    @InjectMocks
    ExamServiceImpl service;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
//        repository = mock(ExamRepositoryOther.class);
//        questionRepository = mock(QuestionRepository.class);
//        service = new ExamServiceImpl(repository, questionRepository);
    }

    @Test
    void findExamByName() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        Optional<Exam> exam = service.findExamByName("Matematicas");
        assertAll(
                () -> assertTrue(exam.isPresent(), () -> "Examen no se encuentra"),
                () -> assertEquals(5L, exam.orElseThrow().getId(), () -> "El id actual no corresponde al esperado"),
                () -> assertEquals("Matematicas", exam.get().getName(), () -> "El nombre actual no corresponde al esperado")
        );
    }

    @Test
    void findExamByNameListaVacia() {
        List<Exam> exams = Collections.emptyList();

        when(repository.findAll()).thenReturn(exams);
        Optional<Exam> exam = service.findExamByName("Matematicas");

        assertFalse(exam.isPresent(), () -> "Examen no se encuentra");
    }

    @Test
    void testQuestionExam() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        Exam exam = service.findExamByNameWithQuestions("Matematicas");
        assertEquals(5, exam.getQuestions().size(), () -> "no coincide el esperado al actual");
        assertTrue(exam.getQuestions().contains("integrales"));
    }

    @Test
    void testQuestionExamVerify() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        Exam exam = service.findExamByNameWithQuestions("Matematicas");
        assertEquals(5, exam.getQuestions().size(), () -> "no coincide el esperado al actual");
        assertTrue(exam.getQuestions().contains("integrales"));
        verify(repository).findAll();
        verify(questionRepository).finQuestionByExamId(anyLong());
    }

    @Test
    void testNoExistQuestionExamVerify() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        Exam exam = service.findExamByNameWithQuestions("Matematicas2");
        assertNull(exam);
        verify(repository).findAll();
        verify(questionRepository).finQuestionByExamId(5L);
    }

    @Test
    void testSaveExam() {
        when(repository.save(any(Exam.class))).thenReturn(Data.EXAM);
        Exam exam = service.save(Data.EXAM);
        assertNotNull(exam.getId(), ()->"no puede estar nulo");
    }
}