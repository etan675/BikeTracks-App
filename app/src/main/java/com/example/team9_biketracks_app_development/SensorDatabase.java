package com.example.team9_biketracks_app_development;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;


/** Custom Database class. */
public class SensorDatabase extends SQLiteOpenHelper {
    /** Accelerometer table name. */
    private final String ACCELEROMETER_TABLE_NAME = "Accelerometer";
    /** Gyroscope table name. */
    private final String GYROSCOPE_TABLE_NAME = "Gyroscope";
    /** Magnetometer table name. */
    private final String MAGNETOMETER_TABLE_NAME = "Magnetometer";
    /** GPS table name. */
    private final String GPS_TABLE_NAME = "GPS";
    /** Accelerometer column names. */
    private final String[] ACCELEROMETER_COLUMN_NAMES = {"timestamp", "acc_x", "acc_y", "acc_z", "activity"};
    /** Gyroscope column names. */
    private final String[] GYROSCOPE_COLUMN_NAMES = {"timestamp", "gyro_x", "gyro_y", "gyro_z", "activity"};
    /** Magnetometer column names. */
    private final String[] MAGNETOMETER_COLUMN_NAMES = {"timestamp", "mag_x", "mag_y", "mag_z", "activity"};
    /** GPS column names. */
    private final String[] GPS_COLUMN_NAMES = {"timestamp", "gps_lat_increment", "gps_long_increment", "gps_alt_increment", "gps_speed", "gps_bearing", "gps_accuracy", "activity"};

    /** Constructor.
     * @param context Context.
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public SensorDatabase(Context context) {
        super(context, "BikeTracks" + java.time.LocalDateTime.now() + ".db" , null, 1);
    }

    /** Create database.
     * @param db Given database.
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                ACCELEROMETER_TABLE_NAME +
                "(id integer primary key, timestamp LocalTime, acc_x float, acc_y float, acc_z float, activity String)");
        db.execSQL("create table " +
                GYROSCOPE_TABLE_NAME +
                "(id integer primary key, timestamp LocalTime, gyro_x float, gyro_y float, gyro_z float, activity String)");
        db.execSQL("create table " +
                MAGNETOMETER_TABLE_NAME +
                "(id integer primary key, timestamp LocalTime, mag_x float, mag_y float, mag_z float, activity String)");
        db.execSQL("create table " +
                GPS_TABLE_NAME +
                "(id integer primary key, timestamp LocalTime, gps_lat_increment float, gps_long_increment float, gps_alt_increment float, gps_speed float, gps_bearing float, gps_accuracy float, activity String)");
    }

    /** Upgrade database.
     * @param db Given database.
     * @param oldVersion Old database version.
     * @param newVersion New database version.
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCELEROMETER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GYROSCOPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MAGNETOMETER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GPS_TABLE_NAME);
        onCreate(db);
    }

    /** Insert data into Accelerometer table.
     * @param acc_x Accelerometer x value.
     * @param acc_y Accelerometer y value.
     * @param acc_z Accelerometer z value.
     * */
    public void insertAccelerometerData(LocalTime timestamp, float acc_x, float acc_y, float acc_z, String activity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[0], String.valueOf(timestamp));
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[1], acc_x);
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[2], acc_y);
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[3], acc_z);
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[4], activity);
        this.getWritableDatabase().insert(ACCELEROMETER_TABLE_NAME, null, contentValues);
    }

    /** Insert data into Gyroscope table.
     * @param gyro_x Gyroscope x value.
     * @param gyro_y Gyroscope y value.
     * @param gyro_z Gyroscope z value.
     * */
    public void insertGyroscopeData(LocalTime timestamp, float gyro_x, float gyro_y, float gyro_z, String activity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GYROSCOPE_COLUMN_NAMES[0], String.valueOf(timestamp));
        contentValues.put(GYROSCOPE_COLUMN_NAMES[1], gyro_x);
        contentValues.put(GYROSCOPE_COLUMN_NAMES[2], gyro_y);
        contentValues.put(GYROSCOPE_COLUMN_NAMES[3], gyro_z);
        contentValues.put(GYROSCOPE_COLUMN_NAMES[4], activity);
        this.getWritableDatabase().insert(GYROSCOPE_TABLE_NAME, null, contentValues);
    }

    /** Insert data into Magnetometer table.
     * @param mag_x Magnetometer x value.
     * @param mag_y Magnetometer y value.
     * @param mag_z Magnetometer z value.
     * */
    public void insertMagnetometerData(LocalTime timestamp, float mag_x, float mag_y, float mag_z, String activity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[0], String.valueOf(timestamp));
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[1], mag_x);
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[2], mag_y);
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[3], mag_z);
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[4], activity);
        this.getWritableDatabase().insert(MAGNETOMETER_TABLE_NAME, null, contentValues);
    }

    /** Insert data into GPS table.
     * @param latitude GPS latitude.
     * @param longitude GPS longitude.
     * @param altitude GPS altitude.
     * */
    public void insertGpsData(LocalTime timestamp, double latitude, double longitude, double altitude, float speed, float bearing, float accuracy, String activity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GPS_COLUMN_NAMES[0], String.valueOf(timestamp));
        contentValues.put(GPS_COLUMN_NAMES[1], latitude);
        contentValues.put(GPS_COLUMN_NAMES[2], longitude);
        contentValues.put(GPS_COLUMN_NAMES[3], altitude);
        contentValues.put(GPS_COLUMN_NAMES[4], speed);
        contentValues.put(GPS_COLUMN_NAMES[5], bearing);
        contentValues.put(GPS_COLUMN_NAMES[6], accuracy);
        contentValues.put(GPS_COLUMN_NAMES[7], activity);
        this.getWritableDatabase().insert(GPS_TABLE_NAME, null, contentValues);
    }

    /** Insert data into GPS table.
     * @param latitude GPS latitude.
     * @param longitude GPS longtitude.
     * */

    public void insertGpsData(LocalTime timestamp, double latitude, double longitude, float speed, float bearing, float accuracy, String activity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GPS_COLUMN_NAMES[0], String.valueOf(timestamp));
        contentValues.put(GPS_COLUMN_NAMES[1], latitude);
        contentValues.put(GPS_COLUMN_NAMES[2], longitude);
        contentValues.put(GPS_COLUMN_NAMES[4], speed);
        contentValues.put(GPS_COLUMN_NAMES[5], bearing);
        contentValues.put(GPS_COLUMN_NAMES[6], accuracy);
        contentValues.put(GPS_COLUMN_NAMES[7], activity);
        this.getWritableDatabase().insert(GPS_TABLE_NAME, null, contentValues);
    }
}
