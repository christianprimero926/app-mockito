package org.cospina.appmockito.ejemplos;

import org.cospina.appmockito.ejemplos.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    public final static List<Exam> EXAMS = Arrays.asList(
            new Exam(5L, "Matematicas"),
            new Exam(6L, "Lenguajes"),
            new Exam(7L, "Historia"));
    public final static List<String> QUESTIONS = Arrays.asList("aritmetica", "integrales", "derivadas", "trigonometria", "geometria");

    public final static Exam EXAM = new Exam(null, "fisica");

    public final static List<Exam> EXAMS_ID_NULL = Arrays.asList(
            new Exam(null, "Matematicas"),
            new Exam(null, "Lenguajes"),
            new Exam(null, "Historia"));

    public final static List<Exam> EXAMS_ID_NEGATIVE = Arrays.asList(
            new Exam(-5l, "Matematicas"),
            new Exam(-6l, "Lenguajes"),
            new Exam(null, "Historia"));
}
