package com.example.oblig1_sensors.ui.main;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oblig1_sensors.R;
import com.example.oblig1_sensors.databinding.FragmentListOfSensorsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListOfSensorsFragment extends Fragment {

    private FragmentListOfSensorsBinding binding;
    private SensorManager mSensorManager;

    public static ListOfSensorsFragment newInstance() {
        return new ListOfSensorsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @NotNull ViewGroup container,
                             @NotNull Bundle savedInstanceState) {
        binding = FragmentListOfSensorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList  = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : sensorList ) {
            sensorText.append(currentSensor.getName()).append(
                    System.getProperty("line.separator"));
        }

        TextView mTextSensorList = binding.tvListOfSensors;
        mTextSensorList.setText(sensorText);
    }

    public void goBack(View view) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}