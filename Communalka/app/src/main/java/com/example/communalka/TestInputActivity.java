package com.example.communalka;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TestInputActivity extends AppCompatActivity {

    private EditText editTextLightT1;
    private EditText editTextLightT2;
    private EditText editTextLightT3;
    private EditText editTextHotWater;
    private EditText editTextColdWater;
    private Button buttonSave;
    private CountersDatabaseHelper dbHelper;
    private Cursor cursor;
    private Button buttonBack;
    private TextView textViewResult;


    private static final double DEFAULT_LIGHT_T1_PRICE = 7.85;
    private static final double DEFAULT_LIGHT_T2_PRICE = 2.98;
    private static final double DEFAULT_LIGHT_T3_PRICE = 6.43;
    private static final double DEFAULT_HOT_WATER_PRICE = 205.15;
    private static final double DEFAULT_COLD_WATER_PRICE = 42.3;

    private double lightT1Price;
    private double lightT2Price;
    private double lightT3Price;
    private double hotWaterPrice;
    private double coldWaterPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_input);

        editTextLightT1 = findViewById(R.id.edit_text_light_t1);
        editTextLightT2 = findViewById(R.id.edit_text_light_t2);
        editTextLightT3 = findViewById(R.id.edit_text_light_t3);
        editTextHotWater = findViewById(R.id.edit_text_hot_water);
        editTextColdWater = findViewById(R.id.edit_text_cold_water);
        buttonSave = findViewById(R.id.button_save);
        dbHelper = new CountersDatabaseHelper(this);
        cursor = dbHelper.getPrices();
        buttonBack = findViewById(R.id.button_back);
        textViewResult = findViewById(R.id.text_view_result);

        lightT1Price = DEFAULT_LIGHT_T1_PRICE;
        lightT2Price = DEFAULT_LIGHT_T2_PRICE;
        lightT3Price = DEFAULT_LIGHT_T3_PRICE;
        hotWaterPrice = DEFAULT_HOT_WATER_PRICE;
        coldWaterPrice = DEFAULT_COLD_WATER_PRICE;

        if (cursor.moveToFirst()) {
            int lightT1PriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T1_PRICE);
            int lightT2PriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T2_PRICE);
            int lightT3PriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T3_PRICE);
            int hotWaterPriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_HOT_WATER_PRICE);
            int coldWaterPriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_COLD_WATER_PRICE);

            if (lightT1PriceIndex >= 0 && lightT2PriceIndex >= 0 && lightT3PriceIndex >= 0 && hotWaterPriceIndex >= 0 && coldWaterPriceIndex >= 0) {
                lightT1Price = cursor.getDouble(lightT1PriceIndex);
                lightT2Price = cursor.getDouble(lightT2PriceIndex);
                lightT3Price = cursor.getDouble(lightT3PriceIndex);
                hotWaterPrice = cursor.getDouble(hotWaterPriceIndex);
                coldWaterPrice = cursor.getDouble(coldWaterPriceIndex);

                if (lightT1Price == 0) lightT1Price = DEFAULT_LIGHT_T1_PRICE;
                if (lightT2Price == 0) lightT2Price = DEFAULT_LIGHT_T2_PRICE;
                if (lightT3Price == 0) lightT3Price = DEFAULT_LIGHT_T3_PRICE;
                if (hotWaterPrice == 0) hotWaterPrice = DEFAULT_HOT_WATER_PRICE;
                if (coldWaterPrice == 0) coldWaterPrice = DEFAULT_COLD_WATER_PRICE;
            } else {
                Toast.makeText(this, "Одна или более элементов не найдены в БД", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Не найдены ежелемны с ценами в БД", Toast.LENGTH_SHORT).show();
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTestInput();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveTestInput() {
        String lightT1 = editTextLightT1.getText().toString();
        String lightT2 = editTextLightT2.getText().toString();
        String lightT3 = editTextLightT3.getText().toString();
        String hotWater = editTextHotWater.getText().toString();
        String coldWater = editTextColdWater.getText().toString();

        if (lightT1.isEmpty() || lightT2.isEmpty() || lightT3.isEmpty() || hotWater.isEmpty() || coldWater.isEmpty()) {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
            return;
        }

        double lightT1Value = Double.parseDouble(lightT1);
        double lightT2Value = Double.parseDouble(lightT2);
        double lightT3Value = Double.parseDouble(lightT3);
        double hotWaterValue = Double.parseDouble(hotWater);
        double coldWaterValue = Double.parseDouble(coldWater);

        double totalCost = calculateTotalCost(lightT1Value, lightT2Value, lightT3Value, hotWaterValue, coldWaterValue);

        textViewResult.setText("Сумма: " + totalCost);

        Toast.makeText(this, "Введено", Toast.LENGTH_SHORT).show();
    }

    private double calculateTotalCost(double lightT1Value, double lightT2Value, double lightT3Value, double hotWaterValue, double coldWaterValue) {
        double lightCost = lightT1Value * lightT1Price + lightT2Value * lightT2Price + lightT3Value * lightT3Price;
        double waterCost = hotWaterValue * hotWaterPrice + coldWaterValue * coldWaterPrice;
        return lightCost + waterCost;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbHelper.close();
    }
}