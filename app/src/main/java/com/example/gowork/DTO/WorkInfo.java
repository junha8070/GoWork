package com.example.gowork.DTO;

public class WorkInfo {

    String place_name;
    String place_address;
    String pay;
    Boolean[] work_schedule = new Boolean[3];

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

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public Boolean[] getWork_schedule() {
        return work_schedule;
    }

    public void setWork_schedule(Boolean[] work_schedule) {
        this.work_schedule = work_schedule;
    }

    public WorkInfo(String place_name, String place_address, String pay, Boolean[] work_schedule) {
        this.place_name = place_name;
        this.place_address = place_address;
        this.pay = pay;
        this.work_schedule = work_schedule;
    }
}
