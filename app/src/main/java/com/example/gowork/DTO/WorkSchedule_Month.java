package com.example.gowork.DTO;

public class WorkSchedule_Month {

    WorkInfo workInfo;
    String work_day;
    Boolean last_day;
    Boolean holiday;

    public WorkInfo getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(WorkInfo workInfo) {
        this.workInfo = workInfo;
    }

    public String getWork_day() {
        return work_day;
    }

    public void setWork_day(String work_day) {
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

    public WorkSchedule_Month(WorkInfo workInfo, String work_day, Boolean last_day, Boolean holiday) {
        this.workInfo = workInfo;
        this.work_day = work_day;
        this.last_day = last_day;
        this.holiday = holiday;
    }
}
