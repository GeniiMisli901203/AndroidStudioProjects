package com.example.practika5;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class AppleActivity extends AppCompatActivity {
    ArrayList<String> users = new ArrayList<String>();
    ArrayList<String> selectedUsers = new ArrayList<String>();
    ArrayAdapter<String> usersAdapter;
    ListView usersListView;
    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apple);

        usersListView = findViewById(R.id.list_view_apple);
        String[] userApple = getResources().getStringArray(R.array.Apple);
        Collections.addAll(users, userApple);
        usersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, users);
        usersListView.setAdapter(usersAdapter);
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = usersAdapter.getItem(position);
                if (usersListView.isItemChecked(position))
                    selectedUsers.add(user);
                else
                    selectedUsers.remove(user);
            }
        });
    }
    public void add(View view){
        EditText userName = findViewById(R.id.editText);
        String user = userName.getText().toString();
        if(!user.isEmpty()){
            usersAdapter.add(user);
            userName.setText("");
            usersAdapter.notifyDataSetChanged();
        }
    }
    public void remove(View view){
        // получаем и удаляем выделенные элементы
        for(int i=0; i< selectedUsers.size();i++){
            usersAdapter.remove(selectedUsers.get(i));
        }
        // снимаем все ранее установленные отметки
        usersListView.clearChoices();
        // очищаем массив выбраных объектов
        selectedUsers.clear();
        usersAdapter.notifyDataSetChanged();
    }
}
