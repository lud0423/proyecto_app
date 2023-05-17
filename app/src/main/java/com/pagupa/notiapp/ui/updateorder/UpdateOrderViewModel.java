package com.pagupa.notiapp.ui.updateorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdateOrderViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UpdateOrderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}