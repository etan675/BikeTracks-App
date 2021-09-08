package com.example.team9_biketracks_app_development;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


/** ActivityManager class. */
public class ActivityManager extends AppCompatActivity implements SensorEventListener {
    /** SessionManager instance. */
    private SensorManager sensorManager;
    /** Sensor fields. */
    EditText acc_x, acc_y, acc_z, gyro_x, gyro_y, gyro_z, mag_x, mag_y, mag_z;
    /** Chronometer. */
    private Chronometer mChronometer;
    /** Offset. */
    private long pauseOffset;
    /** active. */
    private boolean active;
    /** Finish session button. */
    Button finishSessionButton;
    /** SensorDatabase instance. */
    SensorDatabase sensorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sensorDatabase = new SensorDatabase(this);
        mChronometer= (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setFormat("Time: %s");
        mChronometer.setBase(SystemClock.elapsedRealtime());

        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - mChronometer.getBase()) >= 10000){
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(ActivityManager.this, "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        acc_x = (EditText) findViewById(R.id.xValue);
        acc_y = (EditText) findViewById(R.id.yValue);
        acc_z = (EditText) findViewById(R.id.zValue);
        gyro_x = (EditText) findViewById(R.id.xGValue);
        gyro_y = (EditText) findViewById(R.id.yGValue);
        gyro_z = (EditText) findViewById(R.id.zGValue);
        mag_x = (EditText) findViewById(R.id.xMValue);
        mag_y = (EditText) findViewById(R.id.yMValue);
        mag_z = (EditText) findViewById(R.id.zMValue);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometer, gyroscope, magnetometer;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // Register available sensors
        if (accelerometer != null) {
            sensorManager.registerListener(ActivityManager.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(ActivityManager.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetometer != null) {
            sensorManager.registerListener(ActivityManager.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // finish Session Button
        finishSessionButton = (Button) findViewById(R.id.finishSessionButton);
        finishSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endSensor();
                finish();
            }
        });
    }

    public void startChronometer(View v) throws IOException {
        if (!active) {
            mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            mChronometer.start();
            active = true;
        }
    }

    public void pauseChronometer(View v) throws IOException {
        if (active) {
            mChronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
            active = false;
        }
    }

    public void stopChronometer(View v) {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //Get Sensor Data When Changed
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acc_x.setText(String.valueOf(sensorEvent.values[0]));
            acc_y.setText(String.valueOf(sensorEvent.values[1]));
            acc_z.setText(String.valueOf(sensorEvent.values[2]));
            sensorDatabase.insertAccelerometerData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
        if(sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyro_x.setText(String.valueOf(sensorEvent.values[0]));
            gyro_y.setText(String.valueOf(sensorEvent.values[1]));
            gyro_z.setText(String.valueOf(sensorEvent.values[2]));
            sensorDatabase.insertGyroscopeData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
        if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mag_x.setText(String.valueOf(sensorEvent.values[0]));
            mag_y.setText(String.valueOf(sensorEvent.values[1]));
            mag_z.setText(String.valueOf(sensorEvent.values[2]));
            sensorDatabase.insertMagnetometerData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
    }

    public void endSensor() {
        sensorManager.unregisterListener(this);
    }
}
