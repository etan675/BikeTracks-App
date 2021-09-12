package com.example.team9_biketracks_app_development;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

/** ActivityManager class. */
public class ActivityManager extends AppCompatActivity implements SensorEventListener {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FASTEST_UPDATE_INTERVAL= 5;
    /** Logger. */
    private static final String LOGGER = "StartSession";
    private static final int FINE_LOCATION_PERMISSION = 99 ;
    /** SessionManager instance. */
    private SensorManager sensorManager;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    /** Sensor fields. */
    EditText acc_x, acc_y, acc_z, gyro_x, gyro_y, gyro_z, mag_x, mag_y, mag_z, latitude, longitude, altitude;
    /** Store old sensor fields */
    float old_acc_x = 1.0f;
    float old_acc_y = 1.0f;
    float old_acc_z = 1.0f;
    float old_gyro_x = 1.0f;
    float old_gyro_y = 1.0f;
    float old_gyro_z = 1.0f;
    float old_mag_x = 1.0f;
    float old_mag_y = 1.0f;
    float old_mag_z = 1.0f;
    /** Chronometer. */
    private Chronometer mChronometer;
    /** Offset. */
    private long pauseOffset;
    /** active. */
    private boolean active;
    /** SensorDatabase instance. */
    private SensorDatabase sensorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sensorDatabase = new SensorDatabase(this);
        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setFormat("Time: %s");
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setOnChronometerTickListener(chronometer -> {
            if ((SystemClock.elapsedRealtime() - mChronometer.getBase()) >= 10000){
                mChronometer.setBase(SystemClock.elapsedRealtime());
                Toast.makeText(ActivityManager.this, "Bing!", Toast.LENGTH_SHORT).show();
            }
        });
        acc_x = findViewById(R.id.xValue);
        acc_y = findViewById(R.id.yValue);
        acc_z = findViewById(R.id.zValue);
        gyro_x = findViewById(R.id.xGValue);
        gyro_y = findViewById(R.id.yGValue);
        gyro_z = findViewById(R.id.zGValue);
        mag_x = findViewById(R.id.xMValue);
        mag_y = findViewById(R.id.yMValue);
        mag_z = findViewById(R.id.zMValue);
        latitude = findViewById(R.id.latitudeValue);
        longitude = findViewById(R.id.longitudeValue);
        altitude = findViewById(R.id.altitudeValue);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        locationRequest = LocationRequest.create();

        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);

        locationRequest.setFastestInterval(1000 * FASTEST_UPDATE_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // register sensors
        if (accelerometer != null) {
            sensorManager.registerListener(ActivityManager.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(ActivityManager.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetometer != null) {
            sensorManager.registerListener(ActivityManager.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Button finishSessionButton = findViewById(R.id.finishSessionButton);
        finishSessionButton.setOnClickListener(view -> {
            sensorManager.unregisterListener(this);
            finish();
        });
        updateGPS();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FINE_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "This app requires location permission to be granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateGPS() {
        //get permissions to track GPS
        //get location from fused client provider
        //update UI
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ActivityManager.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>(){
                @Override
                public void onSuccess(Location location) {
                    // got permissions, need to put values into UI
                    updateUIValues(location);
                }
            });
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
            }
        }
    }

    private void updateUIValues(Location location) {
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
        if (location.hasAltitude()) {
            altitude.setText(String.valueOf(location.getAltitude()));
        }
        else{
            altitude.setText("Not available");
        }
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
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(LOGGER, "Accelerometer: x: " + sensorEvent.values[0] + ", y: " + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
            acc_x.setText(String.valueOf(sensorEvent.values[0]));
            acc_y.setText(String.valueOf(sensorEvent.values[1]));
            acc_z.setText(String.valueOf(sensorEvent.values[2]));
            if ((old_acc_x != sensorEvent.values[0]) || (old_acc_y != sensorEvent.values[1]) || (old_acc_z != sensorEvent.values[2])){
                sensorDatabase.insertAccelerometerData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                old_acc_x = sensorEvent.values[0];
                old_acc_y = sensorEvent.values[1];
                old_acc_z = sensorEvent.values[2];
            }
        }
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d(LOGGER, "Gyroscope: x: " + sensorEvent.values[0] + ", y: " + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
            gyro_x.setText(String.valueOf(sensorEvent.values[0]));
            gyro_y.setText(String.valueOf(sensorEvent.values[1]));
            gyro_z.setText(String.valueOf(sensorEvent.values[2]));
            if ((old_gyro_x != sensorEvent.values[0]) || (old_gyro_y != sensorEvent.values[1]) || (old_gyro_z != sensorEvent.values[2])){
                sensorDatabase.insertGyroscopeData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                old_gyro_x = sensorEvent.values[0];
                old_gyro_y = sensorEvent.values[1];
                old_gyro_z = sensorEvent.values[2];
            }
        }
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.d(LOGGER, "Magnetometer: x: " + sensorEvent.values[0] + ", y: " + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
            mag_x.setText(String.valueOf(sensorEvent.values[0]));
            mag_y.setText(String.valueOf(sensorEvent.values[1]));
            mag_z.setText(String.valueOf(sensorEvent.values[2]));
            if ((old_mag_x != sensorEvent.values[0]) || (old_mag_y != sensorEvent.values[1]) || (old_mag_z != sensorEvent.values[2])){
                sensorDatabase.insertMagnetometerData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                old_mag_x = sensorEvent.values[0];
                old_mag_y = sensorEvent.values[1];
                old_mag_z = sensorEvent.values[2];
            }
        }
    }
}
