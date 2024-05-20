package com.example.prikol9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Запускаем сервис отправки уведомлений
        startService(new Intent(this, NotificationService.class));

        // Находим WebView и кнопку в разметке
        webView = findViewById(R.id.webView);
        button = findViewById(R.id.button);

        // Включаем JavaScript для WebView
        webView.getSettings().setJavaScriptEnabled(true);

        // Загружаем сайт в WebView
        webView.loadUrl("https://tv.bigboss.tel");

        // Настраиваем поведение при нажатии на ссылки в WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Открываем ссылки внутри приложения, а не в браузере
                view.loadUrl(url);
                return true;
            }
        });

        // Скрываем WebView при запуске приложения
        webView.setVisibility(View.GONE);

        // Добавляем обработчик нажатия на кнопку
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Останавливаем отправку уведомлений
                stopService(new Intent(MainActivity.this, NotificationService.class));

                // Открываем WebView
                webView.setVisibility(View.VISIBLE);
            }
        });
    }

    // Метод для остановки отправки уведомлений
    public void stopNotifications(View view) {
        stopService(new Intent(this, NotificationService.class));
    }
}


