package com.example.gowork;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private final String USERID = "userid";
    private final String PASSWORD = "password";
    private final String AUTOLOGIN = "AUTOLOGIN";
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

    public Boolean getAutoLogin() {return user_prefs.getBoolean(AUTOLOGIN, false);}

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

}
