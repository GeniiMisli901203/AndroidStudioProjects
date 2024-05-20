package com.example.prikol;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.prikol.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    public static final String NOTIFICATION_CHANNEL_NAME = "Notification Channel Name";

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    @Override
    public void onCreate() {
        super.onCreate();

        // Создаем канал уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Создаем уведомление
        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Иконка уведомления
                .setContentTitle("Приложение") // Заголовок уведомления
                .setContentText("Привет") // Текст уведомления
                .setAutoCancel(true); // Уведомление исчезает после нажатия на него

        // Преобразуем уведомление в PendingIntent
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentIntent(pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Запускаем сервис в фоновом режиме
        startForeground(1, notificationBuilder.build());

        // Запускаем таймер, который будет отправлять уведомления каждую секунду
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Обновляем текст уведомления
                notificationBuilder.setContentText("Ой какой ты пошлый, прям я в афиге");

                // Отправляем уведомление
                notificationManager.notify(1, notificationBuilder.build());
            }
        }, 0, 500);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
