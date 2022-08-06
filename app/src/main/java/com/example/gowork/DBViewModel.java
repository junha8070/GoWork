package com.example.gowork;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class DBViewModel extends AndroidViewModel {

    private DBRepository dbRepository;

    private SingleLiveEvent<Task> uploadUserInfoSuccessful;

    public DBViewModel(@NonNull Application application) {
        super(application);

        dbRepository = new DBRepository(application);
        uploadUserInfoSuccessful = dbRepository.getUploadUserInfoSuccessful();
    }

    public void uploadUserInfo(FirebaseUser user, UserDTO userDTO) {
        dbRepository.upLoadUserInfo(user, userDTO);
    }

    public SingleLiveEvent<Task> getUploadUserInfoSuccessful() {
        return uploadUserInfoSuccessful;
    }
}
