package com.pagupa.notiapp.ui.detailorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailOrderViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DetailOrderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}