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
import android.widget.Toast;

import com.example.oblig1_sensors.R;
import com.example.oblig1_sensors.databinding.MainFragmentBinding;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment implements SensorEventListener {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    Context context;

    private SensorManager mSensorManager;

    private static final String FILE_NAME = "sensorsLog.txt";

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
    // disse tester jeg ved annen anleding
    private Sensor mGameRotationVectorSensor;
    private Sensor mGeoMagRotationVectorSensor;
    private Sensor mGravitySensor;

    // For computing the orientation
    float[] mGravity;
    float[] mGeomagnetic;


    private TextView mTextLightsSensor, mTextProximitySensor, mTextAmbientTemperatureSensor,
            mTextMagneticFieldSensor, mTextPressureSensor, mTextHumiditySensor,
            mTextOrientationSensor, mTextAccelerometerSensor, mTextGyroscopeSensor;

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

        mTextLightsSensor = binding.tvLight;
        mTextProximitySensor = binding.tvProximity;
        mTextAmbientTemperatureSensor = binding.tvTemperaure;
        mTextMagneticFieldSensor = binding.tvMagneticField;
        mTextPressureSensor = binding.tvPressure;
        mTextHumiditySensor = binding.tvHummidity;
        mTextOrientationSensor = binding.tvOrientation;
        mTextAccelerometerSensor = binding.tvAccelorometer;
        mTextGyroscopeSensor = binding.tvGyroscope;

        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        //BASE SENSORS
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLightsSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAmbientTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mHumiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //COMPOSITE SENSORS
        //mOrientationSensor gets calculated by data from Magnetic field and Accelerometer in the method updateOrientation()
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        checkErrorOfSensorNorFound();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        String sensorResult = sensorEvent.sensor.getName() + ": " + sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                mTextLightsSensor.setText(sensorResult + " lux");
                writeToFile(sensorResult);
                break;

            case Sensor.TYPE_PROXIMITY:
                mTextProximitySensor.setText(sensorResult + " cm");
                writeToFile(sensorResult);
                break;

            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                mTextAmbientTemperatureSensor.setText(sensorResult + " C°");
                writeToFile(sensorResult);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                String sensorResultMagneticFiels = sensorEvent.sensor.getName() + ": \n" + "North: " +
                        sensorEvent.values[0] + ",  East: " + sensorEvent.values[1] + ",  Up: " + sensorEvent.values[2];
                mTextMagneticFieldSensor.setText(sensorResultMagneticFiels);
                mGeomagnetic = sensorEvent.values;
                updateOrientation(mGravity, mGeomagnetic);
                writeToFile(sensorResultMagneticFiels);
                break;

            case Sensor.TYPE_PRESSURE:
                mTextPressureSensor.setText(sensorResult + " hPa");
                writeToFile(sensorResult);
                break;

            case Sensor.TYPE_RELATIVE_HUMIDITY:
                mTextHumiditySensor.setText(sensorResult + " %");
                writeToFile(sensorResult);
                break;

            case Sensor.TYPE_ACCELEROMETER:
                String sensorResultAccelerometer = sensorEvent.sensor.getName() + ": \n" + "X: " +
                        sensorEvent.values[0] + " m/s2,  Y: " + sensorEvent.values[1] + " m/s2,  Z: " + sensorEvent.values[2] + " m/s2";
                mTextAccelerometerSensor.setText(sensorResultAccelerometer);
                mGravity = sensorEvent.values;
                updateOrientation(mGravity, mGeomagnetic);
                writeToFile(sensorResult);
                break;

            case Sensor.TYPE_GYROSCOPE:
                String sensorGyroscope = sensorEvent.sensor.getName() + ": \n" + "X: " +
                        sensorEvent.values[0] + ",   Y: " + sensorEvent.values[1] + ",   Z: " + sensorEvent.values[2] + " ";
                mTextGyroscopeSensor.setText(sensorGyroscope);
                writeToFile(sensorResult);
                break;
            default:
                // do nothing
        }
    }

    private void updateOrientation(float[] mGravity, float[] mGeomagnetic) {
        // Orientation coputed by accelorometer and magnetic field, since TYPE_ORIENTATION is deprecated.
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
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mGyroscopeSensor != null) {
            mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
    }

    public void writeToFile(String textToWrite) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = requireActivity().openFileOutput(FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(textToWrite.getBytes());
            fileOutputStream.write("\n".getBytes());


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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