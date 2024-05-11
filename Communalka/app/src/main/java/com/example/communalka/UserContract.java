package com.example.communalka;

import android.provider.BaseColumns;

public class UserContract {

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final int ROLE_ADMIN = 1;
        public static final int ROLE_USER= 0;

        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_SALT = "salt";
    }
}
