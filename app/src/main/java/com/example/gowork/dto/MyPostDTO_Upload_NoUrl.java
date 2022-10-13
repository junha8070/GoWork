package com.example.gowork.dto;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MyPostDTO_Upload_NoUrl {

    String title;
    String contents;
    Timestamp time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public MyPostDTO_Upload_NoUrl(String title, String contents, Timestamp time) {
        this.title = title;
        this.contents = contents;
        this.time = time;
    }
}
