package com.example.practika6;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.practika6.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.Button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }


        });


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Настройки ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true); // Показать кнопку назад
            actionBar.setTitle("Главная"); // Установить заголовок
            // Другие настройки ActionBar
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottom_navigation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // Обработка выбора раздела "Домой"
                Toast.makeText(MainActivity.this, "Домой", Toast.LENGTH_LONG).show();

                return true;
            } else if (item.getItemId() == R.id.settings) {
                // Обработка выбора раздела "Настройки"
                Toast.makeText(MainActivity.this, "Настройки", Toast.LENGTH_LONG).show();
                return true;
            }
            return false;

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Определение нажатого элемента
        if (item.getItemId() == R.id.home) {
            // Обработка выбора раздела "Домой"
            Toast.makeText(MainActivity.this, "Домой", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.settings) {
            // Обработка выбора раздела "Настройки"
            Toast.makeText(MainActivity.this, "Настройки", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
