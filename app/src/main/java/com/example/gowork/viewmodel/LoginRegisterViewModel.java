package com.example.gowork.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.model.AppRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> isEmailExistMutableLiveData;

    public LoginRegisterViewModel(@NonNull Application application){
        super(application);

        appRepository = new AppRepository(application);
        userMutableLiveData = appRepository.getUserMutableLiveData();
        isEmailExistMutableLiveData = appRepository.getIsEmailExistMutableLiveData();
    }

    public void register(String email, String password){
        appRepository.register(email, password);
    }

    public void login(String email, String password){
        appRepository.login(email, password);
    }

    public void isEmailExist(String email){
        appRepository.isEmailExist(email);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
