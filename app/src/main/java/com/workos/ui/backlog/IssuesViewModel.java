package com.workos.ui.backlog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IssuesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IssuesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Backlog fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}