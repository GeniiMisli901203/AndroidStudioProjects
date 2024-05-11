package com.example.practika101;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practika101.R;


public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this);

        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        resultTextView = findViewById(R.id.result_text_view);

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();

                Contact contact = new Contact(name, phone, email, address);
                dbHelper.addContact(contact);

                resultTextView.setText("Contact added: " + contact);
            }
        });

        Button findButton = findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();

                // Find the contact by name in the database
                Contact foundContact = dbHelper.getContactByName(name);

                if (foundContact != null) {
                    resultTextView.setText("Contact found: " + foundContact);
                } else {
                    resultTextView.setText("Contact not found: " + name);
                }
            }
        });

        Button updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();

                // Find the contact by name in the database
                Contact foundContact = dbHelper.getContactByName(name);

                if (foundContact != null) {
                    // Create a new Contact object with new values
                    Contact updatedContact = new Contact(foundContact.getId(), name, phone, email, address);

                    // Update the contact in the database
                    dbHelper.updateContact(updatedContact);

                    // Display a success message
                    Toast.makeText(MainActivity.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Contact not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();

                // Find the contact by name in the database
                Contact foundContact = dbHelper.getContactByName(name);

                if (foundContact != null) {
                    // Delete the contact from the database
                    dbHelper.deleteContact(foundContact.getId());

                    resultTextView.setText("Contact deleted: " + name);
                } else {
                    resultTextView.setText("Contact not found: " + name);
                }
            }
        });
    }
}
