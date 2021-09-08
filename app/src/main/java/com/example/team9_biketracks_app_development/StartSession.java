package com.example.team9_biketracks_app_development;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class StartSession extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "StartSession";

    private SensorManager sensorManager;
    private Sensor accelerometer, mGyro, mMagno;
    EditText xValue, yValue, zValue, xGValue, yGValue, zGValue, xMValue, yMValue, zMValue;
    float xAccValue, yAccValue, zAccValue, xGyroValue, yGyroValue, zGyroValue, xMagValue, yMagValue, zMagValue;

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

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //set Sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //Output Sensor Data
        if (accelerometer != null) {
            sensorManager.registerListener(StartSession.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer Listener");
        } else {
            Log.d(TAG, "Accelerometer Not Supported");
        }

        if (mGyro != null) {
            sensorManager.registerListener(StartSession.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyro Listener");
        } else {
            Log.d(TAG, "Gyro not supported");
        }

        if (mMagno != null) {
            sensorManager.registerListener(StartSession.this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Magno Listener");
        } else {
            Log.d(TAG, "Magno Not Supported");
        }
    }

    public void startChronometer(View v) throws IOException {
        if (!active){
            mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            mChronometer.start();
            active = true;
        }
    }

    public void pauseChronometer(View v) throws IOException {
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
            xAccValue = sensorEvent.values[0];
            yAccValue = sensorEvent.values[1];
            zAccValue = sensorEvent.values[2];
            xValue.setText(String.valueOf(sensorEvent.values[0]));
            yValue.setText(String.valueOf(sensorEvent.values[1]));
            zValue.setText(String.valueOf(sensorEvent.values[2]));
            myDb.insertAccelerometerData(xAccValue, yAccValue, zAccValue);
        } if(sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d(TAG, "GYROSCOPE onSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);
            xGyroValue = sensorEvent.values[0];
            yGyroValue = sensorEvent.values[1];
            zGyroValue = sensorEvent.values[2];
            xGValue.setText(String.valueOf(sensorEvent.values[0]));
            yGValue.setText(String.valueOf(sensorEvent.values[1]));
            zGValue.setText(String.valueOf(sensorEvent.values[2]));
            myDb.insertGyroscopeData(xGyroValue, yGyroValue, zGyroValue);
        } if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.d(TAG, "MAGNETIC FIELD onSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);
            xMagValue = sensorEvent.values[0];
            yMagValue = sensorEvent.values[1];
            zMagValue = sensorEvent.values[2];
            xMValue.setText(String.valueOf(sensorEvent.values[0]));
            yMValue.setText(String.valueOf(sensorEvent.values[1]));
            zMValue.setText(String.valueOf(sensorEvent.values[2]));
            myDb.insertMagnetometerData(xMagValue, yMagValue, zMagValue);
        }
    }

    public void toggle(View v) {
        v.setEnabled(false);
        Log.d("success", "Button Disabled");
    }
}
