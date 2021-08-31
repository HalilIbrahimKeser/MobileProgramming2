package com.example.oblig1_sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.oblig1_sensors.databinding.MainActivityBinding;
import com.example.oblig1_sensors.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    //Sensors
    private SensorManager mSensorManager;

    private Sensor m3AxisAccelorometerSensor;
    private Sensor m3axisGyroscopeSensor;
    private Sensor m3axisMagneticFieldSensor;
    private Sensor mGoldfishOrientationSensor;
    private Sensor mAmbientTemperatureSensor;
    private Sensor mProximitySensor;
    private Sensor mLightsSensor;
    private Sensor mPressureSensor;
    private Sensor mHumiditySensor;
    private Sensor mHingeSensor;
    private Sensor mGameRotationVectorSensor;
    private Sensor mGeoMagRotationVectorSensor;
    private Sensor mGravitySensor;
    private Sensor mLinearAccelerationSensor;
    private Sensor mRotationVectorSensor;
    private Sensor mOrientationSensor;
//
    private TextView mTextSensorLight = binding.tvTest;
    private TextView mTextSensorProximity = binding.tvTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLightsSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}