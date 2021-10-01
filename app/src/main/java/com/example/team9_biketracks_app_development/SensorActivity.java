package com.example.team9_biketracks_app_development;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team9_biketracks_app_development.fragment.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    /** Logger. */
    private static final String LOGGER = "StartSession";
    /** Permission code for FINE_LOCATION. */
    private static final int FINE_LOCATION_PERMISSION = 99 ;
    /** GPS update settings. */
    public static final int DEFAULT_UPDATE_INTERVAL = 10;
    public static final int FASTEST_UPDATE_INTERVAL= 5;
    /** SensorManager instance. */
    private SensorManager sensorManager;
    /** Provider of GPS data. */
    FusedLocationProviderClient fusedLocationProviderClient;
    /** LocationRequest instance. */
    LocationRequest locationRequest;
    /** LocationCallback instance */
    LocationCallback locationCallBack;
    /** Store old gps values */
    float gps_alt = 0.0f;
    float gps_long = 0.0f;
    float gps_lat = 0.0f;
    /** Sensor fields. */
    EditText acc_x, acc_y, acc_z, gyro_x, gyro_y, gyro_z, mag_x, mag_y, mag_z, latitude, longitude, altitude;
    Switch gpsWifiSwitch;
    TextView sensorLabel;

    /** Chronometer. */
    private Chronometer mChronometer;
    /** SensorDatabase instance. */
    private SensorDatabase sensorDatabase;
    /** Last read time of each sensor. */
    private LocalDateTime accelerometerReadTime, gyroscopeReadTime, magnetometerReadTime;
    /** Limit of how often database is written to in seconds. */
    private final int writeLimit = 1;
    String activityType = HomeFragment.transport;

    LocalTime startTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startTime = java.time.LocalTime.now();
        sensorDatabase = new SensorDatabase(this);
        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setFormat("Time: %s");
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();

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
        sensorLabel = findViewById(R.id.sensorLabel);
        gpsWifiSwitch = findViewById(R.id.gpsWifiSwitch);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FASTEST_UPDATE_INTERVAL);
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastLocation = locationResult.getLastLocation();
                updateUIValues(lastLocation);
            }
        };

        gpsWifiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsWifiSwitch.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    sensorLabel.setText("High Power Usage");
                }
                else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                    sensorLabel.setText("Cell Tower + Wifi (Power Saving)");
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // register sensors
        if (accelerometer != null) {
            sensorManager.registerListener(SensorActivity.this, accelerometer, 1000000);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(SensorActivity.this, gyroscope, 1000000);
        }
        if (magnetometer != null) {
            sensorManager.registerListener(SensorActivity.this, magnetometer, 1000000);
        }
        Button finishSessionButton = findViewById(R.id.finishSessionButton);
        finishSessionButton.setOnClickListener(view -> {
            sensorManager.unregisterListener(this);
            fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
            MainActivity.endSession(startTime, java.time.LocalTime.now());
            finish();
        });

        updateGPS();
        accelerometerReadTime = LocalDateTime.now();
        gyroscopeReadTime = LocalDateTime.now();
        magnetometerReadTime = LocalDateTime.now();
    }

    /** Actions after requesting user for permission */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGPS();
            } else {
                Toast.makeText(this, "This app requires location permission to be granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /** Check if we got permissions to track GPS,
     * get location from fused location provider,
     * update UI. */
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SensorActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack,null);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>(){
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateUIValues(location);
                    }
                }
            });
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
            }
        }
    }

    /** Update the UI values for GPS data. */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateUIValues(Location location){
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
        if (location.hasAltitude()) {
            altitude.setText(String.valueOf(location.getAltitude()));
            sensorDatabase.insertGpsData(java.time.LocalTime.now(), location.getLatitude() - gps_lat, location.getLongitude() - gps_long, location.getAltitude() - gps_alt, location.getSpeed(), location.getBearing(), location.getAccuracy(), activityType);
            Log.d(LOGGER, "GPS: latitude: " + location.getLatitude() + ", longitude: " + location.getLongitude() + ", altitude: " + location.getAltitude());
            gps_alt = (float) location.getAltitude();
        }
        else {
            altitude.setText("Not available");
            sensorDatabase.insertGpsData(java.time.LocalTime.now(),location.getLatitude() - gps_lat, location.getLongitude() - gps_long, location.getSpeed(), location.getBearing(), location.getAccuracy(), activityType);
            Log.d(LOGGER, "GPS: latitude: " + location.getLatitude() + ", longitude: " + location.getLongitude());
        }
        gps_lat = (float) location.getLatitude();
        gps_long = (float) location.getLongitude();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /** Write sensor data into database.
     * Only writes if the previous write was longer than writeLimit seconds.
     * @param sensorEvent Sensor event.
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (ChronoUnit.SECONDS.between(accelerometerReadTime, LocalDateTime.now()) <= writeLimit) {
                return;
            }
            accelerometerReadTime = LocalDateTime.now();
            acc_x.setText(String.valueOf(sensorEvent.values[0]));
            acc_y.setText(String.valueOf(sensorEvent.values[1]));
            acc_z.setText(String.valueOf(sensorEvent.values[2]));
            sensorDatabase.insertAccelerometerData(java.time.LocalTime.now(), sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], activityType);
            Log.d(LOGGER, "Accelerometer: x: " + sensorEvent.values[0] + ", y: " + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
            return;
        }
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (ChronoUnit.SECONDS.between(gyroscopeReadTime, LocalDateTime.now()) <= writeLimit) {
                return;
            }
            gyroscopeReadTime = LocalDateTime.now();
            gyro_x.setText(String.valueOf(sensorEvent.values[0]));
            gyro_y.setText(String.valueOf(sensorEvent.values[1]));
            gyro_z.setText(String.valueOf(sensorEvent.values[2]));
            sensorDatabase.insertGyroscopeData(java.time.LocalTime.now(), sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], activityType);
            Log.d(LOGGER, "Gyroscope: x: " + sensorEvent.values[0] + ", y: " + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
            return;
        }
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            if (ChronoUnit.SECONDS.between(magnetometerReadTime, LocalDateTime.now()) <= writeLimit) {
                return;
            }
            magnetometerReadTime = LocalDateTime.now();
            mag_x.setText(String.valueOf(sensorEvent.values[0]));
            mag_y.setText(String.valueOf(sensorEvent.values[1]));
            mag_z.setText(String.valueOf(sensorEvent.values[2]));
            sensorDatabase.insertMagnetometerData(java.time.LocalTime.now(), sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], activityType);
            Log.d(LOGGER, "Magnetometer: x: " + sensorEvent.values[0] + ", y: " + sensorEvent.values[1] + ", z: " + sensorEvent.values[2]);
        }
    }
}
