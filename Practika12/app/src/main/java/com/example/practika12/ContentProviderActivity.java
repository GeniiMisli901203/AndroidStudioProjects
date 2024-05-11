package com.example.practika12;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContentProviderActivity extends AppCompatActivity {

    private TextView resultTextView;
    private static final int REQUEST_READ_CONTACTS = 1;
    private static final int REQUEST_WRITE_CONTACTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);

        resultTextView = findViewById(R.id.result_text_view);

        requestReadContactsPermission();
    }

    private void queryContactsProvider() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder builder = new StringBuilder();
            int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);

                builder.append("ID: ").append(id).append("\n");
                builder.append("Name: ").append(name).append("\n\n");
            }
            resultTextView.setText(builder.toString());
            cursor.close();
        }
    }

    private void requestReadContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        } else {
            queryContactsProvider();
        }
    }

    private void requestWriteContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_CONTACTS},
                    REQUEST_WRITE_CONTACTS);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                queryContactsProvider();
            } else {

            }
        } else if (requestCode == REQUEST_WRITE_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }
}
