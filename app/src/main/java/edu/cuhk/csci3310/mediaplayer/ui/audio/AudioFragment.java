package edu.cuhk.csci3310.mediaplayer.ui.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

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
        audioRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        audioRecyclerView.setAdapter(new MusicListAdapter(songsList, requireContext()));
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
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (audioRecyclerView != null) {
            audioRecyclerView.setAdapter(new MusicListAdapter(songsList, requireContext()));
        }
        // updateSongsList();
    }

    private void updateSongsList() {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";


        try (Cursor cursor = requireActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder)) {
            if (cursor != null && cursor.moveToFirst()) {
                while(cursor.moveToNext()){
                    SongModel songData = new SongModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
                    if(new File(songData.getPath()).exists())
                        songsList.add(songData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // log out the no of songs
        Log.d("AudioFragment", "No of songs: " + songsList.size());

        if (songsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            audioRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            audioRecyclerView.setAdapter(new MusicListAdapter(songsList, requireContext()));
        }
    }
//    void updateSongsList() {
//        String[] projection = {
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.DURATION
//        };
//
//        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//
//        Cursor cursor = requireActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,sortOrder);
//        while(cursor.moveToNext()){
//            SongModel songData = new SongModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
//            if(new File(songData.getPath()).exists())
//                songsList.add(songData);
//        }
//
//        if(songsList.size()==0){
//            noMusicTextView.setVisibility(View.VISIBLE);
//        }else{
//            //recyclerview
//            audioRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//            audioRecyclerView.setAdapter(new MusicListAdapter(songsList,requireContext()));
//        }
//    }
}