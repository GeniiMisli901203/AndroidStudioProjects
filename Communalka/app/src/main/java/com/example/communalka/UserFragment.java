package com.example.communalka;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {

    private EditText lightT1EditText;
    private EditText lightT2EditText;
    private EditText lightT3EditText;
    private EditText hotWaterEditText;
    private EditText coldWaterEditText;
    private Button submitButton;
    private TextView resultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        lightT1EditText = view.findViewById(R.id.light_t1_edit_text);
        lightT2EditText = view.findViewById(R.id.light_t2_edit_text);
        lightT3EditText = view.findViewById(R.id.light_t3_edit_text);
        hotWaterEditText = view.findViewById(R.id.hot_water_edit_text);
        coldWaterEditText = view.findViewById(R.id.cold_water_edit_text);
        submitButton = view.findViewById(R.id.submit_button);
        resultTextView = view.findViewById(R.id.result_text_view);
        Button logoutButton = view.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCounterReadings(getContext());
            }
        });
        if (savedInstanceState != null) {
            String lightT1 = savedInstanceState.getString("light_t1_key");
            String lightT2 = savedInstanceState.getString("light_t2_key");
            String lightT3 = savedInstanceState.getString("light_t3_key");
            String hotWater = savedInstanceState.getString("hot_water_key");
            String coldWater = savedInstanceState.getString("cold_water_key");

            lightT1EditText.setText(lightT1);
            lightT2EditText.setText(lightT2);
            lightT3EditText.setText(lightT3);
            hotWaterEditText.setText(hotWater);
            coldWaterEditText.setText(coldWater);
        }


        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the values of your EditText views
        outState.putString("light_t1_key", lightT1EditText.getText().toString());
        outState.putString("light_t2_key", lightT2EditText.getText().toString());
        outState.putString("light_t3_key", lightT3EditText.getText().toString());
        outState.putString("hot_water_key", hotWaterEditText.getText().toString());
        outState.putString("cold_water_key", coldWaterEditText.getText().toString());
    }



    private void saveCounterReadings(Context context, int userId, double lightT1, double lightT2, double lightT3, double hotWater, double coldWater, double totalSum) {
        CountersDatabaseHelper dbHelper = new CountersDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CounterContract.CounterEntry.COLUMN_USER_ID, userId);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T1, lightT1);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T2, lightT2);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T3, lightT3);
        values.put(CounterContract.CounterEntry.COLUMN_HOT_WATER, hotWater);
        values.put(CounterContract.CounterEntry.COLUMN_COLD_WATER, coldWater);
        values.put(CounterContract.CounterEntry.COLUMN_TOTAL_SUM, totalSum);

        // Add default prices
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T1_PRICE, 7.85);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T2_PRICE, 2.98);
        values.put(CounterContract.CounterEntry.COLUMN_LIGHT_T3_PRICE, 6.43);
        values.put(CounterContract.CounterEntry.COLUMN_HOT_WATER_PRICE, 205.15);
        values.put(CounterContract.CounterEntry.COLUMN_COLD_WATER_PRICE, 42.3);

        try {
            long newRowId = db.insert(CounterContract.CounterEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Toast.makeText(context, "Failed to save counter readings", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Counter readings saved successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, "Failed to save counter readings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    private double calculateTotalSum(Context context, double lightT1, double lightT2, double lightT3, double hotWater, double coldWater) {
        CountersDatabaseHelper dbHelper = new CountersDatabaseHelper(context);
        Cursor res = dbHelper.getPrices();
        double lightT1Price = 0, lightT2Price = 0, lightT3Price = 0, hotWaterPrice = 0, coldWaterPrice = 0;

        if (res.getCount() == 0) {
            // Используйте тарифы по умолчанию
            lightT1Price = 7.85;
            lightT2Price = 2.98;
            lightT3Price = 6.43;
            hotWaterPrice = 205.15;
            coldWaterPrice = 42.3;
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

    private void submitCounterReadings(Context context) {
        String lightT1String = lightT1EditText.getText().toString();
        String lightT2String = lightT2EditText.getText().toString();
        String lightT3String = lightT3EditText.getText().toString();
        String hotWaterString = hotWaterEditText.getText().toString();
        String coldWaterString = coldWaterEditText.getText().toString();

        if (lightT1String.isEmpty() || lightT2String.isEmpty() || lightT3String.isEmpty() || hotWaterString.isEmpty() || coldWaterString.isEmpty()) {
            Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double lightT1 = Double.parseDouble(lightT1String);
        double lightT2 = Double.parseDouble(lightT2String);
        double lightT3 = Double.parseDouble(lightT3String);
        double hotWater = Double.parseDouble(hotWaterString);
        double coldWater = Double.parseDouble(coldWaterString);
        double totalSum = calculateTotalSum(context, lightT1, lightT2, lightT3, hotWater, coldWater);

        totalSum = Math.round(totalSum * 100.0) / 100.0;

        resultTextView.setText("Total sum: " + String.valueOf(totalSum));

        int userId = 1;

        saveCounterReadings(context, userId, lightT1, lightT2, lightT3, hotWater, coldWater, totalSum);
    }

    private void logout() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}
