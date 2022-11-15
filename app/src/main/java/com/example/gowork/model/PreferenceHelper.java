package com.example.gowork.model;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private final String USERID = "userid";
    private final String PASSWORD = "password";
    private final String AUTOLOGIN = "AUTOLOGIN";
    private final String WorkingState = "WorkingState";
    private final String WorkingPlace = "PLACE_NAME";
    private final String START_WORKING = "START_WORKING";
    private final String FINISH_WORKING = "FINISH_WORKING";
    private SharedPreferences user_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        user_prefs = context.getSharedPreferences("shared", 0);
        this.context = context;
    }

    public void saveAutoLogin(Boolean isAutoLogin) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putBoolean(AUTOLOGIN, isAutoLogin);
        edit.apply();
    }

    public Boolean getAutoLogin() {
        return user_prefs.getBoolean(AUTOLOGIN, false);
    }

    public void saveUserid(String userId) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(USERID, userId);
        edit.apply();
    }

    public String getUserid() {
        return user_prefs.getString(USERID, "");
    }

    public void savePassword(String passWord) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(PASSWORD, passWord);
        edit.apply();
    }

    public String getPassword() {
        return user_prefs.getString(PASSWORD, "");
    }


    public void saveWorkingState(Boolean workingState) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putBoolean(WorkingState, workingState);
        edit.apply();
    }

    public Boolean getWorkingState() {
        return user_prefs.getBoolean(WorkingState, false);
    }

    public void saveWorkingPlace(String Working_place){
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(WorkingPlace, Working_place);
        edit.apply();
    }

    public String getWorkingPlace(){
        return user_prefs.getString(WorkingPlace, "");
    }

    public void saveStartWorkingTime(String Start_Time) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(START_WORKING, Start_Time);
        edit.apply();
    }

    public String getStartWorkingTime(){
        return user_prefs.getString(START_WORKING, "");
    }

    public void saveFinishWorkingTime(String Finish_Time) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(FINISH_WORKING, Finish_Time);
        edit.apply();
    }

    public String getFinishWorkingTime(){
        return user_prefs.getString(FINISH_WORKING, "");
    }
}
