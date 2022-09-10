package com.example.gowork.dto;

import java.util.HashMap;

public class WorkInfo {

    String place_name;
    String place_address;
    String pay;

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

    public WorkInfo(String place_name, String place_address, String pay) {
        this.place_name = place_name;
        this.place_address = place_address;
        this.pay = pay;
    }
}
