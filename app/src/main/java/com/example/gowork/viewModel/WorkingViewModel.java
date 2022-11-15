package com.example.gowork.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.dto.WorkingTimeDTO;
import com.example.gowork.model.DBRepository;
import com.example.gowork.model.WorkingModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkingViewModel extends AndroidViewModel {

    private WorkingModel workingModel;

    private DBRepository dbRepository;

    private MutableLiveData<Boolean> workingState;

    private MutableLiveData<HashMap<String, HashMap>> workingTimeMutableLiveData;

    public WorkingViewModel(@NonNull Application application) {
        super(application);

        workingModel = new WorkingModel(application);

        dbRepository = new DBRepository(application);

        workingState = new MutableLiveData<>();

        workingTimeMutableLiveData = dbRepository.getWorkingTimeMutableLiveData();
    }

    public void setWorkingState(Boolean workingState){
        workingModel.setWorkingState(workingState);
    }

    public Boolean getWorkingState(){
        return workingModel.getWorkingState();
    }

    public void setWorkingPlace(String Working_Place){
        workingModel.setWorkingPlace(Working_Place);
    }

    public String getWorkingPlace(){
        return workingModel.getWorkingPlace();
    }

    public void setStartWorkingTime(String Start_Time){
        workingModel.setStartWorkingTime(Start_Time);
    }

    public String getStartWorkingTime(){
        return workingModel.getStartWorkingTime();
    }

    public void setFinishWorkingTime(String Finish_Time){
        workingModel.setFinishWorkingTime(Finish_Time);
    }

    public String getFinishWorkingTime(){
        return workingModel.getFinishWorkingTIme();
    }

    public void setWorkingTime(FirebaseUser firebaseUser, WorkingTimeDTO workingTimeDTO){
        dbRepository.UploadWorkingTime(firebaseUser, workingTimeDTO);
    }

    public void getWorkingTime(FirebaseUser firebaseUser, String yearMonth){
        dbRepository.getWorkingTime(firebaseUser, yearMonth);
    }

    public MutableLiveData<HashMap<String, HashMap>> getWorkingTimeMutableLiveData(){
        return workingTimeMutableLiveData;
    }
}
