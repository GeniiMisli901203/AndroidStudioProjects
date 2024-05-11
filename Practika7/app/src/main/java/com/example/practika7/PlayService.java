package com.example.practika7;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class PlayService extends Service {

    private MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Служба создана", Toast.LENGTH_SHORT).show();
        mPlayer = MediaPlayer.create(this, R.raw.lesnik);
        if (mPlayer != null) {
            mPlayer.setLooping(false);
        } else {
            Toast.makeText(this, "Ошибка при создании MediaPlayer", Toast.LENGTH_SHORT).show();
            stopSelf();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба запущена", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            mPlayer.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            mPlayer.stop();
        }

    }
}
