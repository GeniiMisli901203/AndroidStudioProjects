package com.example.practika12;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        resultTextView = findViewById(R.id.result_text_view);


        String jsonString = createJsonUsingGson();
        saveJsonToFile(jsonString);


        String jsonFromFile = readJsonFromFile();
        List<User> userList = parseJsonArrayUsingGson(jsonFromFile);


        displayUserData(userList);
    }

    private String createJsonUsingGson() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("John", 30, "john@example.com"));
        userList.add(new User("Alice", 25, "alice@example.com"));

        Gson gson = new Gson();
        return gson.toJson(userList);
    }

    private void saveJsonToFile(String jsonString) {
        File file = new File(getFilesDir(), "users.json");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonString);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readJsonFromFile() {
        File file = new File(getFilesDir(), "users.json");
        StringBuilder jsonBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                jsonBuilder.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return jsonBuilder.toString();
    }

    private List<User> parseJsonArrayUsingGson(String jsonArrayStr) {
        Gson gson = new Gson();
        Type userListType = new TypeToken<List<User>>() {}.getType();
        return gson.fromJson(jsonArrayStr, userListType);
    }

    private void displayUserData(List<User> userList) {
        if (userList != null) {
            StringBuilder builder = new StringBuilder();
            for (User user : userList) {
                builder.append("Name: ").append(user.name).append("\n");
                builder.append("Age: ").append(user.age).append("\n");
                builder.append("Email: ").append(user.email).append("\n\n");
            }
            resultTextView.setText(builder.toString());
        } else {
            resultTextView.setText("Failed to parse JSON");
        }
    }

    public static class User {
        public String name;
        public int age;
        public String email;

        public User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
    }
}
