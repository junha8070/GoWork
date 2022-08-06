package com.example.gowork;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    private AuthModel authModel;
    private LiveData<Task> registerSuccess;
    private SingleLiveEvent<Task> loginSuccess;
    private SingleLiveEvent<FirebaseUser> firebaseUserLiveData;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        authModel = new AuthModel(application);
        registerSuccess = authModel.getRegisterSuccessful();
        loginSuccess = authModel.getLoginSuccessful();
        firebaseUserLiveData = authModel.getFirebaseUser();
    }

    public void register(String id, String pw){
        authModel.register(id, pw);
    }

    public void login(String id, String pw) { authModel.login(id, pw); }

    public LiveData<Task> getRegisterSuccess(){
        return registerSuccess;
    }

    public SingleLiveEvent<Task> getLoginSuccess(){ return loginSuccess; }

    public SingleLiveEvent<FirebaseUser> getFirebaseUserLiveData() { return firebaseUserLiveData; }
}
