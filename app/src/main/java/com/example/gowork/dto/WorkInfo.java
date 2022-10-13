package com.example.gowork.dto;

import java.util.HashMap;

public class WorkInfo {

    String place_name;
    String place_address;
    String kind;
    String pay;
    String join;
    String resign;
    String probation;
    String start_time;
    String end_time;
    Boolean holiday_allowance;
    Boolean sameTime_5upper;
    Boolean work_holiday;

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getResign() {
        return resign;
    }

    public void setResign(String resign) {
        this.resign = resign;
    }

    public String getProbation() {
        return probation;
    }

    public void setProbation(String probation) {
        this.probation = probation;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Boolean getHoliday_allowance() {
        return holiday_allowance;
    }

    public void setHoliday_allowance(Boolean holiday_allowance) {
        this.holiday_allowance = holiday_allowance;
    }

    public Boolean getSameTime_5upper() {
        return sameTime_5upper;
    }

    public void setSameTime_5upper(Boolean sameTime_5upper) {
        this.sameTime_5upper = sameTime_5upper;
    }

    public Boolean getWork_holiday() {
        return work_holiday;
    }

    public void setWork_holiday(Boolean work_holiday) {
        this.work_holiday = work_holiday;
    }

    public WorkInfo(String place_name, String place_address, String kind, String pay, String join, String resign, String probation, String start_time, String end_time, Boolean holiday_allowance, Boolean sameTime_5upper, Boolean work_holiday) {
        this.place_name = place_name;
        this.place_address = place_address;
        this.kind = kind;
        this.pay = pay;
        this.join = join;
        this.resign = resign;
        this.probation = probation;
        this.start_time = start_time;
        this.end_time = end_time;
        this.holiday_allowance = holiday_allowance;
        this.sameTime_5upper = sameTime_5upper;
        this.work_holiday = work_holiday;
    }
}
