package edu.cuhk.csci3310.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class VideoPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        /* final ImageButton return_btn = findViewById(R.id.return_button);
        return_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("return_btn", "clicked");
            }
        });*/

        /*final Button test_btn = findViewById(R.id.test_button);
        test_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("current_timestamp", Long.toString(.getContentPosition()));
            }

        });*/
    }
}