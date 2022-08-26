package com.example.gowork.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.dto.WorkInfo;
import com.example.gowork.model.DBRepository;
import com.example.gowork.SingleLiveEvent;
import com.example.gowork.dto.UserDTO;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class DBViewModel extends AndroidViewModel {

    private DBRepository dbRepository;

    private SingleLiveEvent<Task> uploadUserInfoSuccessful;
    private LiveData<UserDTO> userInfoData;
    private SingleLiveEvent<Task> updateUserInfoTask;
    private SingleLiveEvent<Task> uploadWorkInfoTask;
    private MutableLiveData<WorkInfo> workInfoMutableLiveData;

    public DBViewModel(@NonNull Application application) {
        super(application);

        dbRepository = new DBRepository(application);
        uploadUserInfoSuccessful = dbRepository.getUploadUserInfoSuccessful();
        userInfoData = dbRepository.getUserInfoLiveData();
        updateUserInfoTask = dbRepository.getUpdateUserInfoTask();
        uploadWorkInfoTask = dbRepository.getUploadWorkInfoTask();
        workInfoMutableLiveData = dbRepository.getWorkInfoMutableLiveData();
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

    public void setUploadWorkInfo(FirebaseUser user, WorkInfo workInfo){
        dbRepository.upLoadWorkInfo(user, workInfo);
    }

    public void getWorkInfoData(FirebaseUser firebaseUser){
        dbRepository.getWorkInfoMutableLiveData();
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

    public SingleLiveEvent<Task> getUploadWorkInfoTask() {
        return uploadWorkInfoTask;
    }

    public MutableLiveData<WorkInfo> getWorkInfoMutableLiveData() {
        return workInfoMutableLiveData;
    }
}
