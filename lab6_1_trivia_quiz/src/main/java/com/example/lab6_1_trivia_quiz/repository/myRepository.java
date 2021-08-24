package com.example.lab6_1_trivia_quiz.repository;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import com.example.lab6_1_trivia_quiz.models.Question;
import com.example.lab6_1_trivia_quiz.models.QuizData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class myRepository {
    private static myRepository repository;

    public static myRepository getInstance() {
        if (repository == null) {
            repository = new myRepository();
        }
        return repository;
    }

    private final TriviaApi triviaApi;
    private final MutableLiveData<ArrayList<Question>> quizData;
    private final MutableLiveData<String> errorMessage;

    //RETROFITT
    private myRepository() {
        errorMessage = new MutableLiveData<>();
        quizData = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        triviaApi = retrofit.create(TriviaApi.class);
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    //LAST NED QUIZ
    public MutableLiveData<ArrayList<Question>> downloadQuiz(String amount, String category, String difficulty, String type) {
        Map<String, String> urlArguments = new HashMap<>();
        urlArguments.put("amount", amount);
        urlArguments.put("category", category);
        urlArguments.put("difficulty", difficulty);
        urlArguments.put("type", type);

        Call<QuizData> call = triviaApi.downloadQuiz(urlArguments);
        call.enqueue(new Callback<QuizData>() {
            @Override
            public void onResponse(Call<QuizData> call, Response<QuizData> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                QuizData data = response.body();
                ArrayList<Question> tmp = data.getResults();
                quizData.setValue(tmp);
            }
            @Override
            public void onFailure(Call<QuizData> call, Throwable t) {
            }
        });
        return quizData;
    }

    //LES FRA INTERN FIL
    public ArrayList<Question> readInternalFile(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = context.openFileInput("running_quiz.json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            String jsonArrayAsString = line.toString();

            //Konverter fra JSON til ArrayList<Question>
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Question>>(){}.getType();

            ArrayList<Question> tmpList = gson.fromJson(jsonArrayAsString, type);
            quizData.setValue(tmpList);
            return tmpList;
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }// Error occurred when opening raw file for reading.

    }

    //SKRIV TIL INTEN FIL
    public void writeInternalFile(Context context, ArrayList<Question> tmpList) {
        Gson gson = new Gson();
        String questionListAsString = gson.toJson(tmpList);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput("running_quiz.json", Context.MODE_PRIVATE);
            fileOutputStream.write(questionListAsString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //DELETE INTERNAL FILE
    public void deleteInternalFile(Context context, String path) {
        try {
            String folder = context.getFilesDir().getAbsolutePath();
            File file = new File(folder, "running_quiz.json");
            if(file.exists()) {
                file.delete();
//                if (!file.exists())
//                Toast.makeText(context, "Fil slettet", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}