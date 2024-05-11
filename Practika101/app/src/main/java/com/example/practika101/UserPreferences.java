package com.example.practika101;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_NAME = "user_name";

    private SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public void updateUserName(String newUserName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, newUserName);
        editor.apply();
    }

    public void deleteUserName() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_NAME);
        editor.apply();
    }
}
