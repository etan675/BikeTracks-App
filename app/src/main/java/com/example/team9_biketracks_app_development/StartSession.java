package com.example.team9_biketracks_app_development;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileWriter;

public class StartSession extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "StartSession";

    private SensorManager sensorManager;
    private Sensor accelerometer, mGyro, mMagno, mLight, mPressure, mTemp, mHumidity;
    EditText xValue, yValue, zValue, xGValue, yGValue, zGValue, xMValue, yMValue, zMValue, light, pressure, temp, humidity;

    private Chronometer mChronometer;
    private long pauseOffset;
    boolean active;

    DBHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Initialising Database
        myDb = new DBHelper(this);

        mChronometer= (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setFormat("Time: %s");
        mChronometer.setBase(SystemClock.elapsedRealtime());

        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - mChronometer.getBase()) >= 10000){
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(StartSession.this, "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Setting Acc values
        xValue = (EditText) findViewById(R.id.xValue);
        yValue = (EditText) findViewById(R.id.yValue);
        zValue = (EditText) findViewById(R.id.zValue);

        //Setting Gyro values
        xGValue = (EditText) findViewById(R.id.xGValue);
        yGValue = (EditText) findViewById(R.id.yGValue);
        zGValue = (EditText) findViewById(R.id.zGValue);

        //Setting Magno values
        xMValue = (EditText) findViewById(R.id.xMValue);
        yMValue = (EditText) findViewById(R.id.yMValue);
        zMValue = (EditText) findViewById(R.id.zMValue);

        //setting other values
        light = (EditText) findViewById(R.id.light);
        pressure = (EditText) findViewById(R.id.pressure);
        temp = (EditText) findViewById(R.id.temp);
        humidity = (EditText) findViewById(R.id.humidity);

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //set Sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mPressure =  sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        //Output Sensor Data
        if (accelerometer != null) {
            sensorManager.registerListener(StartSession.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer Listener");
        } else {
            Log.d(TAG, "Accelerometer Not Supported");
            xValue.setText("Accelerometer Not Supported");
            yValue.setText("Accelerometer Not Supported");
            zValue.setText("Accelerometer Not Supported");
        }

        if (mGyro != null) {
            sensorManager.registerListener(StartSession.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyro Listener");
        } else {
            Log.d(TAG, "Gyro not supported");
            xGValue.setText("Gyroscope Not Supported");
            yGValue.setText("Gyroscope Not Supported");
            zGValue.setText("Gyroscope Not Supported");
        }

        if (mMagno != null) {
            sensorManager.registerListener(StartSession.this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Magno Listener");
        } else {
            Log.d(TAG, "Magno Not Supported");
            xMValue.setText("Magno Not Supported");
            yMValue.setText("Magno Not Supported");
            zMValue.setText("Magno Not Supported");
        }

        if (mLight != null) {
            sensorManager.registerListener(StartSession.this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered LightSensor Listener");
        } else {
            Log.d(TAG, "Light Sensor Not Supported");
            light.setText("Light Sensor Not Supported");
        }

        if (mPressure != null) {
            sensorManager.registerListener(StartSession.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered PressureSensor Listener");
        } else {
            Log.d(TAG, "Pressure Sensor Not Supported");
            pressure.setText("Pressure Sensor Not Supported");
        }

        if (mTemp != null) {
            sensorManager.registerListener(StartSession.this, mTemp, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered TempSensor Listener");
        } else {
            Log.d(TAG, "Temp Sensor Not Supported");
            temp.setText("Temp Sensor Not Supported");
        }

        if (mHumidity != null) {
            sensorManager.registerListener(StartSession.this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Humidity Sensor Listener");
        } else {
            Log.d(TAG, "Humidity Sensor Not Supported");
            humidity.setText("Humidity Sensor Not Supported");
        }
    }

    public void startChronometer(View v){
        if (!active){
            mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            mChronometer.start();
            active = true;
        }
    }

    public void pauseChronometer(View v){
        if (active){
            mChronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
            active = false;
        }
    }

    public void stopChronometer(View v){
        mChronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        Log.d(TAG, "Setting Output variables");
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "ACCELEROMETER onSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);
            xValue.setText(String.valueOf(sensorEvent.values[0]));
            yValue.setText(String.valueOf(sensorEvent.values[1]));
            zValue.setText(String.valueOf(sensorEvent.values[2]));
        } else if(sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d(TAG, "GYROSCOPE onSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);
            xGValue.setText("X: " + sensorEvent.values[0]);
            yGValue.setText("Y: " + sensorEvent.values[1]);
            zGValue.setText("Z: " + sensorEvent.values[2]);
        } else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.d(TAG, "MAGNETIC FIELD onSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);
            xMValue.setText("X: " + sensorEvent.values[0]);
            yMValue.setText("Y: " + sensorEvent.values[1]);
            zMValue.setText("Z: " + sensorEvent.values[2]);
        } else if(sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.d(TAG, "LIGHT onSensorChanged: X: " + sensorEvent.values[0]);
            light.setText("Light: " + sensorEvent.values[0]);
        } else if(sensor.getType() == Sensor.TYPE_PRESSURE) {
            Log.d(TAG, "PRESSURE onSensorChanged: X: " + sensorEvent.values[0]);
            pressure.setText("Pressure: " + sensorEvent.values[0]);
        } else if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            Log.d(TAG, "TEMPERATURE onSensorChanged: X: " + sensorEvent.values[0]);
            temp.setText("Temperature: " + sensorEvent.values[0]);
        } else if(sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            Log.d(TAG, "HUMIDITY onSensorChanged: X: " + sensorEvent.values[0]);
            humidity.setText("Humidity: " + sensorEvent.values[0]);
        }

/*
        myDb.insertSensors(Float.parseFloat(xValue.getText().toString()), Float.parseFloat(yValue.getText().toString()), Float.parseFloat(zValue.getText().toString()),
                Float.parseFloat(xGValue.getText().toString()), Float.parseFloat(yGValue.getText().toString()), Float.parseFloat(zGValue.getText().toString()),
                Float.parseFloat(xMValue.getText().toString()), Float.parseFloat(yMValue.getText().toString()), Float.parseFloat(zMValue.getText().toString()),
                Float.parseFloat(light.getText().toString()), Float.parseFloat(pressure.getText().toString()), Float.parseFloat(temp.getText().toString()),
                Float.parseFloat(humidity.getText().toString()));
*/

        myDb.insertAccSensors(Float.parseFloat(xValue.getText().toString()), Float.parseFloat(yValue.getText().toString()), Float.parseFloat(zValue.getText().toString()));
    }

    public void toggle(View v) {
        v.setEnabled(false);
        Log.d("success", "Button Disabled");
    }

}

