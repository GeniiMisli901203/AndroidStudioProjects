package com.example.communalka;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountFragment extends Fragment {

    private ActivityResultLauncher<String> imagePickerLauncher;
    private ImageView profileImageView;
    private Uri profileImageUri;
    private TextView usernameTextView;
    private TextView errorMessageTextView;
    private static final String KEY_IMAGE_URI = "image_uri";
    private Uri imageUri;
    private EditText nameEditText;
    private Button saveNameButton;
    private String userEmail;

    private CountersDatabaseHelper dbHelper;
    private DatabaseHelper dbHelper1;
    private TextView lightT1TextView;
    private TextView lightT2TextView;
    private TextView lightT3TextView;
    private TextView hotWaterTextView;
    private TextView coldWaterTextView;

    public AccountFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_IMAGE_URI, imageUri);
        dbHelper1 = new DatabaseHelper(requireContext());

        outState.putString("light_t1_text", lightT1TextView.getText().toString());
        outState.putString("light_t2_text", lightT2TextView.getText().toString());
        outState.putString("light_t3_text", lightT3TextView.getText().toString());
        outState.putString("hot_water_text", hotWaterTextView.getText().toString());
        outState.putString("cold_water_text", coldWaterTextView.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        nameEditText = view.findViewById(R.id.name_edit_text);
        saveNameButton = view.findViewById(R.id.save_name_button);
        profileImageView = view.findViewById(R.id.profile_image_view);
        usernameTextView = view.findViewById(R.id.username_text_view);

        lightT1TextView = view.findViewById(R.id.light_t1_text_view);
        lightT2TextView = view.findViewById(R.id.light_t2_text_view);
        lightT3TextView = view.findViewById(R.id.light_t3_text_view);
        hotWaterTextView = view.findViewById(R.id.hot_water_text_view);
        coldWaterTextView = view.findViewById(R.id.cold_water_text_view);
        dbHelper = new CountersDatabaseHelper(requireContext());

        dbHelper1 = new DatabaseHelper(requireContext());
        int userID=1;
        userEmail = dbHelper1.getUserEmail(userID);

        imagePickerLauncher = registerForActivityResult(new GetContent(),

                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            profileImageUri = result;
                            profileImageView.setImageURI(profileImageUri);

                            try {
                                final InputStream imageStream = requireActivity().getContentResolver().openInputStream(profileImageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                profileImageView.setImageBitmap(selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(requireActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
                        }
                    }
                });

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(KEY_IMAGE_URI);
            profileImageView.setImageURI(imageUri);
        }
        saveNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                int userId = dbHelper1.getUserId(userEmail); // Получаем userId
                dbHelper1.saveUserName(name, userId); // Передаем name и userId
                usernameTextView.setText(name);
            }
        });
        String userEmail = dbHelper1.getUserEmail(userID);
        if (userEmail != null) {
            usernameTextView.setText(userEmail);
        } else {
            usernameTextView.setText("User not found");
        }

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerLauncher.launch("image/*");
            }
        });


        updateUserData();

        return view;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            String profileImageUriString = savedInstanceState.getString("profile_image_uri");
            if (profileImageUriString != null) {
                profileImageUri = Uri.parse(profileImageUriString);
                profileImageView.setImageURI(profileImageUri);
            }
            String lightT1Text = savedInstanceState.getString("light_t1_text");
            String lightT2Text = savedInstanceState.getString("light_t2_text");
            String lightT3Text = savedInstanceState.getString("light_t3_text");
            String hotWaterText = savedInstanceState.getString("hot_water_text");
            String coldWaterText = savedInstanceState.getString("cold_water_text");

            lightT1TextView.setText(lightT1Text);
            lightT2TextView.setText(lightT2Text);
            lightT3TextView.setText(lightT3Text);
            hotWaterTextView.setText(hotWaterText);
            coldWaterTextView.setText(coldWaterText);
        }
    }

    private void updateUserData() {
        // Get data from the Counters table
        Cursor cursor = dbHelper.getReadableDatabase().query(
                CounterContract.CounterEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CounterContract.CounterEntry._ID + " DESC",
                "1"
        );

        if (cursor.moveToFirst()) {
            int lightT1Index = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T1);
            int lightT2Index = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T2);
            int lightT3Index = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_LIGHT_T3);
            int hotWaterIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_HOT_WATER);
            int coldWaterIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_COLD_WATER);

            if (lightT1Index != -1 && lightT2Index != -1 && lightT3Index != -1 && hotWaterIndex != -1 && coldWaterIndex != -1) {
                double lightT1 = cursor.getDouble(lightT1Index);
                double lightT2 = cursor.getDouble(lightT2Index);
                double lightT3 = cursor.getDouble(lightT3Index);
                double hotWater = cursor.getDouble(hotWaterIndex);
                double coldWater = cursor.getDouble(coldWaterIndex);

                lightT1TextView.setText("Light T1: " + lightT1);
                lightT2TextView.setText("Light T2: " + lightT2);
                lightT3TextView.setText("Light T3: " + lightT3);
                hotWaterTextView.setText("Hot water: " + hotWater);
                coldWaterTextView.setText("Cold water: " + coldWater);
            } else {
                Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show();

        }

        cursor.close();

        int userId = dbHelper1.getUserId(userEmail);

        String userName = dbHelper1.getUserName(userId);
        if (userName != null) {
            usernameTextView.setText(userName);
        } else {
            usernameTextView.setText("User not found");
        }
    }
}