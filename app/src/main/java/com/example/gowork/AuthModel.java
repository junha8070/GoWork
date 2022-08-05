package com.example.gowork;

import android.app.Application;
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

public class AuthModel {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private Application application;
    private MutableLiveData<Task> registerSuccess;

    public AuthModel(Application application){
        this.application = application;

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        registerSuccess = new MutableLiveData<>();
    }

    public void register(String id, String pw){
        auth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                registerSuccess.postValue(task);

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

    public LiveData<Task> getRegisterSuccessful(){
        return registerSuccess;
    }

}