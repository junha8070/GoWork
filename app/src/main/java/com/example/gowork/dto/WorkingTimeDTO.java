package com.example.gowork.dto;

import java.util.HashMap;

public class WorkingTimeDTO {

    String working_Place;
    HashMap<String, String> time;

    public String getWorking_Place() {
        return working_Place;
    }

    public void setWorking_Place(String working_Place) {
        this.working_Place = working_Place;
    }

    public HashMap<String, String> getTime() {
        return time;
    }

    public void setTime(HashMap<String, String> time) {
        this.time = time;
    }

    public WorkingTimeDTO(String working_Place, HashMap<String, String> time) {
        this.working_Place = working_Place;
        this.time = time;
    }
}
