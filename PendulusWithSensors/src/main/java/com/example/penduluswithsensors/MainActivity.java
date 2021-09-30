package com.example.penduluswithsensors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void dropTheCircle(View view) {
        PendulumView.DropTheCircleFromSurface();
    }

    public void resetValues(View view) {
        PendulumView.resetValues();
    }
}