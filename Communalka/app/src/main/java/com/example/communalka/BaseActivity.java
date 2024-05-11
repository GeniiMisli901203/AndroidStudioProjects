package com.example.communalka;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

    private Typeface customFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/your_font_file.ttf");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        applyCustomFont(findViewById(android.R.id.content));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        applyCustomFont(view);
    }

    private void applyCustomFont(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(customFont);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyCustomFont(viewGroup.getChildAt(i));
            }
        }
    }
}
