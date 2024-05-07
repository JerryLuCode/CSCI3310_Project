package edu.cuhk.csci3310.mediaplayer.ui.audio;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import edu.cuhk.csci3310.mediaplayer.R;

public class MusicService extends Service {
    private final IBinder binder = new MusicBinder();
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Add other methods to control the MediaPlayer as needed


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String songPath = intent.getStringExtra("song_path");
            initMediaPlayer(songPath);
            play();
        }
        return START_STICKY;
    }

    private void initMediaPlayer(String songPath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(songPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
    }

    private Notification createNotification() {
        // Create a notification for the service
        Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        // Create a notification channel for the service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(this, "channel_id")
                    .setContentTitle("Music Service")
                    .setContentText("Playing music")
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        return new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Music Service")
                .setContentText("Playing music")
                .setSmallIcon(R.drawable.music_icon)
                .setContentIntent(pendingIntent)
                .build();
    }
}