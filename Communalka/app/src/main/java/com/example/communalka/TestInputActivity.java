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
    private int lightT1PriceIndex;
    private int lightT2PriceIndex;
    private int lightT3PriceIndex;
    private int hotWaterPriceIndex;
    private int coldWaterPriceIndex;

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
        if (cursor.moveToFirst()) {
            int lightT1PriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T1_PRICE);
            int lightT2PriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T2_PRICE);
            int lightT3PriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T3_PRICE);
            int hotWaterPriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_HOT_WATER_PRICE);
            int coldWaterPriceIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_COLD_WATER_PRICE);

            if (lightT1PriceIndex != -1 && lightT2PriceIndex != -1 && lightT3PriceIndex != -1 && hotWaterPriceIndex != -1 && coldWaterPriceIndex != -1) {
                double lightT1Price = cursor.getDouble(lightT1PriceIndex);
                double lightT2Price = cursor.getDouble(lightT2PriceIndex);
                double lightT3Price = cursor.getDouble(lightT3PriceIndex);
                double hotWaterPrice = cursor.getDouble(hotWaterPriceIndex);
                double coldWaterPrice = cursor.getDouble(coldWaterPriceIndex);

                // Use the prices as needed
            } else {
                Toast.makeText(this, "One or more price columns not found in database", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No prices found in database", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        double lightT1Value = Double.parseDouble(lightT1);
        double lightT2Value = Double.parseDouble(lightT2);
        double lightT3Value = Double.parseDouble(lightT3);
        double hotWaterValue = Double.parseDouble(hotWater);
        double coldWaterValue = Double.parseDouble(coldWater);

        double totalCost = calculateTotalCost(lightT1Value, lightT2Value, lightT3Value, hotWaterValue, coldWaterValue);

        textViewResult.setText("Total cost: " + totalCost);

        Toast.makeText(this, "Test input saved", Toast.LENGTH_SHORT).show();
    }
    private double calculateTotalCost(double lightT1Value, double lightT2Value, double lightT3Value, double hotWaterValue, double coldWaterValue) {
        double lightT1Price = cursor.getDouble(lightT1PriceIndex);
        double lightT2Price = cursor.getDouble(lightT2PriceIndex);
        double lightT3Price = cursor.getDouble(lightT3PriceIndex);
        double hotWaterPrice = cursor.getDouble(hotWaterPriceIndex);
        double coldWaterPrice = cursor.getDouble(coldWaterPriceIndex);

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
