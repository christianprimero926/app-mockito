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

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    ExamRepositoryImpl repository;
    @Mock
    QuestionRepositoryImpl questionRepository;
    @InjectMocks
    ExamServiceImpl service;
    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
//        repository = mock(ExamRepositoryImpl.class);
//        questionRepository = mock(QuestionRepositoryImpl.class);
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
        //Given
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        //When
        Exam exam = service.findExamByNameWithQuestions("Matematicas2");

        //then
        assertNull(exam);
        verify(repository).findAll();
        verify(questionRepository).finQuestionByExamId(5L);
    }

    @Test
    void testSaveExam() {

        //Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);

        when(repository.save(any(Exam.class))).then(new Answer<Exam>() {
            Long sequence = 8L;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        });

        //When
        Exam exam = service.save(newExam);

        //Then
        assertNotNull(exam.getId(), () -> "no puede estar nulo");
        assertEquals(8L, exam.getId(), () -> "no coincide el esperado al actual");
        assertEquals("fisica", exam.getName(), () -> "no coincide el esperado al actual");

        verify(repository).save(any(Exam.class));
        verify(questionRepository).saveSeveral(anyList());
    }

    @Test
    void testManageException() {
        //Given
        when(repository.findAll()).thenReturn(Data.EXAMS_ID_NULL);
        when(questionRepository.finQuestionByExamId(isNull())).thenThrow(IllegalArgumentException.class);

        //When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamByNameWithQuestions("Matematicas");
        });

        //Then
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repository).findAll();
        verify(questionRepository).finQuestionByExamId(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Matematicas");

        verify(repository).findAll();

        //verify(questionRepository).finQuestionByExamId(eq(5L));
        //verify(questionRepository).finQuestionByExamId(argThat(arg-> arg != null && arg.equals(5L)));
        verify(questionRepository).finQuestionByExamId(argThat(arg -> arg != null && arg >= 5L));
    }

    @Test
    void testArgumentMatchers2() {
        when(repository.findAll()).thenReturn(Data.EXAMS_ID_NEGATIVE);
        when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Matematicas");

        verify(repository).findAll();
        verify(questionRepository).finQuestionByExamId(argThat(new MyArgstMatchers()));
    }

    @Test
    void testArgumentMatchers3() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Matematicas");

        verify(repository).findAll();
        verify(questionRepository).finQuestionByExamId(argThat((argument) -> argument != null && argument > 0));
    }

    public static class MyArgstMatchers implements ArgumentMatcher<Long> {
        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "es para un mensaje personalizado de error" +
                    " que se imprime cuando falla el test " +
                    argument + " debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        //when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Matematicas");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(questionRepository).finQuestionByExamId(captor.capture());

        assertEquals(5L, captor.getValue(), () -> "El valor no coincide");
    }

    @Test
    void testDoThrow() {
        Exam exam = Data.EXAM;
        exam.setQuestions(Data.QUESTIONS);
        doThrow(IllegalArgumentException.class).when(questionRepository).saveSeveral(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.save(exam);
        });
    }

    @Test
    void testDoAnswer() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        //when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? Data.QUESTIONS : Collections.emptyList();
        }).when(questionRepository).finQuestionByExamId(anyLong());

        Exam exam = service.findExamByNameWithQuestions("Matematicas");

        assertAll(
                () -> assertEquals(5L, exam.getId(), () -> "No coincide el Id"),
                () -> assertEquals("Matematicas", exam.getName(), () -> "No coincide el nombre"),
                () -> assertEquals(5, exam.getQuestions().size(), () -> "No coincide el # de preguntas"),
                () -> assertTrue(exam.getQuestions().contains("geometria"), () -> exam.getName() + " no contiene geometria")
        );
        verify(questionRepository).finQuestionByExamId(anyLong());


    }

    @Test
    void testDoAnswerSaveExam() {
        //Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);

        doAnswer(new Answer<Exam>() {
            Long sequence = 8L;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        }).when(repository).save(any(Exam.class));

        //When
        Exam exam = service.save(newExam);

        //Then
        assertNotNull(exam.getId(), () -> "no puede estar nulo");
        assertEquals(8L, exam.getId(), () -> "no coincide el esperado al actual");
        assertEquals("fisica", exam.getName(), () -> "no coincide el esperado al actual");

        verify(repository).save(any(Exam.class));
        verify(questionRepository).saveSeveral(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        //when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        doCallRealMethod().when(questionRepository).finQuestionByExamId(anyLong());
        Exam exam = service.findExamByNameWithQuestions("Matematicas");
        assertEquals(5L, exam.getId());
        assertEquals("Matematicas", exam.getName());
    }

    @Test
    void testSpy() {
        //ExamRepository mockExamRepository = mock(ExamRepository.class);
        ExamRepository examRepository = spy(ExamRepositoryImpl.class);
        QuestionRepository questionRepository = spy(QuestionRepositoryImpl.class);
        ExamService examService = new ExamServiceImpl(examRepository, questionRepository);

        List<String> questions = Arrays.asList("aritmetica");
        //when(questionRepository.finQuestionByExamId(anyLong())).thenReturn(questions);
        doReturn(questions).when(questionRepository).finQuestionByExamId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("Matematicas");

        assertEquals(5L, exam.getId());
        assertEquals("Matematicas", exam.getName());
        assertEquals(1, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("aritmetica"));

        verify(examRepository).findAll();
        verify(questionRepository).finQuestionByExamId(anyLong());
    }

    @Test
    void testInvocationOrder() {
        when(repository.findAll()).thenReturn(Data.EXAMS);

        service.findExamByNameWithQuestions("Matematicas");
        service.findExamByNameWithQuestions("Lenguaje");

        InOrder inOrder = inOrder(questionRepository);
        inOrder.verify(questionRepository).finQuestionByExamId(5L);
        inOrder.verify(questionRepository).finQuestionByExamId(6L);
    }

    @Test
    void testInvocationOrder2() {
        when(repository.findAll()).thenReturn(Data.EXAMS);

        service.findExamByNameWithQuestions("Matematicas");
        service.findExamByNameWithQuestions("Lenguaje");

        InOrder inOrder = inOrder(repository, questionRepository);
        inOrder.verify(repository).findAll();
        inOrder.verify(questionRepository).finQuestionByExamId(5L);

        inOrder.verify(repository).findAll();
        inOrder.verify(questionRepository).finQuestionByExamId(6L);
    }

    @Test
    void testNumberInvocations() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        service.findExamByNameWithQuestions("Matematicas");

        verify(questionRepository).finQuestionByExamId(5L);
        verify(questionRepository, times(1)).finQuestionByExamId(5L);
        verify(questionRepository, atLeast(1)).finQuestionByExamId(5L);
        verify(questionRepository, atLeastOnce()).finQuestionByExamId(5L);
        verify(questionRepository, atMost(10)).finQuestionByExamId(5L);
        verify(questionRepository, atMostOnce()).finQuestionByExamId(5L);
    }

    @Test
    void testNumberInvocations2() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        service.findExamByNameWithQuestions("Matematicas");

        //verify(questionRepository).finQuestionByExamId(5L); fail
        verify(questionRepository, times(2)).finQuestionByExamId(5L);
        verify(questionRepository, atLeast(1)).finQuestionByExamId(5L);
        verify(questionRepository, atLeastOnce()).finQuestionByExamId(5L);
        verify(questionRepository, atMost(2)).finQuestionByExamId(5L);
        //verify(questionRepository, atMostOnce()).finQuestionByExamId(5L); fail
    }

    @Test
    void testNumberInvocation3() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        service.findExamByNameWithQuestions("Matematicas");

        verify(questionRepository, never()).finQuestionByExamId(5L);
        verifyNoInteractions(questionRepository);

        verify(repository).findAll();
        verify(repository, times(1)).findAll();
        verify(repository, atLeast(1)).findAll();
        verify(repository, atLeastOnce()).findAll();
        verify(repository, atMost(10)).findAll();
        verify(repository, atMostOnce()).findAll();

    }
}