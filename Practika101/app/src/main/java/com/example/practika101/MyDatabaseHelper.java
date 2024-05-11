package com.example.practika101;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "my_database.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// Создание таблиц
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_PHONE + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_ADDRESS + " TEXT" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Обновление таблиц, если версия базы данных изменилась
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }


    public long addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_EMAIL, contact.getEmail());
        values.put(COLUMN_ADDRESS, contact.getAddress());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_EMAIL, contact.getEmail());
        values.put(COLUMN_ADDRESS, contact.getAddress());

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(contact.getId())};

        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return rowsAffected;
    }

    public int deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int rowsAffected = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();

        return rowsAffected;
    }

    public Contact getContactByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});

        Contact contact = null;

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);

            if (idIndex != -1 && nameIndex != -1 && phoneIndex != -1 && emailIndex != -1 && addressIndex != -1) {
                contact = new Contact(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(addressIndex)
                );
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return contact;
    }
}