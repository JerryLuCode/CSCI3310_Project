package edu.cuhk.csci3310.mediaplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.slider.Slider;

import java.io.File;

public class VideoPlayerFragment extends Fragment {
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "edu.cuhk.csci3310.mediaplayer";
    private final String CONTENT_POSITION_KEY = "content_position";
    private ExoPlayer player;
    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getContext();
        // For storing video playback info
        mPreferences = context.getSharedPreferences(sharedPrefFile, context.MODE_PRIVATE);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);

        // ---- VIDEO PLAYBACK ----
        // playerView stores the view of the video player
        PlayerView playerView = rootView.findViewById(R.id.player_view);
        // player stores the Exoplayer
        player = new ExoPlayer.Builder(getContext()).build();
        // videoUri stores the url/file of the video
        String videoUri = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4";
        // mediaItem stores the media for player (video)
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        // Attach player to the view
        playerView.setPlayer(player);
        // Set the media item
        player.setMediaItem(mediaItem);
        // Prepare player
        player.prepare();
        // Resume the video progress after orientation changes or expected crashes of apps
        player.seekTo(mPreferences.getLong(CONTENT_POSITION_KEY, 0));
        // Play the video
        player.play();

        // ---- Volume Slider ----
        Slider volumeSlider = rootView.findViewById(R.id.volumeSlider);
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
                } else volumeSlider.setVisibility(View.INVISIBLE);
            }
        });

        return rootView;
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
        player.stop();
    }

    private void saveVideoProgress() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(CONTENT_POSITION_KEY, player.getContentPosition());
        editor.apply();
    }
}