package com.example.communalka;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        myTextView = findViewById(R.id.my_text_view);

        String infoText = getString(R.string.info_text);

        myTextView.setText(Html.fromHtml(infoText, Html.FROM_HTML_MODE_LEGACY));
    }
}
