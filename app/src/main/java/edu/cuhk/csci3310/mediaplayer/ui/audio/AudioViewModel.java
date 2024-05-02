package edu.cuhk.csci3310.mediaplayer.ui.audio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AudioViewModel extends ViewModel {

    public AudioViewModel() {
        MutableLiveData<String> mText = new MutableLiveData<>();
        mText.setValue("No Audio Files Found!");
    }
}