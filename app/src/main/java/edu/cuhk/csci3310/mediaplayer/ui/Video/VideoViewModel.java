package edu.cuhk.csci3310.mediaplayer.ui.Video;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VideoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}