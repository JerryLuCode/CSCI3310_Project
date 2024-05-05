package edu.cuhk.csci3310.mediaplayer.ui.AudioPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import edu.cuhk.csci3310.mediaplayer.R;
import edu.cuhk.csci3310.mediaplayer.MediaModel;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv, currentTimeTv, totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay, nextBtn, prevBtn, musicIcon;
    ArrayList<MediaModel> songsList;
    MediaModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    ImageView shareBtn;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.prev);
        musicIcon = findViewById(R.id.music_icon_big);
        shareBtn = findViewById(R.id.share_button);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri fileUri = Uri.parse(currentSong.getPath());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                // Put the URI of the file to be shared
                sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                sendIntent.setType("audio/*");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        titleTv.setSelected(true);

        songsList = (ArrayList<MediaModel>) getIntent().getSerializableExtra("LIST");

        setResourceWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(mCurrentPosition);
                    currentTimeTv.setText(convertToMMSS(String.valueOf(mCurrentPosition)));
                    if (mediaPlayer.isPlaying()) {
                        musicIcon.setRotation(x++);
                    } else {
                        musicIcon.setRotation(x);
                    }
                }
                new Handler().postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                    currentTimeTv.setText(convertToMMSS(String.valueOf(progress)));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null){
                    mediaPlayer.start();
                }
            }
        });
    }

    void setResourceWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSong.getTitle());
        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v -> pausePlay());
        nextBtn.setOnClickListener(v -> playNextSong());
        prevBtn.setOnClickListener(v -> playPrevSong());

        playMusic();
    }

    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void playNextSong() {
        // Loop back to the first song if the last song is playing
        if (MyMediaPlayer.currentIndex == songsList.size() - 1) {
            MyMediaPlayer.currentIndex = 0;
        } else {
            MyMediaPlayer.currentIndex++;
        }
        mediaPlayer.reset();
        setResourceWithMusic();
    }

    private void playPrevSong() {
        if (MyMediaPlayer.currentIndex == 0) {
            MyMediaPlayer.currentIndex = songsList.size() - 1;
        } else {
            MyMediaPlayer.currentIndex--;
        }
        mediaPlayer.reset();
        setResourceWithMusic();
    }

    private void pausePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        } else {
            mediaPlayer.start();
            pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
        }
    }

    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pausePlay();
    }
}