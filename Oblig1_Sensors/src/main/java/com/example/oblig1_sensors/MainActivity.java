package com.example.oblig1_sensors;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oblig1_sensors.databinding.MainActivityBinding;
import com.example.oblig1_sensors.ui.main.ListOfSensorsFragment;
import com.example.oblig1_sensors.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    public void goBack(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow();
    }

    public void goToList(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ListOfSensorsFragment.newInstance())
                .commitNow();
    }
}