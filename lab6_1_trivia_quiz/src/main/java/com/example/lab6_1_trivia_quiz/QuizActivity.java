package com.example.lab6_1_trivia_quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.lab6_1_trivia_quiz.fragments.QuizActivitySlideFragment;
import com.example.lab6_1_trivia_quiz.repository.myRepository;
import com.example.lab6_1_trivia_quiz.viewmodel.myViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.util.Map;

public class QuizActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final myRepository myRepo = myRepository.getInstance();
    private int numPages = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        myViewModel myViewModel = new ViewModelProvider(this).get(myViewModel.class);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation1);
        bottomNav.setOnNavigationItemSelectedListener(this);

        String path = this.getFilesDir().getAbsolutePath()+"/running_quiz.json";
        File yourFile = new File(path);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> sharedPrefs = sharedPreferences.getAll();
        String temp = String.valueOf(sharedPrefs.get("num"));
        numPages = Integer.parseInt(temp);

        if (!yourFile.exists()) {
            myViewModel.getQuiz(String.valueOf(sharedPrefs.get("num")), String.valueOf(sharedPrefs.get("category")),
                    String.valueOf(sharedPrefs.get("diff")), String.valueOf(sharedPrefs.get("type"))).observe(this, quizData -> {
                myRepo.writeInternalFile(this, quizData);
            });
        }else if (yourFile.exists() && myRepo.readInternalFile(getApplicationContext()).isEmpty()){
            //LAG NY
            myViewModel.getQuiz(String.valueOf(sharedPrefs.get("num")), String.valueOf(sharedPrefs.get("category")),
                    String.valueOf(sharedPrefs.get("diff")), String.valueOf(sharedPrefs.get("type"))).observe(this, quizData -> {
                myRepo.writeInternalFile(this, quizData); });
        }else {
            myRepo.readInternalFile(getApplicationContext());
        }

        getSharedPreferences("MyPref",0).edit().clear().apply();
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setPageTransformer(new zDepthPageTransformer());

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hjem:
                onBackPressed();
                break;
            case R.id.fasit:
                Intent intent = new Intent(this, ResultsActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return QuizActivitySlideFragment.newInstance(position);
        }
        @Override
        public int getItemCount() {
            return numPages;
        }
    }
}
