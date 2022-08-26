package com.example.gowork.dto;

public class WorkSchedule_day {

    Boolean holiday;

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public WorkSchedule_day(Boolean holiday) {
        this.holiday = holiday;
    }
}
