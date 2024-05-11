package com.example.practika7;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DialogActivity extends AppCompatActivity {

    private static final int ALERT_DIALOG_RESULT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Button alertDialogButton = findViewById(R.id.alert_dialog_button);
        Button datePickerDialogButton = findViewById(R.id.date_picker_dialog_button);
        Button timePickerDialogButton = findViewById(R.id.time_picker_dialog_button);
        Button customDialogButton = findViewById(R.id.custom_dialog_button);

        alertDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        datePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        customDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы любите деньги?")
                .setMessage("This is an alert dialog.")
                .setPositiveButton("ДААА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("dialog_type", "alert");
                        setResult(ALERT_DIALOG_RESULT_CODE, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton("Точно ДААА", null)
                .show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("dialog_type", "date");
                resultIntent.putExtra("year", year);
                resultIntent.putExtra("month", month);
                resultIntent.putExtra("day", dayOfMonth);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("dialog_type", "time");
                resultIntent.putExtra("hour", hourOfDay);
                resultIntent.putExtra("minute", minute);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View customDialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customDialogView);


        final EditText inputEditText = customDialogView.findViewById(R.id.custom_dialog_input);
        Button okButton = customDialogView.findViewById(R.id.custom_dialog_ok_button);
        Button cancelButton = customDialogView.findViewById(R.id.custom_dialog_cancel_button);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = inputEditText.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("dialog_type", "custom");
                resultIntent.putExtra("input_text", inputText);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AlertDialog customDialog = builder.create();
        customDialog.show();
    }
}