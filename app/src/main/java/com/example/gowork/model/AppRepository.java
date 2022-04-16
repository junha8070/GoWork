package com.example.gowork.model;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class AppRepository {
    private String TAG = "AppRepository";

    private final Application application;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> logoutMutableLiveData;
    private final MutableLiveData<FirebaseFirestore> userInfoMutableLiveData;
    private final MutableLiveData<Boolean> isEmailExistMutableLiveData;


    public AppRepository(Application application) {
        this.application = application;

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        logoutMutableLiveData = new MutableLiveData<>();
        userInfoMutableLiveData = new MutableLiveData<>();
        isEmailExistMutableLiveData = new MutableLiveData<>();

//        userMutableLiveData.postValue(null);
//        logoutMutableLiveData.postValue(null);
//        userInfoMutableLiveData.postValue(null);
        isEmailExistMutableLiveData.postValue(null);

        if (firebaseAuth.getCurrentUser() != null) {
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            logoutMutableLiveData.postValue(false);
        }

    }


    public void register(String email, String password) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(application.getMainExecutor(), task -> {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        } else {
                            Toast.makeText(application, "Registration Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        } else {
                            Toast.makeText(application, "Registration Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public void login(String email, String password) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(application.getMainExecutor(), task -> {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        } else {
                            Toast.makeText(application, "Login Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());

                        } else {
                            Toast.makeText(application, "Login Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void logOut() {
        firebaseAuth.signOut();
        logoutMutableLiveData.postValue(true);
    }

    public void uploadUserInfo(String email, String name, String phoneNum) {
        UserInfo userInfo = new UserInfo(email, name, phoneNum);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firestore.collection("User")
                    .add(userInfo)
                    .addOnCompleteListener(application.getMainExecutor(), task -> {
                        if (task.isSuccessful()) {
                            userInfoMutableLiveData.postValue(firestore);
                            Toast.makeText(application, "Complete", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(application, "UserInfo Upload Failed"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            firestore.collection("User")
                    .add(userInfo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userInfoMutableLiveData.postValue(firestore);
                            Toast.makeText(application, "Complete", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(application, "UserInfo Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void isEmailExist(String email){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firestore.collection("User")
                    .whereEqualTo("email",email)
                    .get()
                    .addOnCompleteListener(application.getMainExecutor(), task -> {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Toast.makeText(application,"toast",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, documentSnapshot.getId());
                        isEmailExistMutableLiveData.postValue(true);
                    }
                    if(task.getResult().isEmpty()){
                        isEmailExistMutableLiveData.postValue(false);
                        Toast.makeText(application,"null",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Log.d(TAG, task.getException().getMessage());
                }
            });
        }
        else{
            firestore.collection("User")
                    .whereEqualTo("email",email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Toast.makeText(application,"toast",Toast.LENGTH_SHORT).show();
                                Log.d(TAG, documentSnapshot.getId());
                                isEmailExistMutableLiveData.postValue(true);
                            }
                            if(task.getResult().isEmpty()){
                                isEmailExistMutableLiveData.postValue(false);
                                Toast.makeText(application,"null",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Log.d(TAG, task.getException().getMessage());
                        }
                    });
        }
    }

    public LiveData<FirebaseUser> getUserData() {
        return userMutableLiveData;
    }


    public LiveData<Boolean> getLogoutData() {
        return logoutMutableLiveData;
    }

    public LiveData<Boolean> getIsEmailExistData() {
        return isEmailExistMutableLiveData;
    }

    public LiveData<FirebaseFirestore> getUserInfoData() {
        return userInfoMutableLiveData;
    }

    public void isLoading(){

    }
}
