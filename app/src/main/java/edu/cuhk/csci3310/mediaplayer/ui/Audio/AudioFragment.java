package edu.cuhk.csci3310.mediaplayer.ui.Audio;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import edu.cuhk.csci3310.mediaplayer.databinding.FragmentAudioBinding;
import edu.cuhk.csci3310.mediaplayer.RecyclerAdapter;
import edu.cuhk.csci3310.mediaplayer.MediaModel;

public class AudioFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    ArrayList<MediaModel> songsList = new ArrayList<>();

    private final String drawFilePath = "android.resource://edu.cuhk.csci3310.mediaplayer/drawable/";
    private FragmentAudioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAudioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Get the audio files from storage
        songsList.clear();
        updateSongsList();

        //Create the recyclerList
        createRecyclerView(root);

        return root;
    }

    public void createRecyclerView(View root)
    {
        //Get a handle to the RecyclerView.
        mRecyclerView = binding.recyclerview;
        //Create an adapter and supply the data to be displayed,
        //initially just a list of image path
        mAdapter = new RecyclerAdapter(requireContext(), root, requireActivity().
                getSupportFragmentManager(),"audio", songsList);

        //Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        //Give the RecyclerView a default layout manager.
       mRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
    }

    //Gets songs from storage
    private void updateSongsList() {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };

        //Selection and sorting order
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        //Go through each media item and save it to MediaModel object
        try (Cursor cursor = requireActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder)) {
            if (cursor != null && cursor.moveToFirst()) {
                do{
                    String title = cursor.getString(0);
                    String path = cursor.getString(1);
                    String duration = cursor.getString(2);
                    // tackle media file does not have HDR metadata
                    if (title == null || path == null || duration == null) {
                        continue;
                    }
                    MediaModel songData = new MediaModel(path, title, duration, drawFilePath+"audio_stock");
                    if(new File(songData.getPath()).exists())
                        songsList.add(songData);
                }
                while (cursor.moveToNext());
            }
            Log.d("AudioFragment", "No of songs: " + songsList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}