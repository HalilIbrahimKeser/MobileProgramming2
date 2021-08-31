package com.example.a2_sensors;


import android.os.Bundle;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    // System sensor manager instance.
    private SensorManager mSensorManager;

    // Proximity and light sensors, as retrieved from the sensor manager.
    private Sensor mProximitySensor;
    private Sensor mLightsSensor;

    private Sensor m3AxisAccelorometerSensor;
    private Sensor m3axisGyroscopeSensor;
    private Sensor m3axisMagneticFieldSensor;
    private Sensor mGoldfishOrientationSensor;
    private Sensor mAmbientTemperatureSensor;

    private Sensor mPressureSensor;
    private Sensor mHumiditySensor;
    private Sensor mHingeSensor;
    private Sensor mGameRotationVectorSensor;
    private Sensor mGeoMagRotationVectorSensor;
    private Sensor mGravitySensor;
    private Sensor mLinearAccelerationSensor;
    private Sensor mRotationVectorSensor;
    private Sensor mOrientationSensor;

    // TextViews to display current sensor values.
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all view variables.
        mTextSensorLight = (TextView) findViewById(R.id.label_light);
        mTextSensorProximity = (TextView) findViewById(R.id.label_proximity);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

        // Get light and proximity sensors from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mProximitySensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_PROXIMITY);
        mLightsSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If either mSensorLight or mSensorProximity are null, those sensors
        // are not available in the device.  Set the text to the error message
        if (mLightsSensor == null) { mTextSensorLight.setText(sensor_error); }
        if (mProximitySensor == null) {
            mTextSensorProximity.setText(sensor_error);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listeners for the sensors are registered in this callback and
        // can be unregistered in onPause().
        //
        // Check to ensure sensors are available before registering listeners.
        // Both listeners are registered with a "normal" amount of delay
        // (SENSOR_DELAY_NORMAL)
        if (mProximitySensor != null) {
            mSensorManager.registerListener(this, mProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mLightsSensor != null) {
            mSensorManager.registerListener(this, mLightsSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is paused.
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // The sensor type (as defined in the Sensor class).
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
                        R.string.label_proximity, currentValue));
                break;
            default:
                // do nothing
        }
    }

    /**
     * Abstract method in SensorEventListener.  It must be implemented, but is
     * unused in this app.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}