package com.example.gowork.DTO;

public class WorkSchedule_day {

    WorkInfo workInfo;
    Boolean holiday;

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public WorkInfo getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(WorkInfo workInfo) {
        this.workInfo = workInfo;
    }

    public WorkSchedule_day(WorkInfo workInfo, Boolean holiday) {
        this.workInfo = workInfo;
        this.holiday = holiday;
    }
}
