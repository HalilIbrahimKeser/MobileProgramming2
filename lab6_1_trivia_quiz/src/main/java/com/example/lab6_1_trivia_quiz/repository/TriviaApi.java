package com.example.lab6_1_trivia_quiz.repository;

import com.example.lab6_1_trivia_quiz.models.QuizData;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface TriviaApi {
    @GET("/api.php")
    Call<QuizData> downloadQuiz(@QueryMap Map<String, String> urlArguments);
}