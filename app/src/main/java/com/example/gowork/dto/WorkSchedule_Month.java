package com.example.gowork.dto;

public class WorkSchedule_Month {

    int work_day;
    Boolean last_day;
    Boolean holiday;

    public int getWork_day() {
        return work_day;
    }

    public void setWork_day(int work_day) {
        this.work_day = work_day;
    }

    public Boolean getLast_day() {
        return last_day;
    }

    public void setLast_day(Boolean last_day) {
        this.last_day = last_day;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public WorkSchedule_Month( int work_day, Boolean last_day, Boolean holiday) {
        this.work_day = work_day;
        this.last_day = last_day;
        this.holiday = holiday;
    }
}
