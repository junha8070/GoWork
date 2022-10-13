package com.example.gowork.model;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.dto.PostDTO;
import com.example.gowork.dto.PostDTO_Upload;
import com.example.gowork.dto.MyPostDTO_Upload_NoUrl;
import com.example.gowork.dto.PostDTO_Upload_NoUrl;
import com.example.gowork.dto.UserDTO;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.SingleLiveEvent;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class DBRepository {
    private String TAG = "DBRepository";

    private Application application;

    private FirebaseDatabase realTime;
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private UploadTask uploadTask;
    int photoCount = 0;

    private SingleLiveEvent<Task> uploadUserInfoSuccessful;
    private MutableLiveData<UserDTO> userInfoLiveData;
    private SingleLiveEvent<Task> updateUserInfoTask;
    private SingleLiveEvent<Task> uploadWorkInfoTask;
    private SingleLiveEvent<Task> getPostTask;
    private MutableLiveData<WorkInfo> workInfoMutableLiveData;
    private MutableLiveData<ArrayList<PostDTO>> postMutableLiveData;

    private ArrayList<String> url;
    private ArrayList<PostDTO> arr_post;

    public DBRepository(Application application) {
        this.application = application;

        realTime = FirebaseDatabase.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        uploadUserInfoSuccessful = new SingleLiveEvent<>();
        userInfoLiveData = new MutableLiveData<>();
        updateUserInfoTask = new SingleLiveEvent<>();
        uploadWorkInfoTask = new SingleLiveEvent<>();
        getPostTask = new SingleLiveEvent<>();
        workInfoMutableLiveData = new MutableLiveData<>();
        postMutableLiveData = new MutableLiveData<>();
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
                            } else {
                                Toast.makeText(application, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Toast.makeText(application, "작업이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
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

    public void uploadPost(FirebaseUser user, PostDTO_Upload postDTO_upload) {

        url = new ArrayList();

        CollectionReference PostColReference = fireStore.collection("Post");
        DocumentReference myPostDocReference = fireStore.collection("myPost").document(user.getUid());


        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("post_images/" + user.getUid());
        uploadTask = riversRef.putFile(postDTO_upload.getPhoto());

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                Log.d(TAG, "사진 업로드 완료 후 링크123 " + riversRef.getDownloadUrl());
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    postDTO_upload.setPhoto(downloadUri);
                    PostColReference.add(postDTO_upload).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                PostDTO postDTO = new PostDTO(task.getResult().getId(), postDTO_upload.getName(), postDTO_upload.getTitle(), postDTO_upload.getContents(), downloadUri, postDTO_upload.getTime());
                                myPostDocReference.set(postDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(application, "게시물을 올렸습니다.", Toast.LENGTH_SHORT).show();
                                            getPost();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(application, "게시물 올리는데 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void getPost(){
        arr_post = new ArrayList<>();
        CollectionReference postColReference = fireStore.collection("Post");

        postColReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        PostDTO data = new PostDTO(document.getId(), String.valueOf(document.get("name")), String.valueOf(document.get("title")), String.valueOf(document.get("contents")), Uri.parse(String.valueOf(document.get("photo"))), String.valueOf(document.get("time")));
                        arr_post.add(data);
                    }
                    getPostTask.postValue(task);
                    postMutableLiveData.postValue(arr_post);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG+" getPost() 함수 요류", e.getMessage());
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

    public SingleLiveEvent<Task> getGetPostTask() {
        return getPostTask;
    }

    public MutableLiveData<WorkInfo> getWorkInfoMutableLiveData() {
        return workInfoMutableLiveData;
    }

    public MutableLiveData<ArrayList<PostDTO>> getPostMutableLiveData() {
        return postMutableLiveData;
    }
}