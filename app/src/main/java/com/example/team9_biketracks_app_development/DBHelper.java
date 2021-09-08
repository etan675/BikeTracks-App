package com.example.team9_biketracks_app_development;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


/** Custom Database class. */
public class DBHelper extends SQLiteOpenHelper {
    /** Database name. */
    private static final String DATABASE_NAME = "BikeTracks.db";
    /** Accelerometer table name. */
    private final String ACCELEROMETER_TABLE_NAME = "Accelerometer";
    /** Gyroscope table name. */
    private final String GYROSCOPE_TABLE_NAME = "Gyroscope";
    /** Magnetometer table name. */
    private final String MAGNETOMETER_TABLE_NAME = "Magnetometer";
    /** Accelerometer column names. */
    private final String[] ACCELEROMETER_COLUMN_NAMES = {"Acc_X", "Acc_Y", "Acc_Z"};
    /** Gyroscope column names. */
    private final String[] GYROSCOPE_COLUMN_NAMES = {"Gyro_X", "Gyro_Y", "Gyro_Z"};
    /** Magnetometer column names. */
    private final String[] MAGNETOMETER_COLUMN_NAMES = {"Mag_X", "Mag_y", "Mag_Z"};

    /** Constructor for DBHelper.
     * @param context Context.
     * */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    /** Called when the database is created for the first time.
     * @param db Given database.
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                ACCELEROMETER_TABLE_NAME +
                "(id integer primary key, Acc_X float, Acc_Y float, Acc_Z float)");
        db.execSQL("create table " +
                GYROSCOPE_TABLE_NAME +
                "(id integer primary key, Gyro_X float, Gyro_Y float, Gyro_Z float)");
        db.execSQL("create table " +
                MAGNETOMETER_TABLE_NAME +
                "(id integer primary key, Mag_X float, Mag_Y float, Mag_Z float)");
    }

    /** Called when the database needs to be upgraded.
     * @param db Given database.
     * @param oldVersion Old database version.
     * @param newVersion New database version.
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCELEROMETER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GYROSCOPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MAGNETOMETER_TABLE_NAME);
        onCreate(db);
    }

    /** Insert data into Accelerometer table.
     * @param Acc_x Accelerometer x value.
     * @param Acc_y Accelerometer y value.
     * @param Acc_z Accelerometer z value.
     * */
    public void insertAccelerometerData(float Acc_x, float Acc_y, float Acc_z) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[0], Acc_x);
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[1], Acc_y);
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[2], Acc_z);
        this.getWritableDatabase().insert(ACCELEROMETER_TABLE_NAME, null, contentValues);
    }

    /** Insert data into Gyroscope table.
     * @param Gyro_X Gyroscope x value.
     * @param Gyro_Y Gyroscope y value.
     * @param Gyro_Z Gyroscope z value.
     * */
    public void insertGyroscopeData(float Gyro_X, float Gyro_Y, float Gyro_Z) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GYROSCOPE_COLUMN_NAMES[0], Gyro_X);
        contentValues.put(GYROSCOPE_COLUMN_NAMES[1], Gyro_Y);
        contentValues.put(GYROSCOPE_COLUMN_NAMES[2], Gyro_Z);
        this.getWritableDatabase().insert(GYROSCOPE_TABLE_NAME, null, contentValues);
    }

    /** Insert data into Magnetometer table.
     * @param Mag_X Magnetometer x value.
     * @param Mag_Y Magnetometer y value.
     * @param Mag_Z Magnetometer z value.
     * */
    public void insertMagnetometerData(float Mag_X, float Mag_Y, float Mag_Z) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[0], Mag_X);
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[1], Mag_Y);
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[2], Mag_Z);
        this.getWritableDatabase().insert(MAGNETOMETER_TABLE_NAME, null, contentValues);
    }

    public Cursor getAccData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " +
                ACCELEROMETER_TABLE_NAME +
                " where id=" + id + "",
                null );
        return res;
    }

    public int numberOfAccRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACCELEROMETER_TABLE_NAME);
        return numRows;
    }

    public ArrayList<String> getAllAccData() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ACCELEROMETER_TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(ACCELEROMETER_COLUMN_NAMES[0])));
            res.moveToNext();
        }
        return array_list;
    }
}
