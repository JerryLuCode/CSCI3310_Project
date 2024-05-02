package edu.cuhk.csci3310.mediaplayer.ui.Audio;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import edu.cuhk.csci3310.mediaplayer.databinding.FragmentHomeBinding;
import edu.cuhk.csci3310.mediaplayer.RecyclerAdapter;

//Kolla om man kan läsa video eller audio folder, eller skapa en public folder man kan läsa ifrån (efter 2ndra)
public class AudioFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private LinkedList<String> imagePathList = new LinkedList<>();
    private LinkedList<String> audioTitleList = new LinkedList<>();
    private LinkedList<String> audioLengthList = new LinkedList<>();

    private static final String LOG_TAG = AudioFragment.class.getSimpleName();

    private final String drawFilePath = "android.resource://edu.cuhk.csci3310.mediaplayer/drawable/";
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        AudioViewModel audioViewModel =
                new ViewModelProvider(this).get(AudioViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //Opens sdcard/android/data/edu.cuhk.csci3310.mediaplayer/Downloads
        File downloadFolder = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);


        //readFiles and initialize lists
        ArrayList<File> audioFiles = getAudioFiles(downloadFolder);

        audioTitleList.clear();
        audioLengthList.clear();
        imagePathList.clear();
        parseAudioFiles(audioFiles);


        createRecyclerView(root);

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    public void createRecyclerView(View root)
    {
        // Get a handle to the RecyclerView.
        mRecyclerView = binding.recyclerview;
        // Create an adapter and supply the data to be displayed,
        // initially just a list of image path
        mAdapter = new RecyclerAdapter(requireContext(), root, imagePathList, audioTitleList, audioLengthList);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
       mRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
    }

    //få den att fungers
    public ArrayList<File> getAudioFiles(File downloadFolder)
    {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = downloadFolder.listFiles();
        for(File singlefile:files)
        {
            if(singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(getAudioFiles(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3")||singlefile.getName().endsWith(".wav"))
                {
                    arrayList.add(singlefile);
                }
            }
        }
        Log.d(LOG_TAG, Integer.toString(arrayList.size()));
        return arrayList;
    }





    public void parseAudioFiles(ArrayList<File> audioList)
    {
        for(int i = 0; i < audioList.size(); i++)
        {
            audioTitleList.add(audioList.get(i).getName().replace(".mp3", "").replace(".wav",""));
            //Fix for audio length and maybe for audio data aswell
            audioLengthList.add("02:47");
            imagePathList.add(drawFilePath+"audio_stock");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}