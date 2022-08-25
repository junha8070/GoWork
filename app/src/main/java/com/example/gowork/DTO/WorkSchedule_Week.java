package com.example.gowork.DTO;

public class WorkSchedule_Week {

    WorkInfo workInfo;
    Boolean[] work_date;
    Boolean holiday;

    public WorkInfo getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(WorkInfo workInfo) {
        this.workInfo = workInfo;
    }

    public Boolean[] getWork_date() {
        return work_date;
    }

    public void setWork_date(Boolean[] work_date) {
        this.work_date = work_date;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public WorkSchedule_Week(WorkInfo workInfo, Boolean[] work_date, Boolean holiday) {
        this.workInfo = workInfo;
        this.work_date = work_date;
        this.holiday = holiday;
    }
}
