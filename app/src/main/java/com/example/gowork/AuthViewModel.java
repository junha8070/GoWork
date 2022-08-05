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

    public AuthViewModel(@NonNull Application application) {
        super(application);

        authModel = new AuthModel(application);
        registerSuccess = authModel.getRegisterSuccessful();;
    }

    public void register(String id, String pw){
        authModel.register(id, pw);
    }

    public LiveData<Task> getRegisterSuccess(){
        return registerSuccess;
    }
}
