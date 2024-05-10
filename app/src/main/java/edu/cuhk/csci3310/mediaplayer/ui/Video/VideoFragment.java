package edu.cuhk.csci3310.mediaplayer.ui.Video;

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

public class VideoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    ArrayList<MediaModel> videoList = new ArrayList<>();

    private final String drawFilePath = "android.resource://edu.cuhk.csci3310.mediaplayer/drawable/";
    private FragmentAudioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAudioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Get video files from storage
        videoList.clear();
        updateVideoList();

        //Create recyclerList
        createRecyclerView(root);

        return root;
    }

    public void createRecyclerView(View root)
    {
        //Get a handle to the RecyclerView.
        mRecyclerView = binding.recyclerview;
        //Create an adapter and supply the data to be displayed,
        //initially just a list of image path
        mAdapter = new RecyclerAdapter(requireContext(), root, requireActivity().getSupportFragmentManager(),
                "video", videoList);

        //Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        //Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
    }

    private void updateVideoList() {
        String[] projection = {
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
        };

        //Selection and sort order
        String selection = MediaStore.Video.Media.DATA + " IS NOT NULL";
        String sortOrder = MediaStore.Video.Media.TITLE + " ASC";

        //Go through each video file and add to MediaModel
        try (Cursor cursor = requireActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder)) {
            if (cursor != null && cursor.moveToFirst()) {
                do{
                    MediaModel videoData = new MediaModel(cursor.getString(1),cursor.getString(0),cursor.getString(2), drawFilePath+"movie_stock");
                    if(new File(videoData.getPath()).exists())
                        videoList.add(videoData);
                }
                while (cursor.moveToNext());
            }
            Log.d("AudioFragment", "No of videos: " + videoList.size());
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