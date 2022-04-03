package com.example.gowork.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.model.AppRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> logOutMutableLiveData;

    public LoggedInViewModel(@NonNull Application application){
        super(application);

        appRepository = new AppRepository(application);
        userMutableLiveData = appRepository.getUserMutableLiveData();
        logOutMutableLiveData = appRepository.getLoggoutMutableLiveData();

    }

    public void logOut(){
        appRepository.logOut();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLogOutMutableLiveData() {
        return logOutMutableLiveData;
    }
}
