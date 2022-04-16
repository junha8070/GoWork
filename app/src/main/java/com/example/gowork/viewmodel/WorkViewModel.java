package com.example.gowork.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.gowork.db.Work;
import com.example.gowork.db.WorkRepository;
import com.example.gowork.model.AppRepository;
import com.example.gowork.db.WorkDatabase;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class WorkViewModel extends AndroidViewModel {
    private WorkRepository workRepository;
    private LiveData<List<Work>> allWorks;

    public WorkViewModel(@NonNull Application application){
        super(application);
        workRepository = new WorkRepository(application);
        allWorks = workRepository.getAllWorks();
    }

    public void insert(Work work) {
        workRepository.insert(work);
    }

    public  void update(Work work) {
        workRepository.update(work);
    }

    public void delete(Work work) {
        workRepository.delete(work);
    }

    public void deleteAllWorks() {
        workRepository.deleteAllWorks();
    }

    public LiveData<List<Work>> getAllWorks() {
        return allWorks;
    }

}
