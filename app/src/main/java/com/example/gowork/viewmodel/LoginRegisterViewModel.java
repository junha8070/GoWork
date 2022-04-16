package com.example.gowork.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gowork.model.AppRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRegisterViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<FirebaseUser> userData;
    private LiveData<Boolean> isEmailExistData;
    private LiveData<FirebaseFirestore> userInfoData;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
        userData = appRepository.getUserData();
        isEmailExistData = appRepository.getIsEmailExistData();
        userInfoData = appRepository.getUserInfoData();
    }

    public void register(String email, String password) {
        appRepository.register(email, password);
    }

    public void login(String email, String password) {
        appRepository.login(email, password);
    }

    public void isEmailExist(String email) {
        appRepository.isEmailExist(email);
    }

    public void uploadUserInfo(String email, String name, String phoneNum) {
        appRepository.uploadUserInfo(email, name, phoneNum);
    }

    public LiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public LiveData<Boolean> getIsEmailExistData() {
        return isEmailExistData;
    }

    public LiveData<FirebaseFirestore> getUserInfoData() {
        return userInfoData;
    }
}
