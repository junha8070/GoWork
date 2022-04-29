package com.example.gowork.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkRepository {
    private WorkDao workDao;
    private LiveData<List<Work>> allWorks;
    private LiveData<List<String>> getAllTitle;

    public WorkRepository(Application application) {
        WorkDatabase database = WorkDatabase.getInstance(application);
        workDao = database.workDao();
        allWorks = workDao.getAllWorks();
        getAllTitle = workDao.getAllTitle();
    }

    public void insert(Work work) {
        new InsertWorkAsyncTask(workDao).execute(work);
    }

    public void update(Work work) {
        new UpdateWorkAsyncTask(workDao).execute(work);
    }

    public void delete(Work work) {
        new DeleteWorkAsyncTask(workDao).execute(work);
    }

    public void deleteAllWorks() {
        new DeleteAllWorksAsyncTask(workDao).execute();
    }

    public LiveData<List<Work>> getAllWorks() {
        return allWorks;
    }

    public LiveData<List<String>> getAllTitle(){
        return getAllTitle;
    }

    private static class InsertWorkAsyncTask extends AsyncTask<Work, Void, Void> {
        private WorkDao workDao;

        private InsertWorkAsyncTask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.insert(works[0]);
            return null;
        }
    }

    private static class UpdateWorkAsyncTask extends AsyncTask<Work, Void, Void> {
        private WorkDao workDao;

        private UpdateWorkAsyncTask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.update(works[0]);
            return null;
        }
    }

    private static class DeleteWorkAsyncTask extends AsyncTask<Work, Void, Void> {
        private WorkDao workDao;

        private DeleteWorkAsyncTask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.delete(works[0]);
            return null;
        }
    }

    private static class DeleteAllWorksAsyncTask extends AsyncTask<Work, Void, Void> {
        private WorkDao workDao;

        private DeleteAllWorksAsyncTask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.deleteAllWorks();
            return null;
        }
    }
}