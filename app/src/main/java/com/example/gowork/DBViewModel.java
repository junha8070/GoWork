package com.example.gowork;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class DBViewModel extends AndroidViewModel {

    private DBRepository dbRepository;

    private SingleLiveEvent<Task> uploadUserInfoSuccessful;
    private LiveData<UserDTO> userInfoData;
    private SingleLiveEvent<Task> updateUserInfoTask;

    public DBViewModel(@NonNull Application application) {
        super(application);

        dbRepository = new DBRepository(application);
        uploadUserInfoSuccessful = dbRepository.getUploadUserInfoSuccessful();
        userInfoData = dbRepository.getUserInfoLiveData();
        updateUserInfoTask = dbRepository.getUpdateUserInfoTask();
    }

    public void uploadUserInfo(FirebaseUser user, UserDTO userDTO) {
        dbRepository.upLoadUserInfo(user, userDTO);
    }

    public void userInfoLiveData(FirebaseUser firebaseUser){
        dbRepository.getUserInfo(firebaseUser);
    }

    public void updateUserInfo(FirebaseUser firebaseUser, UserDTO userDto){
        dbRepository.updateUserInfo(firebaseUser, userDto);
    }

    public SingleLiveEvent<Task> getUploadUserInfoSuccessful() {
        return uploadUserInfoSuccessful;
    }

    public LiveData<UserDTO> getUserInfoLiveData(){
        return userInfoData;
    }

    public SingleLiveEvent<Task> getUpdateUserInfoTask(){
        return updateUserInfoTask;
    }
}
