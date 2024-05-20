package com.example.communalka;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationService extends Service {

    private static final long NOTIFICATION_INTERVAL = 24 * 60 * 60 * 1000;
    private HandlerThread handlerThread;
    private Handler handler;

    private static final String CHANNEL_ID = "channel_id";

    @Override
    public void onCreate() {
        super.onCreate();

        handlerThread = new HandlerThread("NotificationService");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        checkNotification();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNotification();
                handler.postDelayed(this, NOTIFICATION_INTERVAL);
            }
        }, NOTIFICATION_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkNotification() {
        String currentDate = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        int currentDay = Integer.parseInt(currentDate);

        if (currentDay == 1) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(
                    CounterContract.CounterEntry.TABLE_NAME,
                    new String[]{CounterContract.CounterEntry.COLUMN_TIMESTAMP},
                    null,
                    null,
                    null,
                    null,
                    CounterContract.CounterEntry._ID + " DESC",
                    "1"
            );

            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    sendNotification();
                } else {
                    cursor.moveToFirst();
                    int timestampIndex = cursor.getColumnIndex(CounterContract.CounterEntry.COLUMN_TIMESTAMP);
                    if (timestampIndex != -1) {

                        if (!isSameMonth(cursor.getLong(timestampIndex))) {
                            sendNotification();
                        }
                    }
                }
            }
            cursor.close();
        }
    }

    private boolean isSameMonth(long timestamp) {
        String dateFromDb = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date(timestamp));
        String currentDate = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        return dateFromDb.equals(currentDate);
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Подсчет коммунальных услуг")
                .setContentText("Напоминание: введите показания счетчиков")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    }
}