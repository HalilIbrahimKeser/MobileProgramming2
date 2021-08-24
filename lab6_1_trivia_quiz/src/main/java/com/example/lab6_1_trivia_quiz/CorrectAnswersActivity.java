package com.example.lab6_1_trivia_quiz;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.lab6_1_trivia_quiz.fragments.CorrectAnswersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CorrectAnswersActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_answers);

        Fragment fragment = CorrectAnswersFragment.newInstance(1);
        replaceFragmentWidth(fragment, false);

        BottomNavigationView bottomNav2 = findViewById(R.id.bottom_navigation3);
        bottomNav2.setOnNavigationItemSelectedListener(this);
    }

    public void replaceFragmentWidth(Fragment newFragment, boolean addTobackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (addTobackStack)
            fragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .add(R.id.fragment_correctAnswersContainer, newFragment)
                    .commit();
        else
            fragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_correctAnswersContainer, newFragment)
                    .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.hjem) {
            onBackPressed();
        }
        return false;
    }
}
