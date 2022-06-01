package com.example.gowork.model;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.MyService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeModel {

    private Application application;
    private SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar cal;
    private Date date;
    long mNow;
    private String today = null;
    private MutableLiveData<String> currentTime;


    public HomeModel(Application application) {
        this.application = application;
        cal = Calendar.getInstance();
        date = new Date();
        currentTime = new MutableLiveData<>();
    }

//    public void getWorkTime(){
//        cal.setTime(date);
//        cal.add();
//    }

    public void currentTime(){
        mNow = System.currentTimeMillis();
        date = new Date(mNow);
        currentTime.postValue(mformat.format(date));
    }

    public LiveData<String> getCurrentTime() {
        return currentTime;
    }
}
