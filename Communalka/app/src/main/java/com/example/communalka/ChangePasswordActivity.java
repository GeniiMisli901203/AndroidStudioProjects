package com.example.communalka;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button changePasswordButton;
    private String email;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPasswordEditText = findViewById(R.id.current_password_edit_text);
        newPasswordEditText = findViewById(R.id.new_password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        changePasswordButton = findViewById(R.id.change_password_button);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = dbHelper.getUserId("user");
                email = dbHelper.getUserEmail(userId);

                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty() || currentPassword.isEmpty()) {
                    showErrorDialog("Пожалуйста, заполните все поля");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    showErrorDialog("Пароли не совпадают");
                    return;
                }

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
                    Toast.makeText(ChangePasswordActivity.this, "Ошибка входа: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                        String hashedInputPassword = hashPassword(currentPassword, storedSalt);

                        if (hashedInputPassword.equals(storedHashedPassword))
                        {
                            String newSalt = generateSalt();
                            String newHashedPassword = hashPassword(newPassword, newSalt);

                            ContentValues values = new ContentValues();
                            values.put(UserContract.UserEntry.COLUMN_PASSWORD, newHashedPassword);
                            values.put(UserContract.UserEntry.COLUMN_SALT, newSalt);

                            int rowsAffected = db.update(UserContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);

                            if (rowsAffected > 0) {
                                Toast.makeText(ChangePasswordActivity.this, "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Ошибка изменения пароля", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Ошибка входа: неправильный пароль", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Ошибка входа: не найден пользователь", Toast.LENGTH_SHORT).show();
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        });
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