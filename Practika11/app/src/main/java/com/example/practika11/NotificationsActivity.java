package com.example.practika11;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class NotificationsActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "example_channel";
    private static final String DELAYED_CHANNEL_ID = "delayed_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        createNotificationChannel(CHANNEL_ID, "Example Channel", "Channel for example notifications");
        createNotificationChannel(DELAYED_CHANNEL_ID, "Delayed Notifications", "Channel for delayed example notifications");

        Button notifyButton = findViewById(R.id.notifyButton);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(CHANNEL_ID, "Простое уведомление", "Тестовое уведомление");
            }
        });

        Button delayedNotifyButton = findViewById(R.id.delayedNotifyButton);
        delayedNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleDelayedNotification(DELAYED_CHANNEL_ID, "Уведомление с задержкой", "Это тестовое уведомление с задержкой", 5000);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, CharSequence name, String description) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void sendNotification(String channelId, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }

    private void scheduleDelayedNotification(String channelId, String title, String text, long delayMillis) {
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("channelId", channelId);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("text", text);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long futureInMillis = SystemClock.elapsedRealtime() + delayMillis;
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
