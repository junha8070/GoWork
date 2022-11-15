package com.example.gowork.model;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

public class WorkingModel {

    private Application application;
    private PreferenceHelper preferenceHelper;

    private MutableLiveData<Boolean> workingState;

    public WorkingModel(Application application){
        this.application = application;

        preferenceHelper = new PreferenceHelper(application);

        workingState = new MutableLiveData<>();
    }

    public void setWorkingState(Boolean workingState) {
        preferenceHelper.saveWorkingState(workingState);
    }

    public Boolean getWorkingState() {
        return preferenceHelper.getWorkingState();
    }

    public void setWorkingPlace(String Working_place){
        preferenceHelper.saveWorkingPlace(Working_place);
    }

    public String getWorkingPlace(){
        return preferenceHelper.getWorkingPlace();
    }

    public void setStartWorkingTime(String Start_Time){
        preferenceHelper.saveStartWorkingTime(Start_Time);
    }

    public String getStartWorkingTime(){
        return preferenceHelper.getStartWorkingTime();
    }

    public void setFinishWorkingTime(String Finish_time){
        preferenceHelper.saveFinishWorkingTime(Finish_time);
    }

    public String getFinishWorkingTIme(){
        return preferenceHelper.getFinishWorkingTime();
    }
}
