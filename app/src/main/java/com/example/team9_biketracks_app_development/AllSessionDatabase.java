package com.example.team9_biketracks_app_development;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/** Custom Database class for general information of all sessions . */
public class AllSessionDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Sessions.db";
    /** Table name. */
    private final String TABLE_NAME = "SessionInfo";
    /** Column names. */
    private final String[] COLUMN_NAMES = {"Date", "StartTime", "EndTime", "VehicleType"};

    /** Constructor for DBHelper.
     * @param context Context.
     * */
    public AllSessionDatabase(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    /** Called when the database is created for the first time.
     * @param db Given database.
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                TABLE_NAME +
                "(id integer primary key, Date LocalDate, StartTime LocalTime, StopTime LocalDate, VehicleType String)");
    }

    /** Called when the database needs to be upgraded.
     * @param db Given database.
     * @param oldVersion Old database version.
     * @param newVersion New database version.
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    /** Insert data into Accelerometer table.
     * @param date Session date.
     * @param startTime Session startTime.
     * */
    public void insertDateTime(LocalDate date, LocalTime startTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAMES[0], String.valueOf(date));
        contentValues.put(COLUMN_NAMES[1], String.valueOf(startTime));
        this.getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

}
