package com.example.practika5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class CucumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cucumber);

        RecyclerView list = findViewById(R.id.recyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(R.drawable.cucumber1, "Сорт Абсолют"));
        items.add(new Item(R.drawable.cucumber2, "Сорт Адмирал"));
        items.add(new Item(R.drawable.cucumber3, "Сорт Вагнер"));
        items.add(new Item(R.drawable.cucumber4, "Сорт Провансаль"));


        SimpleAdapter adapter = new SimpleAdapter(this, items);
        list.setAdapter(adapter);
    }

    public void onBack(View view) {finish();}
}