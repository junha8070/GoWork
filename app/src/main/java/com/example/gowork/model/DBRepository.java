package com.example.gowork.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.dto.UserDTO;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.SingleLiveEvent;

import com.example.gowork.dto.WorkSchedule_day;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;

public class DBRepository {
    private String TAG = "DBRepository";

    private Application application;

    private FirebaseDatabase realTime;
    private FirebaseFirestore fireStore;

    private SingleLiveEvent<Task> uploadUserInfoSuccessful;
    private MutableLiveData<UserDTO> userInfoLiveData;
    private SingleLiveEvent<Task> updateUserInfoTask;
    private SingleLiveEvent<Task> uploadWorkInfoTask;
    private MutableLiveData<WorkInfo> workInfoMutableLiveData;

    public DBRepository(Application application) {
        this.application = application;

        realTime = FirebaseDatabase.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        uploadUserInfoSuccessful = new SingleLiveEvent<>();
        userInfoLiveData = new MutableLiveData<>();
        updateUserInfoTask = new SingleLiveEvent<>();
        uploadWorkInfoTask = new SingleLiveEvent<>();
        workInfoMutableLiveData = new MutableLiveData<>();
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
                    Log.d(TAG, userInfo.getId());
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

    public void updateUserInfo(FirebaseUser user, UserDTO userDTO) {
        DocumentReference userReference = fireStore.collection("User").document(user.getUid());

        fireStore.runTransaction(new Transaction.Function<UserDTO>() {
            @Nullable
            @Override
            public UserDTO apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(userReference, "name", userDTO.getName());
                transaction.update(userReference, "phone", userDTO.getPhone());
                // 새로운 값 반환
                return userDTO;
            }
        }).addOnCompleteListener(new OnCompleteListener<UserDTO>() {
            @Override
            public void onComplete(@NonNull Task<UserDTO> task) {
                if (task.isSuccessful()) {
                    // 위에 새로운 값 반환된것으로 사용
//                    Log.d(TAG, task.getResult().getName());
                    UserDTO updateUserDTO = new UserDTO();
                    updateUserDTO.setId(user.getEmail());
                    updateUserDTO.setName(task.getResult().getName());
                    updateUserDTO.setPhone(task.getResult().getPhone());
                    userInfoLiveData.postValue(updateUserDTO);
                    updateUserInfoTask.postValue(task);
                } else {
                    Log.d(TAG, task.getException().getMessage());
                    updateUserInfoTask.postValue(task);
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d(TAG, "UserInfo update canceled");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    public void upLoadWorkInfo(FirebaseUser user, WorkInfo workInfo, HashMap<String, Object> schedule_data) {
        CollectionReference colReference = fireStore.collection("Work").document(user.getUid()).collection(workInfo.getPlace_name());


        colReference.document("WorkInfo").set(workInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    colReference.document("Schedule").set(schedule_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                uploadWorkInfoTask.postValue(task);
                            }else{
                                Toast.makeText(application, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Toast.makeText(application, "작업이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(application, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(application, "작업이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWorkInfo(FirebaseUser user) {
        DocumentReference docReference = fireStore.collection("Work").document(user.getUid());

        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, String.valueOf(value.getData()));
                WorkInfo workInfo = value.toObject(WorkInfo.class);
                workInfoMutableLiveData.postValue(workInfo);
            }
        });
    }

    public SingleLiveEvent<Task> getUploadUserInfoSuccessful() {
        return uploadUserInfoSuccessful;
    }

    public LiveData<UserDTO> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public SingleLiveEvent<Task> getUpdateUserInfoTask() {
        return updateUserInfoTask;
    }

    public SingleLiveEvent<Task> getUploadWorkInfoTask() {
        return uploadWorkInfoTask;
    }

    public MutableLiveData<WorkInfo> getWorkInfoMutableLiveData() {
        return workInfoMutableLiveData;
    }
}