package com.example.gowork.model;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.dto.CommentDTO;
import com.example.gowork.dto.PostDTO;
import com.example.gowork.dto.PostDTO_Upload;
import com.example.gowork.dto.UserDTO;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.SingleLiveEvent;

import com.example.gowork.dto.WorkingTimeDTO;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DBRepository {
    private String TAG = "DBRepository";

    private Application application;

    private FirebaseDatabase realTime;
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private UploadTask uploadTask;
    int photoCount = 0;

    // Task
    private SingleLiveEvent<Task> updateUserInfoTask;
    private SingleLiveEvent<Task> uploadWorkInfoTask;
    private SingleLiveEvent<Task> getPostTask;
    private SingleLiveEvent<Task> uploadUserInfoSuccessful;
    private SingleLiveEvent<Task> uploadCommentTask;

    // Value
    private MutableLiveData<UserDTO> userInfoLiveData;
    private MutableLiveData<ArrayList<PostDTO>> postMutableLiveData;
    private MutableLiveData<ArrayList<CommentDTO>> commentMutableLiveData;
    private MutableLiveData<ArrayList<WorkInfo>> workInfoMutableLiveData;
    private MutableLiveData<ArrayList<HashMap<String, Object>>> scheduleMutableLiveData;
    private MutableLiveData<HashMap<String, HashMap>> workingTimeMutableLiveData;

    private ArrayList<String> url;
    private ArrayList<PostDTO> arr_post;
    private ArrayList<CommentDTO> arr_comment;

    public DBRepository(Application application) {
        this.application = application;

        realTime = FirebaseDatabase.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Task init
        uploadUserInfoSuccessful = new SingleLiveEvent<>();
        updateUserInfoTask = new SingleLiveEvent<>();
        uploadWorkInfoTask = new SingleLiveEvent<>();
        getPostTask = new SingleLiveEvent<>();
        uploadCommentTask = new SingleLiveEvent<>();

        // Value init
        userInfoLiveData = new MutableLiveData<>();
        postMutableLiveData = new MutableLiveData<>();
        commentMutableLiveData = new SingleLiveEvent<>();
        workInfoMutableLiveData = new MutableLiveData<>();
        scheduleMutableLiveData = new MutableLiveData<>();
        workingTimeMutableLiveData = new MutableLiveData<>();
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
        DocumentReference docWorkTitleReference = fireStore.collection("Work").document(user.getUid());
        CollectionReference colReference = fireStore.collection("Work").document(user.getUid()).collection(workInfo.getPlace_name());

        colReference.document("WorkInfo").set(workInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    colReference.document("Schedule").set(schedule_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                docWorkTitleReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            // WorkPlace 배열 값이 존재하지 않을경우
                                            if (!task.getResult().exists()) {
                                                ArrayList WorkPlace = new ArrayList();
                                                WorkPlace.add(workInfo.getPlace_name());
                                                HashMap<String, ArrayList> temp_value = new HashMap<>();
                                                temp_value.put("WorkPlace", WorkPlace);
                                                docWorkTitleReference.set(temp_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            uploadWorkInfoTask.postValue(task);
                                                        } else {
                                                            Log.d(TAG, "WorkPlace 배열 생성 오류 : " + task.getException().getMessage());
                                                            Toast.makeText(application, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "WorkPlace 배열 생성 오류 : " + e.getMessage());
                                                        Toast.makeText(application, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            // WorkPlace 배열 값이 존재하지 않을경우
                                            else {
                                                docWorkTitleReference.update("WorkPlace", FieldValue.arrayUnion(workInfo.getPlace_name())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "WorkPlace 배열 추가 : " + task.getResult());
                                                            uploadWorkInfoTask.postValue(task);
                                                        } else {
                                                            Log.d(TAG, "WorkPlace 배열 추가 오류 : " + task.getException().getMessage());
                                                            Toast.makeText(application, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "WorkPlace 배열 추가 오류 : " + e.getMessage());
                                                        Toast.makeText(application, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnCanceledListener(new OnCanceledListener() {
                                                    @Override
                                                    public void onCanceled() {
                                                        Log.d(TAG, "WorkPlace 배열 추가 오류 : 취소됨");
                                                        Toast.makeText(application, "작업이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
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
        ArrayList<HashMap<String, Object>> Schedule = new ArrayList<>();
        ArrayList<WorkInfo> arr_WorkInfo = new ArrayList<>();
        DocumentReference docReference = fireStore.collection("Work").document(user.getUid());
        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.exists()) {
                    return;
                }
                ArrayList arrayList = (ArrayList) value.get("WorkPlace");
                for (int i = 0; i < arrayList.size(); i++) {
                    docReference.collection(String.valueOf(arrayList.get(i))).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    if (snapshot.getId().equals("Schedule")) {
                                        Object[] key = snapshot.getData().keySet().toArray();
                                        Object[] value = snapshot.getData().values().toArray();
                                        HashMap<String, Object> temp = new HashMap<>();
                                        for (int i = 0; i < key.length; i++) {
                                            temp.put(String.valueOf(key[i]), value[i]);
                                        }
                                        Schedule.add(temp);
                                        scheduleMutableLiveData.postValue(Schedule);
                                        Log.d(TAG, String.valueOf(Schedule));
                                        Log.d(TAG, String.valueOf(Schedule.size()));
                                    }
                                    if (snapshot.getId().equals("WorkInfo")) {
                                        Object[] key = snapshot.getData().keySet().toArray();
                                        Object[] value = snapshot.getData().values().toArray();
                                        HashMap<String, String> temp = new HashMap<>();
                                        for (int i = 0; i < key.length; i++) {
                                            temp.put(String.valueOf(key[i]), String.valueOf(value[i]));
                                        }
                                        WorkInfo workInfo = new WorkInfo(
                                                temp.get("place_name"),
                                                temp.get("place_address"),
                                                temp.get("kind"),
                                                temp.get("pay"),
                                                temp.get("join"),
                                                temp.get("resign"),
                                                temp.get("probation"),
                                                temp.get("start_time"),
                                                temp.get("end_time"),
                                                Boolean.valueOf(temp.get("holiday_allowance")),
                                                Boolean.valueOf(temp.get("sameTime_5upper")),
                                                Boolean.valueOf(temp.get("work_holiday")));

                                        arr_WorkInfo.add(workInfo);
                                        workInfoMutableLiveData.postValue(arr_WorkInfo);

                                        Log.d(TAG, String.valueOf(arr_WorkInfo.get(arr_WorkInfo.size() - 1).getPlace_name()));
                                    }
                                }
                            }
                        }
                    });
                }
//                Log.d(TAG, "데이터 확인 : "+arr_WorkInfo.get(0).getJoin());


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

    public void getPost() {
        arr_post = new ArrayList<>();
        CollectionReference postColReference = fireStore.collection("Post");

        postColReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
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
                Log.d(TAG + " getPost() 함수 요류", e.getMessage());
            }
        });
    }

    public void delPost(String postId){
        DocumentReference postDocReference = fireStore.collection("Post").document(postId);
        CollectionReference commentColReference = postDocReference.collection("Comment");

        commentColReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        for(int i = 0; i<task.getResult().getDocuments().size();i++){
                            commentColReference.document(task.getResult().getDocuments().get(i).getId()).delete();
                        }
                        postDocReference.delete();
                    }
                }
            }
        });

    }

    public void upload_comment(FirebaseUser user, CommentDTO commentDTO) {
        DocumentReference postDocReference = fireStore.collection("Post").document(commentDTO.getPostId());

        postDocReference.collection("Comment").add(commentDTO).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    uploadCommentTask.postValue(task);
                } else {
                    Toast.makeText(application, "댓글이 정상적으로 남겨지지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application, "댓글 서버와 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void get_comment(PostDTO postDTO) {
        arr_comment = new ArrayList<>();

        CollectionReference commentColReference = fireStore
                .collection("Post")
                .document(postDTO.getPostId())
                .collection("Comment");

        commentColReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "댓글 작업");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        CommentDTO data = new CommentDTO(String.valueOf(document.get("postId")),
                                String.valueOf(document.get("userUID")),
                                String.valueOf(document.get("profile")),
                                String.valueOf(document.get("name")),
                                String.valueOf(document.get("contents")),
                                String.valueOf(document.get("time")));
                        arr_comment.add(data);
                    }
                    commentMutableLiveData.postValue(arr_comment);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application, "서버로부터 댓글 가져오는데 실패했어요...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // String Work_Place, HashMap<String, String> Working_Time
    public void UploadWorkingTime(FirebaseUser firebaseUser, WorkingTimeDTO workingTimeDTO) {


        ArrayList<String> Start_Time = new ArrayList<>();
        ArrayList<String> Finish_Time = new ArrayList<>();
        Start_Time.add(workingTimeDTO.getTime().get("Start_Time"));
        Finish_Time.add(workingTimeDTO.getTime().get("Finish_Time"));

        HashMap<String, ArrayList<String>> Start_Time_HashMap = new LinkedHashMap<>();
        Start_Time_HashMap.put("Start_Time", Start_Time);
        Start_Time_HashMap.put("Finish_Time", Finish_Time);

        String yearMonth = Start_Time.get(0).substring(0, 7);
        Log.d(TAG, "yearMonth" + yearMonth);

        DocumentReference workingTimeDocReference = fireStore.collection("WorkingTime").document(firebaseUser.getUid()).collection(workingTimeDTO.getWorking_Place()).document(yearMonth);

        workingTimeDocReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {
                        workingTimeDocReference.set(Start_Time_HashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(application, "수고하셨어요 :)", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        workingTimeDocReference.update("Start_Time", FieldValue.arrayUnion(workingTimeDTO.getTime().get("Start_Time"))).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    workingTimeDocReference.update("Finish_Time", FieldValue.arrayUnion(workingTimeDTO.getTime().get("Finish_Time"))).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(application, "오늘도 수고하셨어요 :)", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void getWorkingTime(FirebaseUser firebaseUser, String yearMonth) {
        DocumentReference workingTimeDocReference = fireStore.collection("WorkingTime").document(firebaseUser.getUid());
        DocumentReference workTitleDocReference = fireStore.collection("Work").document(firebaseUser.getUid());
        HashMap<String, ArrayList> startTime_hashmap = new LinkedHashMap<>();
        HashMap<String, ArrayList> finishTime_hashmap = new LinkedHashMap<>();
        HashMap<String, HashMap> timeHashMap = new LinkedHashMap<>();

        ArrayList<WorkingTimeDTO> workingTimeDTOS;

        workTitleDocReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {
                        return;
                    }
                    ArrayList<String> workTitle = new ArrayList<>();
                    workTitle = (ArrayList) task.getResult().get("WorkPlace");
//                    workTitle.addAll(Collections.singleton(task.getResult().getData().values().toString()));
                    for (String title : workTitle) {
                        workingTimeDocReference.collection(title).document(yearMonth).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        Log.d(TAG + " WorkingTimeTAG ", task.getResult().toString());
                                        ArrayList startTime = (ArrayList) task.getResult().get("Start_Time");
                                        ArrayList finishTime = (ArrayList) task.getResult().get("Finish_Time");
                                        startTime_hashmap.put(title, startTime);
                                        finishTime_hashmap.put(title, finishTime);
                                        timeHashMap.put("startTime", startTime_hashmap);
                                        timeHashMap.put("finishTime", finishTime_hashmap);
                                        workingTimeMutableLiveData.postValue(timeHashMap);
                                    } else {
                                        Log.d(TAG + " WorkingTimeTAG ", String.valueOf(task.getResult()));
                                    }
                                } else {
                                    Log.d(TAG + " WorkingTimeTAG ", task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                Log.d(TAG, e.getStackTrace().toString());
            }
        });
    }

    public SingleLiveEvent<Task> getUploadUserInfoSuccessful() {
        return uploadUserInfoSuccessful;
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

    public SingleLiveEvent<Task> getUploadCommentTask() {
        return uploadCommentTask;
    }

    public LiveData<UserDTO> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public MutableLiveData<ArrayList<PostDTO>> getPostMutableLiveData() {
        return postMutableLiveData;
    }

    public MutableLiveData<ArrayList<CommentDTO>> getCommentMutableLiveData() {
        return commentMutableLiveData;
    }

    public MutableLiveData<ArrayList<WorkInfo>> getWorkInfoMutableLiveData() {
        return workInfoMutableLiveData;
    }

    public MutableLiveData<ArrayList<HashMap<String, Object>>> getScheduleMutableLiveData() {
        return scheduleMutableLiveData;
    }

    public MutableLiveData<HashMap<String, HashMap>> getWorkingTimeMutableLiveData() {
        return workingTimeMutableLiveData;
    }
}