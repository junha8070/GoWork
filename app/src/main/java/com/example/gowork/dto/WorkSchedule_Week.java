package com.example.gowork.dto;

import java.util.HashMap;

public class WorkSchedule_Week {

    HashMap<String, Boolean> work_date;
    Boolean holiday;

    public HashMap<String, Boolean> getWork_date() {
        return work_date;
    }

    public void setWork_date(HashMap<String, Boolean> work_date) {
        this.work_date = work_date;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public WorkSchedule_Week(HashMap<String, Boolean> work_date, Boolean holiday) {
        this.work_date = work_date;
        this.holiday = holiday;
    }
}
