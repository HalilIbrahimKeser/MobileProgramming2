package com.example.penduluswithsensors;
//https://github.com/google-developer-training/android-advanced/blob/master/SensorListeners/app/src/main/java/com/example/android/sensorlisteners/MainActivity.java
//Bygget på koden ovenfor fra github

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.penduluswithsensors.databinding.MainFragmentBinding;

import java.text.CollationElementIterator;

public class MainFragment extends Fragment implements SensorEventListener {
    private static final float ALPHA = (float) 0.25;
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
    public static int mCounter;
    public static float boundryLeftMain;
    public static float boundryRightMain;
    public static float addValueMain;
    public static float x_dirMain;
    protected static TextView mTextCounter;

    private TextView mTextValues, mTextName, mTextValues1, mTextName1, mTextValues2, mTextName2,
            mTextValues3, mTextName3, mTextValues4, mTextName4, mTextError;
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
        mCounter = 0;

        mTextName= binding.tvName;
        mTextValues = binding.tvValues;
        mTextName1= binding.tvName1;
        mTextValues1 = binding.tvValues1;
        mTextName2= binding.tvName2;
        mTextValues2 = binding.tvValues2;
        mTextName3= binding.tvName3;
        mTextValues3 = binding.tvValues3;
        mTextName4= binding.tvName4;
        mTextValues4 = binding.tvValues4;

        mTextError = binding.tvError;

        mTextCounter = binding.tvCounter;

        mButtonReset = binding.btnReset;
        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGameRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        checkErrorOfSensorNorFound();
    }

    public static void sendData(float boundryLeft, float boundryRight, float addValue, float x_dir) {
        boundryLeftMain = boundryLeft;
        boundryRightMain = boundryRight;
        addValueMain = addValue;
        x_dirMain = x_dir;
    }

    public static void updateCounter() {
        //mCounter++;
        mTextCounter.setText(String.valueOf(boundryLeftMain) + ", " + String.valueOf(boundryRightMain) +  ", " + String.valueOf(addValueMain)+ ", " + String.valueOf(x_dirMain));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = sensorEvent.values;
                updateOrientation(mGravity, mGeomagnetic);

                //Low pass filter
                float alpha = (float) 0.25;
                mGeomagnetic[0] = alpha * sensorEvent.values[0] + (1 - alpha) * sensorEvent.values[0];
                mGeomagnetic[1] = alpha * sensorEvent.values[1] + (1 - alpha) * sensorEvent.values[1];
                mGeomagnetic[2] = alpha * sensorEvent.values[2] + (1 - alpha) * sensorEvent.values[2];

                //Update
                mTextName.setText(sensorEvent.sensor.getName());
                mTextValues.setText("North: " + mGeomagnetic[0] + "\n East: " + mGeomagnetic[1] + " \n Up: " + mGeomagnetic[2]);
                PendulumView.sendMagneticFieldData(sensorEvent.values);
                break;

            case Sensor.TYPE_ACCELEROMETER:
                mGravity = sensorEvent.values;
                updateOrientation(mGravity, mGeomagnetic);

                mTextName1.setText(sensorEvent.sensor.getName());
                mTextValues1.setText("X: " + sensorEvent.values[0] + " m/s2,  \nY: " + sensorEvent.values[1] + " m/s2,  \nZ: " + sensorEvent.values[2] + " m/s2");
                PendulumView.sendAccelerometerData(sensorEvent.values);
                break;

            case Sensor.TYPE_GYROSCOPE:
                mTextName2.setText(sensorEvent.sensor.getName());
                mTextValues2.setText("X: " + sensorEvent.values[0] + "\nY: " + sensorEvent.values[1] + "\nZ: " + sensorEvent.values[2]);
                PendulumView.sendGyroscopeData(sensorEvent.values);
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                float[] gameRotation = new float[4];
                gameRotation = lowPass(sensorEvent.values.clone(), gameRotation);

                //Update
                mTextName3.setText(sensorEvent.sensor.getName());
                mTextValues3.setText("1: " + gameRotation[0] + ",   \n2: " + gameRotation[1] + ",   " +
                                "\n3: " + gameRotation[2] + " ,   \n4: " + gameRotation[3] + "\n");
                PendulumView.sendGameRotationData(gameRotation);
                break;
            default:
                // do nothing
        }
    }

    protected float[] lowPass( float[] input, float[] output ) {
        //https://github.com/Bhide/Low-Pass-Filter-To-Android-Sensors
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
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

                mTextName4.setText("Orientation");
                mTextValues4.setText("Azimuth: " + orientation[0] + ", \nPitch: " + orientation[1] + ", \nRoll: " + orientation[2] + " \n--------------");
                PendulumView.sendOrientationData(orientation);
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