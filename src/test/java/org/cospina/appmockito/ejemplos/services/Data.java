package org.cospina.appmockito.ejemplos.services;

import org.cospina.appmockito.ejemplos.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    public final static List<Exam> EXAMS = Arrays.asList(
            new Exam(5L, "Matematicas"),
            new Exam(6L, "Lenguajes"),
            new Exam(7L, "Historia"));
    public final static List<String> QUESTIONS = Arrays.asList("aritmetica", "integrales", "derivadas", "trigonometria", "geometria");

    public final static Exam EXAM = new Exam(8L, "fisica");
}
