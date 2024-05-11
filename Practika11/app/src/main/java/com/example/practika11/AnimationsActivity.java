package com.example.practika11;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AnimationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);


        ImageView imageView = findViewById(R.id.rotateImageView);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotateAnim.setDuration(2000);
        rotateAnim.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnim.setRepeatMode(ObjectAnimator.RESTART);
        rotateAnim.start();


        final Button moveButton = findViewById(R.id.moveButton);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator moveAnim = ObjectAnimator.ofFloat(moveButton, "translationX", 0f, 300f);
                moveAnim.setDuration(1000);
                moveAnim.start();
            }
        });

        TextView scaleText = findViewById(R.id.scaleTextView);
        scaleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(scaleText, "scaleX", 1f, 2f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(scaleText, "scaleY", 1f, 2f);
                scaleX.setDuration(1000);
                scaleY.setDuration(1000);
                scaleX.start();
                scaleY.start();
            }
        });
    }
}
