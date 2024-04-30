package edu.cuhk.csci3310.mediaplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoPlayerFragment extends Fragment {
    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        PlayerView playerView = rootView.findViewById(R.id.player_view);
        ExoPlayer player = new ExoPlayer.Builder(getContext()).build();
        String videoUri = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4";
        MediaItem mediaItem = MediaItem.fromUri(videoUri);  // Video

        playerView.setPlayer(player);   // Attach player to the view
        player.setMediaItem(mediaItem); // Set the media item
        player.prepare();   // Prepare player
        player.play();  // Play the video

        return rootView;

    }
}