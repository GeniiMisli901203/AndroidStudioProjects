package com.example.communalka;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "utilities.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserContract.UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, "
                + UserContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_SALT + " TEXT NOT NULL,"
                + UserContract.UserEntry.COLUMN_ROLE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }
    public String getUserEmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME,
                new String[]{UserContract.UserEntry.COLUMN_EMAIL},
                null, null, null, null, null);

        if (cursor != null) {
            int emailIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL);
            if (emailIndex != -1 && cursor.moveToFirst()) {
                String email = cursor.getString(emailIndex);
                cursor.close();
                return email;
            }
            cursor.close();
        }

        return null;
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_USERS_TABLE = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_USERS_TABLE);
        onCreate(db);
    }
}
