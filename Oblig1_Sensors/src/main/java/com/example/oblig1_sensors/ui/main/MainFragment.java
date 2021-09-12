package com.example.oblig1_sensors.ui.main;
//https://github.com/google-developer-training/android-advanced/blob/master/SensorListeners/app/src/main/java/com/example/android/sensorlisteners/MainActivity.java
//Bygget på koden ovenfor fra github

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oblig1_sensors.R;
import com.example.oblig1_sensors.databinding.MainFragmentBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainFragment extends Fragment implements SensorEventListener {

    Context context;
    private MainFragmentBinding binding;

    private static final String FILE_NAME = "sensorsLog.txt";
    File file;
    FileWriter stream;

    private SensorManager mSensorManager;

    // BASE SENSORS ------------------------
    private Sensor mLightsSensor;
    private Sensor mProximitySensor;
    private Sensor mAmbientTemperatureSensor;
    private Sensor mMagneticFieldSensor;
    private Sensor mPressureSensor;
    private Sensor mHumiditySensor;
    private Sensor mAccelerometerSensor;
    // disse tester jeg ved annen anleding
    private Sensor mLinearAccelerationSensor;
    private Sensor mGoldfishOrientationSensor;
    private Sensor mRotationVectorSensor;

    // COMPOSITE SENSORS ------------------
    private Sensor mOrientationSensor;
    private Sensor mGyroscopeSensor;
    private Sensor mGameRotationVectorSensor;
    // disse tester jeg ved annen anleding
    private Sensor mGeoMagRotationVectorSensor;
    private Sensor mGravitySensor;

    // For computing the orientation
    float[] mGravity;
    float[] mGeomagnetic;

    private TextView mTextLightsSensor, mTextProximitySensor, mTextAmbientTemperatureSensor,
            mTextMagneticFieldSensor, mTextPressureSensor, mTextHumiditySensor,
            mTextOrientationSensor, mTextAccelerometerSensor, mTextGyroscopeSensor,
            mTextGameRotatiomVectorSensor;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch mSwLight, mSwProximity, mSwTemperature,
            mSwMagneticField, mSwPressure, mSwHumidity,
            mSwOrientation, mSwAccelerometer,mSwGyroscope, mSwGameRotation;


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

        context = view.getContext();

        File path = context.getFilesDir();
        file = new File(path, FILE_NAME);

        mTextLightsSensor = binding.tvLight;
        mTextProximitySensor = binding.tvProximity;
        mTextAmbientTemperatureSensor = binding.tvTemperaure;
        mTextMagneticFieldSensor = binding.tvMagneticField;
        mTextPressureSensor = binding.tvPressure;
        mTextHumiditySensor = binding.tvHummidity;
        mTextOrientationSensor = binding.tvOrientation;
        mTextAccelerometerSensor = binding.tvAccelorometer;
        mTextGyroscopeSensor = binding.tvGyroscope;
        mTextGameRotatiomVectorSensor = binding.tvGameRotation;

        mSwLight = binding.swLight;
        mSwProximity = binding.swProximity;
        mSwTemperature = binding.swTemperature;
        mSwMagneticField = binding.swMagneticField;
        mSwPressure = binding.swPressure;
        mSwHumidity = binding.swHummidity;
        mSwOrientation = binding.swOrientation;
        mSwAccelerometer = binding.swAccelerometer;
        mSwGyroscope = binding.swGyroscope;
        mSwGameRotation = binding.swGameVector;

        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        //BASE SENSORS
        mLightsSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAmbientTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mHumiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //COMPOSITE SENSORS
        //mOrientationSensor gets calculated by data from Magnetic field and Accelerometer in the method updateOrientation()
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGameRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        checkErrorOfSensorNorFound();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        String sensorResult = sensorEvent.sensor.getName() + ": " + sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                if (mSwLight.isChecked()) {
                    mTextLightsSensor.setText(sensorResult + " lux");
                    writeToFile(sensorResult);
                }
                break;

            case Sensor.TYPE_PROXIMITY:
                if (mSwProximity.isChecked()) {
                    mTextProximitySensor.setText(sensorResult + " cm");
                    writeToFile(sensorResult);
                }
                break;

            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                if (mSwTemperature.isChecked()) {
                    mTextAmbientTemperatureSensor.setText(sensorResult + " C°");
                    writeToFile(sensorResult);
                }
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                if (mSwMagneticField.isChecked()) {
                    String sensorResultMagneticFiels = sensorEvent.sensor.getName() + ": \n" + "North: " +
                            sensorEvent.values[0] + ",  East: " + sensorEvent.values[1] + ",  Up: " + sensorEvent.values[2];
                    mTextMagneticFieldSensor.setText(sensorResultMagneticFiels);
                    mGeomagnetic = sensorEvent.values;
                    updateOrientation(mGravity, mGeomagnetic);
                    writeToFile(sensorResultMagneticFiels);
                }
                break;

            case Sensor.TYPE_PRESSURE:
                if (mSwPressure.isChecked()) {
                    mTextPressureSensor.setText(sensorResult + " hPa");
                    writeToFile(sensorResult);
                }
                break;

            case Sensor.TYPE_RELATIVE_HUMIDITY:
                if (mSwHumidity.isChecked()) {
                    mTextHumiditySensor.setText(sensorResult + " %");
                    writeToFile(sensorResult);
                }
                break;

            case Sensor.TYPE_ACCELEROMETER:
                if (mSwAccelerometer.isChecked()) {
                    String sensorResultAccelerometer = sensorEvent.sensor.getName() + ": \n" + "X: " +
                            sensorEvent.values[0] + " m/s2,  Y: " + sensorEvent.values[1] + " m/s2,  Z: " + sensorEvent.values[2] + " m/s2";
                    mTextAccelerometerSensor.setText(sensorResultAccelerometer);
                    mGravity = sensorEvent.values;
                    updateOrientation(mGravity, mGeomagnetic);
                    writeToFile(sensorResult);
                }
                break;

            case Sensor.TYPE_GYROSCOPE:
                if (mSwGyroscope.isChecked()) {
                    String sensorGyroscope = sensorEvent.sensor.getName() + ": \n" + "X: " +
                            sensorEvent.values[0] + ",   Y: " + sensorEvent.values[1] + ",   Z: " + sensorEvent.values[2] + " ";
                    mTextGyroscopeSensor.setText(sensorGyroscope);
                    writeToFile(sensorResult);
                }
                break;

            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                if (mSwGameRotation.isChecked()) {
                    String stringSensorGameRotation = sensorEvent.sensor.getName() + ": \n" + "1: " +
                            sensorEvent.values[0] + ",   2: " + sensorEvent.values[1] + ",   3: " + sensorEvent.values[2] + " ,   4: " + sensorEvent.values[3] ;
                    mTextGameRotatiomVectorSensor.setText(stringSensorGameRotation);
                    writeToFile(sensorResult);
                }
                break;
            default:
                // do nothing
        }
    }

    private void updateOrientation(float[] mGravity, float[] mGeomagnetic) {
        // Orientation coputed by accelorometer and magnetic field, since TYPE_ORIENTATION is deprecated.
        if(mSwOrientation.isChecked()) {
            if (mGravity != null && mGeomagnetic != null) {
                float[] R = new float[9];
                float[] I = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float[] orientation = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    // orientation contains: azimut, pitch and roll
                    mTextOrientationSensor.setText("Azimuth: " + orientation[0] + ", Pitch: " + orientation[1] + ", Roll: " + orientation[2]);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //LISTENERS OF SENSORS
        registerListenerSensors();
    }

    private void registerListenerSensors() {
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
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mGyroscopeSensor != null) {
            mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mGameRotationVectorSensor != null) {
            mSensorManager.registerListener(this, mGameRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void checkErrorOfSensorNorFound() {
        //ERROR CHECK OF NO SENSOR, IF NO SENSOR SHOW ERROR TEXT
        String sensor_error = getResources().getString(R.string.error_no_sensor);
        if (mLightsSensor == null) {
            mTextLightsSensor.setText(sensor_error);
        }
        if (mProximitySensor == null) {
            mTextProximitySensor.setText(sensor_error);
        }
        if (mAmbientTemperatureSensor == null) {
            mTextAmbientTemperatureSensor.setText(sensor_error);
        }
        if (mMagneticFieldSensor == null) {
            mTextProximitySensor.setText(sensor_error);
        }
        if (mPressureSensor == null) {
            mTextPressureSensor.setText(sensor_error);
        }
        if (mHumiditySensor == null) {
            mTextHumiditySensor.setText(sensor_error);
        }
        if (mAccelerometerSensor == null) {
            mTextAccelerometerSensor.setText(sensor_error);
        }
        if (mGyroscopeSensor == null) {
            mTextGyroscopeSensor.setText(sensor_error);
        }
        if (mGameRotationVectorSensor == null) {
            mTextGameRotatiomVectorSensor.setText(sensor_error);
        }
    }

    public void writeToFile(String textToWrite) {
        try {
            stream = new FileWriter(file, true);
            stream.write(textToWrite);
            stream.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
        registerListenerSensors();
    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);

        //Slett innhold i fil, styres ikke fra programmet
        boolean slettInnholdIFil = false;
        if (slettInnholdIFil) {
            try {
                stream = new FileWriter(file, false );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                stream.write("Slettett av programmet");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}