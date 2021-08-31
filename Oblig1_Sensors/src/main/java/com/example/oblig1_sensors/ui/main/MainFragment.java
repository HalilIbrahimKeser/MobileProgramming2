package com.example.oblig1_sensors.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oblig1_sensors.R;
import com.example.oblig1_sensors.databinding.MainActivityBinding;
import com.example.oblig1_sensors.databinding.MainFragmentBinding;

public class MainFragment extends Fragment  implements SensorEventListener {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;

    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private Sensor mLightsSensor;

    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

        if (mProximitySensor != null) {
            mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mLightsSensor != null) {
            mSensorManager.registerListener(this, mLightsSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvAcceleretor.setText("Hello");
        binding.tvGyroscope.setText("Ali");

        mTextSensorLight = binding.tvAcceleretor;
        mTextSensorProximity = binding.tvGyroscope;

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLightsSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        String sensor_error = getResources().getString(R.string.error_no_sensor);

        if (mLightsSensor == null) { mTextSensorLight.setText(sensor_error); }
        if (mProximitySensor == null) { mTextSensorProximity.setText(sensor_error);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        // The new data value of the sensor.  Both the light and proximity
        // sensors report one value at a time, which is always the first
        // element in the values array.
        float currentValue = sensorEvent.values[0];

        switch (sensorType) {
            // Event came from the light sensor.
            case Sensor.TYPE_LIGHT:
                // Set the light sensor text view to the light sensor string
                // from the resources, with the placeholder filled in.
                mTextSensorLight.setText(getResources().getString(
                        R.string.label_light, currentValue));
                break;
            case Sensor.TYPE_PROXIMITY:
                // Set the proximity sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextSensorProximity.setText(getResources().getString(
                        R.string.label_light, currentValue));
                break;
            default:
                // do nothing
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }
}