package com.example.team9_biketracks_app_development;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


/** Custom Database class. */
public class SensorDatabase extends SQLiteOpenHelper {
    /** Database name. */
    private static final String DATABASE_NAME = "BikeTracks.db";
    /** Accelerometer table name. */
    private final String ACCELEROMETER_TABLE_NAME = "Accelerometer";
    /** Gyroscope table name. */
    private final String GYROSCOPE_TABLE_NAME = "Gyroscope";
    /** Magnetometer table name. */
    private final String MAGNETOMETER_TABLE_NAME = "Magnetometer";
    /** Accelerometer column names. */
    private final String[] ACCELEROMETER_COLUMN_NAMES = {"acc_x", "acc_y", "acc_z"};
    /** Gyroscope column names. */
    private final String[] GYROSCOPE_COLUMN_NAMES = {"gyro_x", "gyro_y", "gyro_z"};
    /** Magnetometer column names. */
    private final String[] MAGNETOMETER_COLUMN_NAMES = {"mag_x", "mag_y", "mag_z"};

    /** Constructor for DBHelper.
     * @param context Context.
     * */
    public SensorDatabase(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    /** Called when the database is created for the first time.
     * @param db Given database.
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                ACCELEROMETER_TABLE_NAME +
                "(id integer primary key, acc_x float, acc_y float, acc_z float)");
        db.execSQL("create table " +
                GYROSCOPE_TABLE_NAME +
                "(id integer primary key, gyro_x float, gyro_y float, gyro_z float)");
        db.execSQL("create table " +
                MAGNETOMETER_TABLE_NAME +
                "(id integer primary key, mag_x float, mag_y float, mag_z float)");
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
     * @param acc_x Accelerometer x value.
     * @param acc_y Accelerometer y value.
     * @param acc_z Accelerometer z value.
     * */
    public void insertAccelerometerData(float acc_x, float acc_y, float acc_z) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[0], acc_x);
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[1], acc_y);
        contentValues.put(ACCELEROMETER_COLUMN_NAMES[2], acc_z);
        this.getWritableDatabase().insert(ACCELEROMETER_TABLE_NAME, null, contentValues);
    }

    /** Insert data into Gyroscope table.
     * @param gyro_x Gyroscope x value.
     * @param gyro_y Gyroscope y value.
     * @param gyro_z Gyroscope z value.
     * */
    public void insertGyroscopeData(float gyro_x, float gyro_y, float gyro_z) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GYROSCOPE_COLUMN_NAMES[0], gyro_x);
        contentValues.put(GYROSCOPE_COLUMN_NAMES[1], gyro_y);
        contentValues.put(GYROSCOPE_COLUMN_NAMES[2], gyro_z);
        this.getWritableDatabase().insert(GYROSCOPE_TABLE_NAME, null, contentValues);
    }

    /** Insert data into Magnetometer table.
     * @param mag_x Magnetometer x value.
     * @param mag_y Magnetometer y value.
     * @param mag_z Magnetometer z value.
     * */
    public void insertMagnetometerData(float mag_x, float mag_y, float mag_z) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[0], mag_x);
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[1], mag_y);
        contentValues.put(MAGNETOMETER_COLUMN_NAMES[2], mag_z);
        this.getWritableDatabase().insert(MAGNETOMETER_TABLE_NAME, null, contentValues);
    }
/*
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
    }*/
}
