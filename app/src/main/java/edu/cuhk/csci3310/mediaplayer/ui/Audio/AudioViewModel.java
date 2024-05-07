package edu.cuhk.csci3310.mediaplayer.ui.Audio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//Den här sparar datan!!! typ class för data collection
public class AudioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AudioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}