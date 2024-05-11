package com.example.practika7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = findViewById(R.id.button_start);
        final Button btnStop = findViewById(R.id.button_stop);

        // запуск службы
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // используем явный вызов службы
                startService(
                        new Intent(MainActivity.this, PlayService.class));
            }
        });
        resultTextView = findViewById(R.id.result_text_view);

        Button openDialogActivityButton = findViewById(R.id.open_dialog_activity_button);
        openDialogActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                startActivityForResult(intent, 100);
            }
        });


        // остановка службы
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(
                        new Intent(MainActivity.this, PlayService.class));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String dialogType = data.getStringExtra("dialog_type");
            switch (dialogType) {
                case "date":
                    int year = data.getIntExtra("year", 0);
                    int month = data.getIntExtra("month", 0);
                    int day = data.getIntExtra("day", 0);
                    resultTextView.setText(String.format("Selected date: %d-%02d-%02d", year, month + 1, day));
                    break;
                case "time":
                    int hour = data.getIntExtra("hour", 0);
                    int minute = data.getIntExtra("minute", 0);
                    resultTextView.setText(String.format("Selected time: %02d:%02d", hour, minute));
                    break;
                case "custom":
                    String inputText = data.getStringExtra("input_text");
                    resultTextView.setText(String.format("Custom input: %s", inputText));
                    break;
            }
        }
    }
}