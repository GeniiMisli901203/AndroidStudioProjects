package com.example.communalka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import android.util.Base64;

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
                + UserContract.UserEntry.COLUMN_NAME + " TEXT, " // Изменено
                + UserContract.UserEntry.COLUMN_SALT + " TEXT NOT NULL,"
                + UserContract.UserEntry.COLUMN_ROLE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_USERS_TABLE);

        addDefaultAdmin(db);
    }

    public void addDefaultAdmin(SQLiteDatabase db) {
        String adminEmail = "admin";
        String adminPassword = "admin";

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                new String[]{UserContract.UserEntry._ID},
                UserContract.UserEntry.COLUMN_EMAIL + " = ? AND " + UserContract.UserEntry.COLUMN_ROLE + " = ?",
                new String[]{adminEmail, "1"},
                null, null, null
        );

        if (cursor != null && cursor.getCount() == 0) {
            String salt = generateSalt();
            String hashedPassword = hashPassword(adminPassword, salt);

            ContentValues values = new ContentValues();
            values.put(UserContract.UserEntry.COLUMN_EMAIL, adminEmail);
            values.put(UserContract.UserEntry.COLUMN_PASSWORD, hashedPassword);
            values.put(UserContract.UserEntry.COLUMN_SALT, salt);
            values.put(UserContract.UserEntry.COLUMN_ROLE, 1);

            db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void saveUserName(String name, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME, name);
        db.update(UserContract.UserEntry.TABLE_NAME, values, UserContract.UserEntry._ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME,
                new String[]{UserContract.UserEntry.COLUMN_NAME},
                UserContract.UserEntry._ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
            if (nameIndex != -1 && cursor.moveToFirst()) {
                String name = cursor.getString(nameIndex);
                cursor.close();
                return name;
            }
            cursor.close();
        }
        return null;
    }

    public String getUserEmail(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME,
                new String[]{UserContract.UserEntry.COLUMN_EMAIL},
                UserContract.UserEntry._ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int emailIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL);
            if (emailIndex != -1) {
                String email = cursor.getString(emailIndex);
                cursor.close();
                return email;
            }
            cursor.close();
        }

        return null;
    }



    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME,
                new String[]{UserContract.UserEntry._ID},
                UserContract.UserEntry.COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(UserContract.UserEntry._ID);
            if (idIndex != -1 && cursor.moveToFirst()) {
                int id = cursor.getInt(idIndex);
                cursor.close();
                return id;
            }
            cursor.close();
        }

        return -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_USERS_TABLE = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_USERS_TABLE);
        onCreate(db);
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.encodeToString(salt, Base64.DEFAULT);
    }

    private String hashPassword(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash;
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeToString(hash, Base64.DEFAULT);
    }
}
