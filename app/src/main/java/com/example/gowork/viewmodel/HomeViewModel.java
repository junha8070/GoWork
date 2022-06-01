package com.example.gowork.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gowork.model.AppRepository;
import com.example.gowork.model.HomeModel;

public class HomeViewModel extends AndroidViewModel {
    private HomeModel homeModel;
    private LiveData<String> currentTime;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeModel = new HomeModel(application);
        currentTime = homeModel.getCurrentTime();
    }

    public void currentTime(){
        homeModel.currentTime();
    }

    public LiveData<String> getTime() {
        return currentTime;
    }


}
