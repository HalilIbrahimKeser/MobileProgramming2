package com.example.oblig1_sensors.ui.main;
//https://github.com/google-developer-training/android-advanced/blob/master/SensorListeners/app/src/main/java/com/example/android/sensorlisteners/MainActivity.java
//Bygget på koden ovenfor fra github

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
import com.example.oblig1_sensors.databinding.MainFragmentBinding;

public class MainFragment extends Fragment implements SensorEventListener {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;

    private SensorManager mSensorManager;

    //Base sensors
    private Sensor mLightsSensor;
    private Sensor mProximitySensor;
    private Sensor mAmbientTemperatureSensor;
    private Sensor mMagneticFieldSensor;
    private Sensor mPressureSensor;
    private Sensor mHumiditySensor;
    private Sensor mAccelorometerSensor;
    private Sensor mLinearAccelerationSensor;
    private Sensor mGyroscopeSensor;

    private Sensor mGoldfishOrientationSensor;
    private Sensor mRotationVectorSensor;

    // Composite sensors
    private Sensor mOrientationSensor;
    private Sensor mGameRotationVectorSensor;
    private Sensor mGeoMagRotationVectorSensor;
    private Sensor mGravitySensor;

    // To compute orientation
    float[] mGravity;
    float[] mGeomagnetic;

    private TextView mTextLightsSensor, mTextProximitySensor, mTextAmbientTemperatureSensor, mTextMagneticFieldSensor,
            mTextPressureSensor, mTextHumiditySensor, mTextOrientationSensor, mTextAccelorometerSensor;

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextLightsSensor = binding.tvLight;
        mTextProximitySensor = binding.tvGyroscope;
        mTextAmbientTemperatureSensor = binding.tvTemperaure;
        mTextMagneticFieldSensor = binding.tvMagneticField;
        mTextPressureSensor = binding.tvPressure;
        mTextHumiditySensor = binding.tvHummidity;
        mTextOrientationSensor = binding.tvOrientation;
        mTextAccelorometerSensor = binding.tvAccelorometer;

        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLightsSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAmbientTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mHumiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        //mOrientationSensor = 0;
        mAccelorometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        checkErrorOfSensorNorFound();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        String sensorResult = sensorEvent.sensor.getName() + ": " + sensorEvent.values[0];
        String sensorResultMagneticFiels = sensorEvent.sensor.getName() + ": \n" + "North: " + sensorEvent.values[0] + ",  East: " + sensorEvent.values[1] + ",  Up: " + sensorEvent.values[2];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                mTextLightsSensor.setText(sensorResult + " lux");
                break;
            case Sensor.TYPE_PROXIMITY:
                mTextProximitySensor.setText(sensorResult + " cm");
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                mTextAmbientTemperatureSensor.setText(sensorResult + " C°");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mTextMagneticFieldSensor.setText(sensorResultMagneticFiels);
                break;
            case Sensor.TYPE_PRESSURE:
                mTextPressureSensor.setText(sensorResult + " hPa");
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                mTextHumiditySensor.setText(sensorResult + " %");
                break;
            case Sensor.TYPE_ACCELEROMETER:
                mTextAccelorometerSensor.setText(sensorResult + " %");
                break;
            default:
                // do nothing
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //LISTENERS OF SENSORS
        if (mLightsSensor != null) {
            mSensorManager.registerListener(this, mLightsSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mProximitySensor != null) {
            mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mAmbientTemperatureSensor != null) {
            mSensorManager.registerListener(this, mAmbientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mMagneticFieldSensor != null) {
            mSensorManager.registerListener(this, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mPressureSensor != null) {
            mSensorManager.registerListener(this, mPressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mHumiditySensor != null) {
            mSensorManager.registerListener(this, mHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void checkErrorOfSensorNorFound() {
        //ERROR CHECK OF NO SENSOR, IF NO SENSOR SHOW ERROR TEXT
        String sensor_error = getResources().getString(R.string.error_no_sensor);
        if (mLightsSensor == null) { mTextLightsSensor.setText(sensor_error);
        }
        if (mProximitySensor == null) { mTextProximitySensor.setText(sensor_error);
        }
        if (mAmbientTemperatureSensor == null) { mTextAmbientTemperatureSensor.setText(sensor_error);
        }
        if (mMagneticFieldSensor == null) { mTextProximitySensor.setText(sensor_error);
        }
        if (mPressureSensor == null) { mTextPressureSensor.setText(sensor_error);
        }
        if (mHumiditySensor == null) { mTextHumiditySensor.setText(sensor_error);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        float[] mGravity;
        float[] mGeomagnetic;
    }

    public void goToList(View view) {
        //BUTTON TO SHOW LIST OF SENSORS
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ListOfSensorsFragment.newInstance())
                .commitNow();
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
    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }
}