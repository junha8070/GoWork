package com.example.gowork;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class AuthViewModel extends AndroidViewModel {
    private AuthModel authModel;
    private LiveData<Task> registerSuccess;
    private SingleLiveEvent<Task> loginSuccess;
    private LiveData<FirebaseUser> firebaseUserLiveData;
    private HashMap<String, Object> loginInfo;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        authModel = new AuthModel(application);

        loginInfo = authModel.getPreferenceString();
        registerSuccess = authModel.getRegisterSuccessful();
        loginSuccess = authModel.getLoginSuccessful();
        firebaseUserLiveData = authModel.getFirebaseUser();
    }

    public void register(String id, String pw){
        authModel.register(id, pw);
    }

    public void login(String id, String pw) { authModel.login(id, pw); }

    public void setLoginInfo(HashMap<String, Object> loginInfo){
        authModel.setPreference(loginInfo);
    }

    public void logout(){
        authModel.logout();
    }

    public LiveData<Task> getRegisterSuccess(){
        return registerSuccess;
    }

    public LiveData<Task> getLoginSuccess(){ return loginSuccess; }

    public LiveData<FirebaseUser> getFirebaseUserLiveData() { return firebaseUserLiveData; }

    public HashMap<String, Object> getLoginInfo(){
        return loginInfo;
    }
}
