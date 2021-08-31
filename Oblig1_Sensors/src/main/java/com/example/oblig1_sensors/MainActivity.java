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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

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
    private Sensor mGeoMagneticRotationSensor;
    private Sensor mGravitySensor;
    private Sensor mLinearAccelerationSensor;
    private Sensor mRotationVectorSensor;
    private Sensor mOrientationSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        // Initialize all view variables.
        mTextSensorLight = binding.label_light;
        mTextSensorProximity = (TextView) findViewById(R.id.label_proximity);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

        // Get light and proximity sensors from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorProximity = mSensorManager.getDefaultSensor(
                Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If either mSensorLight or mSensorProximity are null, those sensors
        // are not available in the device.  Set the text to the error message
        if (mSensorLight == null) { mTextSensorLight.setText(sensor_error); }
        if (mSensorProximity == null) {
            mTextSensorProximity.setText(sensor_error);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}