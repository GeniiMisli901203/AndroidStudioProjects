package com.example.communalka;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import android.util.Base64;


import com.example.communalka.R;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private int selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showErrorDialog("Пожалуйста, заполните все поля");
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String[] projection = {
                        UserContract.UserEntry._ID,
                        UserContract.UserEntry.COLUMN_ROLE,
                        UserContract.UserEntry.COLUMN_SALT,
                        UserContract.UserEntry.COLUMN_PASSWORD
                };

                String selection = UserContract.UserEntry.COLUMN_EMAIL + " = ?";
                String[] selectionArgs = {email};

                Cursor cursor = null;
                try {
                    cursor = db.query(
                            UserContract.UserEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null
                    );
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Ошибка входа: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int roleIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_ROLE);
                    int saltIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_SALT);
                    int passwordIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD);

                    if (roleIndex != -1 && saltIndex != -1 && passwordIndex != -1) {
                        int role = cursor.getInt(roleIndex);
                        String storedSalt = cursor.getString(saltIndex);
                        String storedHashedPassword = cursor.getString(passwordIndex);

                        String hashedInputPassword = hashPassword(password, storedSalt);

                        if (hashedInputPassword.equals(storedHashedPassword)) {
                            if (role == 1) {
                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Ошибка входа: неправильный пароль", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка входа: не найден пользователь", Toast.LENGTH_SHORT).show();
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    showErrorDialog("Пожалуйста, заполните все поля");
                    return;
                }
                registerUser(email, password, selectedRole);
            }
        });
    }

    private void registerUser(String email, String password, int role) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_EMAIL, email);
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, hashedPassword);
        values.put(UserContract.UserEntry.COLUMN_SALT, salt);
        values.put(UserContract.UserEntry.COLUMN_ROLE, role);

        try {
            long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
            if (newRowId == -1) {
                Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Вы зарегистрировались", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка регистрации: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ошибка");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.encodeToString(salt, Base64.DEFAULT);
    }

    private String hashPassword(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash;
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeToString(hash, Base64.DEFAULT);
    }
}