package com.example.lab6_1_trivia_quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.lab6_1_trivia_quiz.repository.myRepository;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private final myRepository myRepo = myRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar quizToolbar = findViewById(R.id.quizToolbar1);
        setSupportActionBar(quizToolbar);

    }

    public void settingsActivity(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void startGame(View view) {
        String path = this.getFilesDir().getAbsolutePath()+"/running_quiz.json";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!path.isEmpty() && !sharedPreferences.getAll().isEmpty()) {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Gå inn på innstillinger og\nVelg nye spørsmål fra innstillinger", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetGame (View view) {
        //SLETTER SHARED
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        //SLETT DEN TOMME FILEN
        String path = this.getFilesDir().getAbsolutePath()+"/running_quiz.json";
        myRepo.deleteInternalFile(getApplicationContext(), path);

        Toast.makeText(this, "Innstillinger og lokalspørsmålsfil slettet.\nVelg nye spørsmål fra innstillinger", Toast.LENGTH_SHORT).show();
    }
}