package com.example.communalka;

import android.provider.BaseColumns;

public class CounterContract {

    public static final class CounterEntry implements BaseColumns {

        public static final String TABLE_NAME = "counters";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_LIGHT_PRICE = "light_price";
        public static final String COLUMN_WATER_PRICE = "water_price";

        public static final String COLUMN_LIGHT_T1 = "light_t1";
        public static final String COLUMN_LIGHT_T2 = "light_t2";
        public static final String COLUMN_LIGHT_T3 = "light_t3";
        public static final String COLUMN_HOT_WATER = "hot_water";
        public static final String COLUMN_COLD_WATER = "cold_water";
        public static final String COLUMN_TOTAL_SUM = "total_sum";
        public static final String COLUMN_LIGHT_T1_PRICE = "light_t1_price";
        public static final String COLUMN_LIGHT_T2_PRICE = "light_t2_price";
        public static final String COLUMN_LIGHT_T3_PRICE = "light_t3_price";
        public static final String COLUMN_HOT_WATER_PRICE = "hot_water_price";
        public static final String COLUMN_COLD_WATER_PRICE = "cold_water_price";
        public static final String COLUMN_PREVIOUS_SUM = "previous_sum";
        public static final String COLUMN_DIFFERENCE = "difference";
        public static final String COLUMN_TIMESTAMP = "timestamp";


    }
}
