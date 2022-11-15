package com.example.gowork.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.dto.CommentDTO;
import com.example.gowork.dto.PostDTO;
import com.example.gowork.dto.PostDTO_Upload;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.model.DBRepository;
import com.example.gowork.SingleLiveEvent;
import com.example.gowork.dto.UserDTO;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

public class DBViewModel extends AndroidViewModel {

    private DBRepository dbRepository;

    // Task
    private SingleLiveEvent<Task> uploadUserInfoSuccessful;
    private SingleLiveEvent<Task> updateUserInfoTask;
    private SingleLiveEvent<Task> uploadWorkInfoTask;
    private SingleLiveEvent<Task> getPostTask;
    private SingleLiveEvent<Task> uploadCommentTask;

    // Value
    private LiveData<UserDTO> userInfoData;
    private MutableLiveData<ArrayList<PostDTO>> postMutableLiveData;
    private MutableLiveData<ArrayList<CommentDTO>> commentMutableLiveData;
    private MutableLiveData<ArrayList<WorkInfo>> workInfoMutableLiveData;
    private MutableLiveData<ArrayList<HashMap<String, Object>>> scheduleMutableLiveData;

    public DBViewModel(@NonNull Application application) {
        super(application);

        dbRepository = new DBRepository(application);

        // Task init
        uploadUserInfoSuccessful = dbRepository.getUploadUserInfoSuccessful();
        updateUserInfoTask = dbRepository.getUpdateUserInfoTask();
        uploadWorkInfoTask = dbRepository.getUploadWorkInfoTask();
        getPostTask = dbRepository.getGetPostTask();

        // Value init
        userInfoData = dbRepository.getUserInfoLiveData();
        postMutableLiveData = dbRepository.getPostMutableLiveData();
        commentMutableLiveData = dbRepository.getCommentMutableLiveData();
        workInfoMutableLiveData = dbRepository.getWorkInfoMutableLiveData();
        scheduleMutableLiveData = dbRepository.getScheduleMutableLiveData();
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

    public void setUploadWorkInfo(FirebaseUser user, WorkInfo workInfo, HashMap<String, Object> schedule_data){
        dbRepository.upLoadWorkInfo(user, workInfo, schedule_data);
    }

    public void getWorkInfoData(FirebaseUser firebaseUser){
        dbRepository.getWorkInfo(firebaseUser);
    }

    public void getScheduleData(){
        dbRepository.getScheduleMutableLiveData();
    }

    public void getPost(){
        dbRepository.getPost();
    }

    public void uploadPost(FirebaseUser user, PostDTO_Upload postDTO_upload){
        dbRepository.uploadPost(user, postDTO_upload);
    }

    public void delPost(String postId){
        dbRepository.delPost(postId);
    }

    public void uploadComment(FirebaseUser user, CommentDTO commentData){
        dbRepository.upload_comment(user, commentData);
    }

    public void getComment(PostDTO postDTO){
        dbRepository.get_comment(postDTO);
    }

    public void isUploadCommentSuccessful(){

    }

    public SingleLiveEvent<Task> getUploadUserInfoSuccessful() {
        return uploadUserInfoSuccessful;
    }

    public SingleLiveEvent<Task> getUpdateUserInfoTask(){
        return updateUserInfoTask;
    }

    public SingleLiveEvent<Task> getUploadWorkInfoTask() {
        return uploadWorkInfoTask;
    }

    public SingleLiveEvent<Task> getGetPostTask() {
        return getPostTask;
    }

    public MutableLiveData<ArrayList<WorkInfo>> getWorkInfoMutableLiveData() {
        return workInfoMutableLiveData;
    }

    public MutableLiveData<ArrayList<HashMap<String, Object>>> getScheduleMutableLiveData(){
        return scheduleMutableLiveData;
    }

    public LiveData<UserDTO> getUserInfoLiveData(){
        return userInfoData;
    }

    public MutableLiveData<ArrayList<PostDTO>> postMutableLiveData() {
        return postMutableLiveData;
    }

    public MutableLiveData<ArrayList<CommentDTO>> getCommentLiveData(){
        return commentMutableLiveData;
    }
}
