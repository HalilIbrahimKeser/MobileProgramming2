package com.example.lab6_1_trivia_quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ResultsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private int correctAnswersCount;
    private int correctAnswersSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        BottomNavigationView bottomNav2 = findViewById(R.id.bottom_navigation2);
        bottomNav2.setOnNavigationItemSelectedListener(this);

        SharedPreferences sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        correctAnswersCount = sharedPref.getInt("count", 0);
        correctAnswersSize = sharedPref.getInt("countSize", 0);

        TextView tvResultat = findViewById(R.id.tvResultat);
        TextView tvKarakter = findViewById(R.id.tvKarakter);

        String resultat = "Resultat: " + correctAnswersCount + " / " + correctAnswersSize;
        String karakter = "Karakter: " + calculateResult();

        tvResultat.setText(resultat);
        tvKarakter.setText(karakter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.hjem) {
            onBackPressed();
        }
        return false;
    }

    public char calculateResult () {
        char ch = 'F';
        double prosent = ((100.0 / correctAnswersSize) * correctAnswersCount);

        if (prosent < 40.0 ) {
            ch = 'F';
        }else if (prosent >= 41.0 && prosent <= 52.0 ) {
            ch = 'E';
        }else if (prosent >= 53.0 && prosent <= 64.0 ) {
            ch = 'D';
        }else if (prosent >= 65.0 && prosent <= 76.0 ) {
            ch = 'C';
        }else if (prosent >= 77.0 && prosent <= 88.0 ) {
            ch = 'B';
        }else if (prosent >= 89.0 && prosent <= 100.0 ) {
            ch = 'A';
        }else {
            ch = 'F';
        }return ch;
    }

    public void seFasit(View view) {
        Intent intent = new Intent(this, CorrectAnswersActivity.class);
        startActivity(intent);
    }
}
