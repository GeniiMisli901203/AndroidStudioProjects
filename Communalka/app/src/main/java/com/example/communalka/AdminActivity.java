package com.example.communalka;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private EditText lightT1PriceEditText;
    private EditText lightT2PriceEditText;
    private EditText lightT3PriceEditText;
    private EditText hotWaterPriceEditText;
    private EditText coldWaterPriceEditText;
    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        lightT1PriceEditText = findViewById(R.id.light_t1_price_edit_text);
        lightT2PriceEditText = findViewById(R.id.light_t2_price_edit_text);
        lightT3PriceEditText = findViewById(R.id.light_t3_price_edit_text);
        hotWaterPriceEditText = findViewById(R.id.hot_water_price_edit_text);
        coldWaterPriceEditText = findViewById(R.id.cold_water_price_edit_text);
        saveChangesButton = findViewById(R.id.save_changes_button);
        loadPrices();
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lightT1PriceString = lightT1PriceEditText.getText().toString();
                String lightT2PriceString = lightT2PriceEditText.getText().toString();
                String lightT3PriceString = lightT3PriceEditText.getText().toString();
                String hotWaterPriceString = hotWaterPriceEditText.getText().toString();
                String coldWaterPriceString = coldWaterPriceEditText.getText().toString();

                if (lightT1PriceString.isEmpty() || lightT2PriceString.isEmpty() || lightT3PriceString.isEmpty() || hotWaterPriceString.isEmpty() || coldWaterPriceString.isEmpty()) {
                    Toast.makeText(AdminActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double lightT1Price = Double.parseDouble(lightT1PriceString);
                double lightT2Price = Double.parseDouble(lightT2PriceString);
                double lightT3Price = Double.parseDouble(lightT3PriceString);
                double hotWaterPrice = Double.parseDouble(hotWaterPriceString);
                double coldWaterPrice = Double.parseDouble(coldWaterPriceString);

                updatePrices(lightT1Price, lightT2Price, lightT3Price, hotWaterPrice, coldWaterPrice);
            }
        });
        Button buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button buttonTestInput = findViewById(R.id.button_test_input);
        buttonTestInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, TestInputActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updatePrices(double lightT1Price, double lightT2Price, double lightT3Price, double hotWaterPrice, double coldWaterPrice) {
        CountersDatabaseHelper dbHelper = new CountersDatabaseHelper(this);
        dbHelper.updatePrices(lightT1Price, lightT2Price, lightT3Price, hotWaterPrice, coldWaterPrice);
        Toast.makeText(AdminActivity.this, "Prices updated successfully", Toast.LENGTH_SHORT).show();
    }
    private void loadPrices() {
        CountersDatabaseHelper dbHelper = new CountersDatabaseHelper(this);
        Cursor priceCursor = dbHelper.getPrices();

        if (priceCursor.moveToFirst()) {
            int lightT1PriceIndex = priceCursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T1_PRICE);
            int lightT2PriceIndex = priceCursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T2_PRICE);
            int lightT3PriceIndex = priceCursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T3_PRICE);
            int hotWaterPriceIndex = priceCursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_HOT_WATER_PRICE);
            int coldWaterPriceIndex = priceCursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_COLD_WATER_PRICE);

            double lightT1Price = (lightT1PriceIndex != -1) ? priceCursor.getDouble(lightT1PriceIndex) : 0;
            double lightT2Price = (lightT2PriceIndex != -1) ? priceCursor.getDouble(lightT2PriceIndex) : 0;
            double lightT3Price = (lightT3PriceIndex != -1) ? priceCursor.getDouble(lightT3PriceIndex) : 0;
            double hotWaterPrice = (hotWaterPriceIndex != -1) ? priceCursor.getDouble(hotWaterPriceIndex) : 0;
            double coldWaterPrice = (coldWaterPriceIndex != -1) ? priceCursor.getDouble(coldWaterPriceIndex) : 0;

            lightT1PriceEditText.setText(String.valueOf(lightT1Price));
            lightT2PriceEditText.setText(String.valueOf(lightT2Price));
            lightT3PriceEditText.setText(String.valueOf(lightT3Price));
            hotWaterPriceEditText.setText(String.valueOf(hotWaterPrice));
            coldWaterPriceEditText.setText(String.valueOf(coldWaterPrice));
        }
        priceCursor.close();
    }
}