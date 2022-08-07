package com.example.gowork;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AuthModel {
    private String TAG = "AuthModel";

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private Application application;
    private MutableLiveData<Task> registerSuccess;
    private SingleLiveEvent<FirebaseUser> firebaseUserMutableLiveData;
    private HashMap<String, Object> loginInfo;
//    private MutableLiveData<Task> loginSuccess;
    private SingleLiveEvent<Task> loginSuccess;

    private PreferenceHelper preferenceHelper;

    public AuthModel(Application application){
        this.application = application;

        preferenceHelper = new PreferenceHelper(application);
        loginInfo = new HashMap<>();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        registerSuccess = new MutableLiveData<>();
        loginSuccess = new SingleLiveEvent<>();
        firebaseUserMutableLiveData = new SingleLiveEvent<>();
    }

    public void register(String id, String pw){
        auth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                registerSuccess.postValue(task);
                firebaseUserMutableLiveData.postValue(auth.getCurrentUser());

                // task 완료
//                if(task.isComplete()){
//                    // task 성공
//                    if(task.isSuccessful()){
//                        registerSuccess.postValue(task);
//                    }
//                    // task 실패
//                    else{
//                        registerSuccess.postValue(task);
////                        Toast.makeText(application.getApplicationContext(), "오류가 발생하였습니다.\n관리자한테 문의해주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                // task 취소
//                else {
//                    registerSuccess.postValue(task);
////                    Toast.makeText(application.getApplicationContext(), "오류가 발생하였습니다.\n관리자한테 문의해주세요.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public void login(String id, String pw){
        auth.signInWithEmailAndPassword(id, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()){
                    if(task.isSuccessful()){
                        loginSuccess.setValue(task);
                        firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                    }else{
                        loginSuccess.setValue(task);
                    }
                }else{
                    loginSuccess.setValue(task);
                }
            }
        });
    }

    //데이터를 내부 저장소에 저장하기
    public void setPreference(HashMap<String, Object> loginInfo) {
//        Log.d(TAG, loginInfo.get("id").toString());
        preferenceHelper.saveAutoLogin((Boolean) loginInfo.get("autologin"));
        preferenceHelper.saveUserid(String.valueOf(loginInfo.get("id")));
        preferenceHelper.savePassword(String.valueOf(loginInfo.get("password")));
    }

    public void logout(){
        auth.getInstance().signOut();
    }

    public LiveData<Task> getRegisterSuccessful(){
        return registerSuccess;
    }

    public HashMap<String, Object> getPreferenceString() {
        loginInfo.put("autologin", preferenceHelper.getAutoLogin());
        loginInfo.put("id", preferenceHelper.getUserid());
        loginInfo.put("password", preferenceHelper.getPassword());
        return loginInfo;
    }

    public SingleLiveEvent<Task> getLoginSuccessful(){ return loginSuccess; }

    public SingleLiveEvent<FirebaseUser> getFirebaseUser() {return firebaseUserMutableLiveData;}
}
