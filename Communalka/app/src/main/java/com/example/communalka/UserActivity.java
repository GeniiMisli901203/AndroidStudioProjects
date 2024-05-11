package com.example.communalka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import com.example.communalka.R;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class UserActivity extends AppCompatActivity {

    private EditText lightT1EditText;
    private EditText lightT2EditText;
    private EditText lightT3EditText;
    private EditText hotWaterEditText;
    private EditText coldWaterEditText;
    private Button submitButton;
    private TextView resultTextView;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private SettingsFragment settingsFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        lightT1EditText = findViewById(R.id.light_t1_edit_text);
        lightT2EditText = findViewById(R.id.light_t2_edit_text);
        lightT3EditText = findViewById(R.id.light_t3_edit_text);
        hotWaterEditText = findViewById(R.id.hot_water_edit_text);
        coldWaterEditText = findViewById(R.id.cold_water_edit_text);
        submitButton = findViewById(R.id.submit_button);
        resultTextView = findViewById(R.id.result_text_view);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_settings) {
                    // Handle settings click
                } else if (id == R.id.nav_contact_developer) {
                    // Handle contact developer click
                } else if (id == R.id.nav_history) {
                    // Handle history click
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lightT1 = Integer.parseInt(lightT1EditText.getText().toString());
                int lightT2 = Integer.parseInt(lightT2EditText.getText().toString());
                int lightT3 = Integer.parseInt(lightT3EditText.getText().toString());
                int hotWater = Integer.parseInt(hotWaterEditText.getText().toString());
                int coldWater = Integer.parseInt(coldWaterEditText.getText().toString());


                int result = calculateUtilityBill(lightT1, lightT2, lightT3, hotWater, coldWater);
                resultTextView.setText("Result: " + result);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCounterReadings();
            }
        });
        Button buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        settingsFragment = new SettingsFragment();
        accountFragment = new AccountFragment();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.action_user) {
                } else if (item.getItemId() == R.id.action_settings) {
                    fragmentTransaction.replace(R.id.frame_layout, settingsFragment);
                    fragmentTransaction.commit();
                } else if (item.getItemId() == R.id.action_account) {
                    fragmentTransaction.replace(R.id.frame_layout, accountFragment);
                    fragmentTransaction.commit();
                }
                return false;
            }
        });
    }



    private int calculateUtilityBill(int lightT1, int lightT2, int lightT3, int hotWater, int coldWater) {

        return 0;
    }
    private void saveCounterReadings(int userId, double lightT1, double lightT2, double lightT3, double hotWater, double coldWater, double totalSum) {
        CountersDatabaseHelper dbHelper = new CountersDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CounterContract.CounterEntry.COLUMN_USER_ID, userId);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T1, lightT1);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T2, lightT2);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T3, lightT3);
        values.put(CounterContract.CounterEntry.COLUMN_HOT_WATER, hotWater);
        values.put(CounterContract.CounterEntry.COLUMN_COLD_WATER, coldWater);
        values.put(CounterContract.CounterEntry.COLUMN_TOTAL_SUM, totalSum);

        long newRowId = db.insert(CounterContract.CounterEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Failed to save counter readings", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Counter readings saved successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateTotalSum(double lightT1, double lightT2, double lightT3, double hotWater, double coldWater) {
        CountersDatabaseHelper dbHelper = new CountersDatabaseHelper(this);
        Cursor res = dbHelper.getPrices();
        double lightT1Price = 0, lightT2Price = 0, lightT3Price = 0, hotWaterPrice = 0, coldWaterPrice = 0;

        if (res.getCount() == 0) {
            // Используйте тарифы по умолчанию
            lightT1Price = 7.85;
            lightT2Price = 2.98;
            lightT3Price = 6.43;
            hotWaterPrice = 42.3;
            coldWaterPrice = 205.15;
        } else {
            while (res.moveToNext()) {
                int lightT1PriceIndex = res.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T1_PRICE);
                int lightT2PriceIndex = res.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T2_PRICE);
                int lightT3PriceIndex = res.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T3_PRICE);
                int hotWaterPriceIndex = res.getColumnIndex(CounterContract.CounterEntry.COLUMN_HOT_WATER_PRICE);
                int coldWaterPriceIndex = res.getColumnIndex(CounterContract.CounterEntry.COLUMN_COLD_WATER_PRICE);

                if (lightT1PriceIndex != -1) lightT1Price = res.getDouble(lightT1PriceIndex);
                if (lightT2PriceIndex != -1) lightT2Price = res.getDouble(lightT2PriceIndex);
                if (lightT3PriceIndex != -1) lightT3Price = res.getDouble(lightT3PriceIndex);
                if (hotWaterPriceIndex != -1) hotWaterPrice = res.getDouble(hotWaterPriceIndex);
                if (coldWaterPriceIndex != -1) coldWaterPrice = res.getDouble(coldWaterPriceIndex);
            }
        }

        double lightSum = lightT1 * lightT1Price + lightT2 * lightT2Price + lightT3 * lightT3Price;
        double waterSum = hotWater * hotWaterPrice + coldWater * coldWaterPrice;

        return lightSum + waterSum;
    }

    private void submitCounterReadings() {
        String lightT1String = lightT1EditText.getText().toString();
        String lightT2String = lightT2EditText.getText().toString();
        String lightT3String = lightT3EditText.getText().toString();
        String hotWaterString = hotWaterEditText.getText().toString();
        String coldWaterString = coldWaterEditText.getText().toString();

        if (lightT1String.isEmpty() || lightT2String.isEmpty() || lightT3String.isEmpty() || hotWaterString.isEmpty() || coldWaterString.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double lightT1 = Double.parseDouble(lightT1String);
        double lightT2 = Double.parseDouble(lightT2String);
        double lightT3 = Double.parseDouble(lightT3String);
        double hotWater = Double.parseDouble(hotWaterString);
        double coldWater = Double.parseDouble(coldWaterString);
        double totalSum = calculateTotalSum(lightT1, lightT2, lightT3, hotWater, coldWater);


        totalSum = Math.round(totalSum * 100.0) / 100.0;

        resultTextView.setText("Total sum: " + String.valueOf(totalSum));


        int userId = 1;

        saveCounterReadings(userId, lightT1, lightT2, lightT3, hotWater, coldWater, totalSum);
    }

    private void logout() {


        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
