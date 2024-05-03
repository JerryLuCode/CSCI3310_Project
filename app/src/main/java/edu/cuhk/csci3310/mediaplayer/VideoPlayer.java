package edu.cuhk.csci3310.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Objects;

public class VideoPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();  // remove the title bar
        setContentView(R.layout.activity_video_player);


        // ---- PIP ----
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            setPictureInPictureParams(new PictureInPictureParams.Builder()
                    .setAutoEnterEnabled(true)
                    .build());
        }
    }

}