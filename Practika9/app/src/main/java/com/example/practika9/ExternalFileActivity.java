package com.example.practika9;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExternalFileActivity extends AppCompatActivity {

    private EditText editTextFileName;
    private EditText editTextFileContent;
    private TextView textViewFileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_file);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextFileContent = findViewById(R.id.editTextFileContent);
        textViewFileContent = findViewById(R.id.textViewFileContent);

        Button buttonCreateFile = findViewById(R.id.buttonCreateFile);
        Button buttonAppendToFile = findViewById(R.id.buttonAppendToFile);
        Button buttonReadFile = findViewById(R.id.buttonReadFile);
        Button buttonDeleteFile = findViewById(R.id.buttonDeleteFile);

        buttonCreateFile.setOnClickListener(v -> createOrWriteFile(false));
        buttonAppendToFile.setOnClickListener(v -> createOrWriteFile(true));
        buttonReadFile.setOnClickListener(v -> readFileFromExternalStorage(editTextFileName.getText().toString()));
        buttonDeleteFile.setOnClickListener(v -> deleteFileWithConfirmation(editTextFileName.getText().toString()));

        // Восстановление состояния textViewFileContent, если есть сохраненное состояние
        if (savedInstanceState != null) {
            textViewFileContent.setText(savedInstanceState.getString("FileContent"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Сохраняем текстовое содержимое
        outState.putString("FileContent", textViewFileContent.getText().toString());
    }

    private void createOrWriteFile(boolean append) {
        String fileName = editTextFileName.getText().toString();
        String content = editTextFileContent.getText().toString();
        File file = new File(getExternalFilesDir(null), fileName);

        try (FileOutputStream fos = new FileOutputStream(file, append)) {
            fos.write(content.getBytes());
            Toast.makeText(this, append ? "Информация добавлена" : "Файл создан", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при работе с файлом", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void readFileFromExternalStorage(String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);
        StringBuilder content = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append('\n');
            }

            textViewFileContent.setText(content.toString());
            Toast.makeText(this, "Файл прочитан", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при чтении файла", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void deleteFileWithConfirmation(String fileName) {
        new AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы уверены, что хотите удалить этот файл?")
                .setPositiveButton("Да", (dialog, which) -> deleteFileFromExternalStorage(fileName))
                .setNegativeButton("Нет", null)
                .show();
    }

    private void deleteFileFromExternalStorage(String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);
        if (file.delete()) {
            textViewFileContent.setText(""); // Очистка поля содержимого после удаления файла
            Toast.makeText(this, "Файл удален", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при удалении файла", Toast.LENGTH_SHORT).show();
        }
    }
}
