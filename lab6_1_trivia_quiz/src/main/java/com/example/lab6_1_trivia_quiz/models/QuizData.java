package com.example.lab6_1_trivia_quiz.models;

import java.util.ArrayList;

public class QuizData {
    private int response_code;
    private ArrayList<Question> results;

    public QuizData() {
        this.response_code = response_code;
        this.results = results;
    }

    public int getResponse_code() {
        return response_code;
    }

    public ArrayList<Question> getResults() {
        return results;
    }
}
