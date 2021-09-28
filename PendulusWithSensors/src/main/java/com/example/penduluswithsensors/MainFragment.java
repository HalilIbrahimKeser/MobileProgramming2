package com.example.penduluswithsensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.penduluswithsensors.databinding.MainFragmentBinding;

public class MainFragment extends Fragment implements SensorEventListener {
    Context context;
    private MainFragmentBinding binding;

    private SensorManager mSensorManager;
    private Sensor mMagneticFieldSensor;
    private Sensor mAccelerometerSensor;
    private Sensor mLinearAccelerationSensor;
    private Sensor mRotationVectorSensor;

    private Sensor mOrientationSensor;
    private Sensor mGyroscopeSensor;
    private Sensor mGameRotationVectorSensor;

    float[] mGravity;
    float[] mGeomagnetic;

    private TextView mTextValues;
    private TextView mTextError;
    private Button mButtonReset;

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

        mTextValues = binding.tvValues;
        mTextError = binding.tvError;
        mButtonReset = binding.btnReset;
        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        //BASE SENSORS
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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
        StringBuilder stringBuilder = new StringBuilder();

        switch (sensorType) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                String sensorResultMagneticFiels = sensorEvent.sensor.getName() + sensorEvent.sensor.getType() + ": \n" + "North: " +
                            sensorEvent.values[0] + ",  East: " + sensorEvent.values[1] + ",  Up: " + sensorEvent.values[2];
                stringBuilder.append(sensorResultMagneticFiels);
                mGeomagnetic = sensorEvent.values;
                updateOrientation(mGravity, mGeomagnetic);
                PendulumView.sendData(sensorEvent.values);
                break;

            case Sensor.TYPE_ACCELEROMETER:
                String sensorResultAccelerometer = sensorEvent.sensor.getName() +  + sensorEvent.sensor.getType() + ": \n" + "X: " +
                            sensorEvent.values[0] + " m/s2,  Y: " + sensorEvent.values[1] + " m/s2,  Z: " + sensorEvent.values[2] + " m/s2";
                stringBuilder.append(sensorResultAccelerometer);
                mGravity = sensorEvent.values;
                updateOrientation(mGravity, mGeomagnetic);
                PendulumView.sendData(sensorEvent.values);
                break;

            case Sensor.TYPE_GYROSCOPE:
                String sensorGyroscope = sensorEvent.sensor.getName() + sensorEvent.sensor.getType() + ": \n" + "X: " +
                            sensorEvent.values[0] + ",   Y: " + sensorEvent.values[1] + ",   Z: " + sensorEvent.values[2] + " ";
                stringBuilder.append(sensorGyroscope);
                PendulumView.sendData(sensorEvent.values);
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                String stringSensorGameRotation = sensorEvent.sensor.getName() + sensorEvent.sensor.getType() + ": \n" + "1: " +
                        sensorEvent.values[0] + ",   2: " + sensorEvent.values[1] + ",   3: " + sensorEvent.values[2] + " ,   4: " + sensorEvent.values[3] ;
                stringBuilder.append(stringSensorGameRotation);
                PendulumView.sendGyroscopeData(sensorEvent.values);
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

                // orientation contains: azimuth, pitch and roll
                //mTextOrientationSensor.setText("Azimuth: " + orientation[0] + ", Pitch: " + orientation[1] + ", Roll: " + orientation[2]);
                PendulumView.sendData(orientation);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onStart() {
        super.onStart();
        //LISTENERS OF SENSORS
        registerListenerSensors();
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
    }

    private void registerListenerSensors() {
        //LISTENERS OF SENSORS
        if (mMagneticFieldSensor != null) {
            mSensorManager.registerListener(this, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

        if (mMagneticFieldSensor == null) {
            mTextError.setText(sensor_error);
        }
        if (mAccelerometerSensor == null) {
            mTextError.setText(sensor_error);
        }
        if (mGyroscopeSensor == null) {
            mTextError.setText(sensor_error);
        }
        if (mGameRotationVectorSensor == null) {
            mTextError.setText(sensor_error);
        }
    }
}