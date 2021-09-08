package com.example.team9_biketracks_app_development;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BikeTracks.db";
    public static final String ACC_TABLE_NAME = "Accelerometer";
    public static final String GYRO_TABLE_NAME = "Gyroscope";
    public static final String MAG_TABLE_NAME = "MagneticField";

    public static final String COLUMN_ID = "id";
    public static final String COL_2 = "Acc_X";
    public static final String COL_3 = "Acc_Y";
    public static final String COL_4 = "Acc_Z";
    public static final String COL_5 = "Gyro_X";
    public static final String COL_6 = "Gyro_Y";
    public static final String COL_7 = "Gyro_Z";
    public static final String COL_8 = "Mag_X";
    public static final String COL_9 = "Mag_y";
    public static final String COL_10 = "Mag_Z";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + ACC_TABLE_NAME +
                        "(id integer primary key, Acc_X float, Acc_Y float,Acc_Z float)"
        );

        db.execSQL(
                "create table " + GYRO_TABLE_NAME +
                        "(id integer primary key, Gyro_X float, Gyro_Y float, Gyro_Z float)"
        );

        db.execSQL(
                "create table " + MAG_TABLE_NAME +
                        "(id integer primary key, Mag_X float, Mag_Y float,Mag_Z float)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACC_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GYRO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MAG_TABLE_NAME);
        onCreate(db);
    }

    //Insert data into Accelerometer Table
    public boolean insertAccSensors (float Acc_x, float Acc_y, float Acc_z){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Acc_x);
        contentValues.put(COL_3, Acc_y);
        contentValues.put(COL_4, Acc_z);

        db.insert(ACC_TABLE_NAME, null, contentValues);
        return true;
    }

    //Insert data into Gyroscope Table
    public boolean insertGyroSensors (float Gyro_X, float Gyro_Y, float Gyro_Z){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5, Gyro_X);
        contentValues.put(COL_6, Gyro_Y);
        contentValues.put(COL_7, Gyro_Z);
        db.insert(GYRO_TABLE_NAME, null, contentValues);
        return true;
    }

    //Insert data into MagneticField Table
    public boolean insertMagSensors (float Mag_X, float Mag_Y, float Mag_Z){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8, Mag_X);
        contentValues.put(COL_9, Mag_Y);
        contentValues.put(COL_10, Mag_Z);
        db.insert(MAG_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getAccData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ACC_TABLE_NAME + " where id="+id+"", null );
        return res;
    }

    public int numberOfAccRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACC_TABLE_NAME);
        return numRows;
    }

    public ArrayList<String> getAllAccData() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ACC_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COL_2)));
            res.moveToNext();
        }
        return array_list;
    }
}