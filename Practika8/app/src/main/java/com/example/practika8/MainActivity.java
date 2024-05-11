package com.example.practika8;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practika8.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textViewSequentialTasks;
    private TextView textViewParallelTasks;
    private ImageView imageView;
    private Button buttonLoadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        textViewSequentialTasks = findViewById(R.id.textViewSequentialTasks);
        textViewParallelTasks = findViewById(R.id.textViewParallelTasks);
        buttonLoadImage = findViewById(R.id.buttonLoadImage);

        Button buttonSequential = findViewById(R.id.buttonSequential);
        buttonSequential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSequentialTasks();
            }
        });

        Button buttonParallel = findViewById(R.id.buttonParallel);
        buttonParallel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startParallelTasks();
            }
        });

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadImageTask(imageView).execute();
            }
        });
    }

    private void startSequentialTasks() {
        new SequentialTask(1, textViewSequentialTasks).execute();
        new SequentialTask(2, textViewSequentialTasks).execute();
        new SequentialTask(3, textViewSequentialTasks).execute();
    }

    private void startParallelTasks() {
        new ParallelTask(1, textViewParallelTasks).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new ParallelTask(2, textViewParallelTasks).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // Класс для выполнения задач последовательно
    private static class SequentialTask extends AsyncTask<Void, Void, Void> {
        private int taskNumber;
        private TextView textView;

        public SequentialTask(int taskNumber, TextView textView) {
            this.taskNumber = taskNumber;
            this.textView = textView;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("SequentialTask", "Task " + taskNumber + " started");
            // Имитация работы задачи
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("SequentialTask", "Task " + taskNumber + " finished");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.append("Task " + taskNumber + " finished\n");
        }
    }

    // Класс для выполнения задач параллельно
    private static class ParallelTask extends AsyncTask<Void, Void, Void> {
        private int taskNumber;
        private TextView textView;

        public ParallelTask(int taskNumber, TextView textView) {
            this.taskNumber = taskNumber;
            this.textView = textView;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("ParallelTask", "Task " + taskNumber + " started");
            // Имитация работы задачи
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("ParallelTask", "Task " + taskNumber + " finished");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.append("Task " + taskNumber + " finished\n");
        }
    }

    private static class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL url = new URL("https://random.dog/woof.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); //Получает входной поток данных из соединения и создает BufferedReader для чтения данных из потока.
                StringBuilder json = new StringBuilder(); //Считывает данные из потока построчно и добавляет их в StringBuilder.
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                JSONObject jsonObject = new JSONObject(json.toString()); //Создает JSONObject из полученной строки JSON.
                String imageUrl = jsonObject.getString("url");

                InputStream imageInputStream = new URL(imageUrl).openStream(); //Открывает входной поток для этого URL и декодирует его в объект Bitmap.
                return BitmapFactory.decodeStream(imageInputStream);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
                //Если при выполнении любого из этих действий возникает исключение (IOException или JSONException), оно перехватывается и выводится стек-трейс в лог (e.printStackTrace()), а метод возвращает null.
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
