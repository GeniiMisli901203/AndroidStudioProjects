package com.example.communalka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CountersDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "counters.db";
    public static final int DATABASE_VERSION = 1;

    public CountersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_COUNTERS_TABLE = "CREATE TABLE " + CounterContract.CounterEntry.TABLE_NAME + " ("
                + CounterContract.CounterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CounterContract.CounterEntry.COLUMN_USER_ID + " INTEGER NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_LIGHT_T1 + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_LIGHT_T2 + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_LIGHT_T3 + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_HOT_WATER + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_COLD_WATER + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_TOTAL_SUM + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_LIGHT_T1_PRICE + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_LIGHT_T2_PRICE + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_LIGHT_T3_PRICE + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_HOT_WATER_PRICE + " REAL NOT NULL, "
                + CounterContract.CounterEntry.COLUMN_COLD_WATER_PRICE + " REAL NOT NULL);";
        db.execSQL(SQL_CREATE_COUNTERS_TABLE);
    }


    public Cursor getPrices() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CounterContract.CounterEntry.TABLE_NAME, null );
        return res;
    }

    public void updatePrices(double lightT1Price, double lightT2Price, double lightT3Price, double hotWaterPrice, double coldWaterPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CounterContract.CounterEntry.COLUMN_LIGHT_T1_PRICE, lightT1Price);
        contentValues.put(CounterContract.CounterEntry.COLUMN_LIGHT_T2_PRICE, lightT2Price);
        contentValues.put(CounterContract.CounterEntry.COLUMN_LIGHT_T3_PRICE, lightT3Price);
        contentValues.put(CounterContract.CounterEntry.COLUMN_HOT_WATER_PRICE, hotWaterPrice);
        contentValues.put(CounterContract.CounterEntry.COLUMN_COLD_WATER_PRICE, coldWaterPrice);
        db.update(CounterContract.CounterEntry.TABLE_NAME, contentValues, null, null);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_COUNTERS_TABLE = "DROP TABLE IF EXISTS " + CounterContract.CounterEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_COUNTERS_TABLE);
        onCreate(db);
    }
}
