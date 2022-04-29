package com.example.gowork.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.List;

@Entity(tableName = "work_table")
public class Work {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "workTitle")
    private String workTitle;

    private Boolean Sun;
    private Boolean Mon;
    private Boolean Tue;
    private Boolean Wed;
    private Boolean Thu;
    private Boolean Fri;
    private Boolean Sat;

    private int hourMoney;

    public Work(String workTitle, Boolean Sun, Boolean Mon, Boolean Tue, Boolean Wed, Boolean Thu, Boolean Fri, Boolean Sat, int hourMoney) {
        this.workTitle = workTitle;
        this.Sun = Sun;
        this.Mon = Mon;
        this.Tue = Tue;
        this.Wed = Wed;
        this.Thu = Thu;
        this.Fri = Fri;
        this.Sat = Sat;
        this.hourMoney = hourMoney;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public Boolean getSun() {
        return Sun;
    }

    public void setSun(Boolean sun) {
        Sun = sun;
    }

    public Boolean getMon() {
        return Mon;
    }

    public void setMon(Boolean mon) {
        Mon = mon;
    }

    public Boolean getTue() {
        return Tue;
    }

    public void setTue(Boolean tue) {
        Tue = tue;
    }

    public Boolean getWed() {
        return Wed;
    }

    public void setWed(Boolean wed) {
        Wed = wed;
    }

    public Boolean getThu() {
        return Thu;
    }

    public void setThu(Boolean thu) {
        Thu = thu;
    }

    public Boolean getFri() {
        return Fri;
    }

    public void setFri(Boolean fri) {
        Fri = fri;
    }

    public Boolean getSat() {
        return Sat;
    }

    public void setSat(Boolean sat) {
        Sat = sat;
    }

    public int getHourMoney() {
        return hourMoney;
    }

    public void setHourMoney(int hourMoney) {
        this.hourMoney = hourMoney;
    }
}