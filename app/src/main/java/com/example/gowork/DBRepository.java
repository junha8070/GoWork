package com.example.gowork;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBRepository {
    private String TAG = "DBRepository";

    private Application application;

    private FirebaseDatabase realTime;
    private FirebaseFirestore fireStore;

    private SingleLiveEvent<Task> uploadUserInfoSuccessful;
    private MutableLiveData<UserDTO> userInfoLiveData;

    public DBRepository(Application application) {
        this.application = application;

        realTime = FirebaseDatabase.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        uploadUserInfoSuccessful = new SingleLiveEvent<>();
        userInfoLiveData = new MutableLiveData<>();
    }

    public void upLoadUserInfo(FirebaseUser user, UserDTO userDTO) {
        DocumentReference userReference = fireStore.collection("User").document(user.getUid());

        userReference.set(userDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                uploadUserInfoSuccessful.postValue(task);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error " + TAG + " upLoadUserInfo : ", e.getMessage());
            }
        });
    }

    public void getUserInfo(FirebaseUser user) {
        DocumentReference userReference = fireStore.collection("User").document(user.getUid());

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    UserDTO userInfo = task.getResult().toObject(UserDTO.class);
                    Log.d(TAG, userInfo.id);
                    userInfoLiveData.setValue(userInfo);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error " + TAG + " getUserInfo : ", e.getMessage());
            }
        });
    }

    public SingleLiveEvent<Task> getUploadUserInfoSuccessful() {
        return uploadUserInfoSuccessful;
    }

    public LiveData<UserDTO> getUserInfoLiveData(){
        return userInfoLiveData;
    }

}
