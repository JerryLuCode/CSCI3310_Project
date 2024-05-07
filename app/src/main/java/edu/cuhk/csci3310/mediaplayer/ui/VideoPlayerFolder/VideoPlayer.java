package edu.cuhk.csci3310.mediaplayer.ui.VideoPlayerFolder;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.google.android.material.slider.Slider;

import java.io.File;

import edu.cuhk.csci3310.mediaplayer.R;

public class VideoPlayer extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "edu.cuhk.csci3310.mediaplayer";
    private final String ARG_CONTENT_POSITION = "content_position";
    private PlayerView playerView;
    private ExoPlayer player;
    private Slider volumeSlider;

    ImageView shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove the top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_video_player);
        Context context = this;
        // For storing video playback info
        mPreferences = context.getSharedPreferences(sharedPrefFile, context.MODE_PRIVATE);


        // ---- VIDEO PLAYBACK ----
        // playerView stores the view of the video player
        playerView = findViewById(R.id.player_view);
        // player stores the Exoplayer
        player = new ExoPlayer.Builder(this).build();
        // videoUri stores the url/file of the video
        Intent intent = getIntent();
        String videoUri = intent.getStringExtra("URI");
        // mediaItem stores the media for player (video)
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        // Attach player to the view
        playerView.setPlayer(player);
        // Set the media item
        player.setMediaItem(mediaItem);
        // Prepare player
        player.prepare();
        // Resume the video progress after orientation changes,
        Log.d("ContentDuration", Long.toString(getVideoLength(getVideoUri(mediaItem))));
        if (mPreferences.getLong(ARG_CONTENT_POSITION, 0) <= getVideoLength(getVideoUri(mediaItem)))
            player.seekTo(mPreferences.getLong(ARG_CONTENT_POSITION, 0));
        // Play the video
        player.play();

        shareBtn = findViewById(R.id.share_button_video);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri fileUri = Uri.parse(videoUri);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                // Put the URI of the file to be shared
                sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                sendIntent.setType("video/*");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        // ---- Volume Slider ----
        volumeSlider = findViewById(R.id.volumeSlider);
        volumeSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Log.d("Slider Change", Float.toString(value));
                player.setVolume(value/100);
            }
        });

        playerView.setControllerVisibilityListener(new PlayerView.ControllerVisibilityListener() {
            @Override
            public void onVisibilityChanged(int visibility) {
                Log.d("VisibilityChanged", Integer.toString(visibility));
                if (visibility == View.VISIBLE) {
                    volumeSlider.setVisibility(View.VISIBLE);
                    shareBtn.setVisibility(View.VISIBLE);
                } else {
                    volumeSlider.setVisibility(View.INVISIBLE);
                    shareBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        // ---- PIP ----
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            setPictureInPictureParams(new PictureInPictureParams.Builder()
                    .setAutoEnterEnabled(true)
                    .build());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        saveVideoProgress();
        player.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveVideoProgress();
        // Stop and release the player
        player.stop();
        player.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release the player
        player.release();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (isInPictureInPictureMode) {
            playerView.setUseController(false);
            volumeSlider.setVisibility(View.INVISIBLE);
        } else {
            playerView.setUseController(true);
        }
        player.play();
    }

    private void saveVideoProgress() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(ARG_CONTENT_POSITION, player.getContentPosition());
        editor.apply();
    }

    private long getVideoLength(Uri uri) {
        /*
            Input: Uri
            Output: length of the video at uri (in long)
         */
        long timeInMillisec = 0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getApplicationContext(), uri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (time != null) timeInMillisec = Long.parseLong(time);

            retriever.release();

        } catch (Exception e) {
            Log.e("getVideoLength", e.toString());
        }
        return timeInMillisec;
    }

    private Uri getVideoUri(MediaItem mediaItem) {
        // getVideoLength(mediaItem.localConfiguration.uri): Uri of the video
        if (mediaItem.localConfiguration != null) {
            return mediaItem.localConfiguration.uri;
        } else {
            Log.e("getVideoUri", "Unable to retrieve video uri!");
            return Uri.parse("");
        }
    }
}
