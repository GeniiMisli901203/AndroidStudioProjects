package com.example.practika9;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileActivity extends AppCompatActivity {

    private EditText fileNameEditText;
    private EditText fileContentEditText;
    private TextView fileOutputTextView;
    private String fileName;
    private String fileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        fileNameEditText = findViewById(R.id.fileNameEditText);
        fileContentEditText = findViewById(R.id.fileContentEditText);
        fileOutputTextView = findViewById(R.id.fileOutputTextView);

        Button createButton = findViewById(R.id.createButton);
        Button appendButton = findViewById(R.id.appendButton);
        Button readButton = findViewById(R.id.readButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });

        appendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendFile();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fileName", fileName);
        outState.putString("fileContent", fileContent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileName = savedInstanceState.getString("fileName");
        fileContent = savedInstanceState.getString("fileContent");
        fileNameEditText.setText(fileName);
        fileContentEditText.setText(fileContent);
    }

    private void createFile() {
        fileName = fileNameEditText.getText().toString();
        fileContent = fileContentEditText.getText().toString();

        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(fileContent.getBytes());
            fos.close();
            Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendFile() {
        fileName = fileNameEditText.getText().toString();
        fileContent = fileContentEditText.getText().toString();

        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_APPEND);
            fos.write(fileContent.getBytes());
            fos.close();
            Toast.makeText(this, "File appended", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile() {
        fileName = fileNameEditText.getText().toString();

        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[100];
            String s = "";
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;
            }
            fileOutputTextView.setText(s);
            Toast.makeText(this, "File read", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile() {
        fileName = fileNameEditText.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete the file?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFile(fileName);
                        Toast.makeText(FileActivity.this, "File deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}