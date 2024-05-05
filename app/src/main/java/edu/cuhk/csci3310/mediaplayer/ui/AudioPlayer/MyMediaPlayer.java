package edu.cuhk.csci3310.mediaplayer.ui.AudioPlayer;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();
        }
        return instance;
    }

    // the song is not playing
    public static int currentIndex = -1;
}
