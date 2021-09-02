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
    public static final String TABLE_NAME = "Sensors";
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
    public static final String COL_11 = "Light";
    public static final String COL_12 = "Pressure";
    public static final String COL_13 = "Temperature";
    public static final String COL_14 = "Humidity";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(id integer primary key, Acc_X float, Acc_Y float,Acc_Z float, Gyro_X float,Gyro_Y float," +
                        "Gyro_Z float, Mag_X float, Mag_Y float, Mag_Z float, Light float, Pressure float, Temperature float, Humidity float)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSensors (float Acc_x, float Acc_y, float Acc_z, float Gyro_X, float Gyro_Y, float Gyro_Z,
                                  float Mag_X, float Mag_Y, float Mag_Z, float light, float pressure, float temperature, float humidity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Acc_x);
        contentValues.put(COL_3, Acc_y);
        contentValues.put(COL_4, Acc_z);
        contentValues.put(COL_5, Gyro_X);
        contentValues.put(COL_6, Gyro_Y);
        contentValues.put(COL_7, Gyro_Z);
        contentValues.put(COL_8, Mag_X);
        contentValues.put(COL_9, Mag_Y);
        contentValues.put(COL_10, Mag_Z);
        contentValues.put(COL_11, light);
        contentValues.put(COL_12, pressure);
        contentValues.put(COL_13, temperature);
        contentValues.put(COL_14, humidity);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertAccSensors (float Acc_x, float Acc_y, float Acc_z){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Acc_x);
        contentValues.put(COL_3, Acc_y);
        contentValues.put(COL_4, Acc_z);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME + " where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public ArrayList<String> getAllData() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COL_2)));
            res.moveToNext();
        }
        return array_list;
    }
}