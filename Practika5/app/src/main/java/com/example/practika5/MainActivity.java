package com.example.practika5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String[] userNames = { "Яблоки", "Огурцы", "Картошка", "Помидоры" };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // находим представление списка
        ListView usersListView = (ListView) findViewById(R.id.list_view);
        // создаем адаптер
        ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNames);
        // устанавливаем адаптер для списка
        usersListView.setAdapter(usersAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // по позиции получаем выбранный элемент
                String selectedItem = userNames[position];
                if (selectedItem == "Яблоки" ){
                    Intent intent = new Intent(MainActivity.this, AppleActivity.class);
                    startActivity(intent);
                }
                if (selectedItem == "Огурцы"){
                    Intent intent = new Intent(MainActivity.this, CucumberActivity.class);
                    startActivity(intent);
                }
                if (selectedItem == "Картошка"){
                    Intent intent = new Intent(MainActivity.this, Potato.class);
                    startActivity(intent);
                }
                if (selectedItem == "Помидоры"){
                    Intent intent = new Intent(MainActivity.this, Tomato.class);
                    startActivity(intent);
                }
            }
        });

    }



}


