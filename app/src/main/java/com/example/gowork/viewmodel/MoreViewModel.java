package com.example.gowork.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.model.AppRepository;
import com.google.firebase.auth.FirebaseUser;

public class MoreViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<FirebaseUser> userData;
    private LiveData<Boolean> logOutData;

    public MoreViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
        userData = appRepository.getUserData();
        logOutData = appRepository.getLogoutData();

    }

    public void logOut() {
        appRepository.logOut();
    }

    public LiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public LiveData<Boolean> getLogOutData() {
        return logOutData;
    }
}
