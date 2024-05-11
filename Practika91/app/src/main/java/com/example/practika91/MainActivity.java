package com.example.practika91;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createFileButton = findViewById(R.id.create_file_button);
        createFileButton.setOnClickListener(v -> {
            fileUri = createFileWithScopedStorage(this, "test.txt", "text/plain");
            writeToFile(this, fileUri, "Здрасте");
        });

        Button readFileButton = findViewById(R.id.read_file_button);
        readFileButton.setOnClickListener(v -> {
            if (fileUri != null) {
                Intent intent = new Intent(this, SecondActivity.class);
                intent.setData(fileUri);
                startActivity(intent);
            }
        });

        Button deleteFileButton = findViewById(R.id.delete_file_button);
        deleteFileButton.setOnClickListener(v -> {
            if (fileUri != null) {
                deleteFile(this, fileUri);
                fileUri = null;
            }
        });
    }

    private Uri createFileWithScopedStorage(Context context, String displayName, String mimeType) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/" + displayName);
        return context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
    }

    private void writeToFile(Context context, Uri uri, String text) {
        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(Context context, Uri uri) {
        context.getContentResolver().delete(uri, null, null);
    }
}
