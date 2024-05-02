package edu.cuhk.csci3310.mediaplayer.ui.audio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.cuhk.csci3310.mediaplayer.R;

public class AudioFragment extends Fragment {

    RecyclerView audioRecyclerView;
    TextView noMusicTextView;
    ArrayList<SongModel> songsList = new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        audioRecyclerView = view.findViewById(R.id.audio_recycler_view);
        noMusicTextView = view.findViewById(R.id.no_songs_text);

        if (!checkPermission()) {
            requestPermission();
        } else {
            updateSongsList();
        }

        return view;
    }

    boolean checkPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), "Permission needed to read audio files", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (audioRecyclerView != null) {
            audioRecyclerView.setAdapter(new MusicListAdapter(songsList, requireContext()));
        }
        updateSongsList();
    }

    private void updateSongsList() {
        songsList.clear();

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        try (Cursor cursor = requireActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    SongModel songData = new SongModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
                    if (new File(songData.getPath()).exists()) {
                        songsList.add(songData);
                    }
//                    else {
//                        Toast.makeText(requireContext(), "File doesn't exist: " + songData.getPath(), Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        }
//        Cursor cursor = requireActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
//        while(cursor.moveToNext()){
//            SongModel songData = new SongModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
//            if(new File(songData.getPath()).exists())
//                songsList.add(songData);
//        }

        if (songsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            noMusicTextView.setVisibility(View.GONE);
            audioRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            audioRecyclerView.setAdapter(new MusicListAdapter(songsList, requireContext()));
        }

        // Log all the songs
        for (SongModel song : songsList) {
            System.out.println(song.getTitle() + " - " + song.getPath());
        }
    }
}