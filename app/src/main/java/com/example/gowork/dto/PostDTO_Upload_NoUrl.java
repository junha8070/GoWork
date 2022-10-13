package com.example.gowork.dto;

import java.sql.Timestamp;

public class PostDTO_Upload_NoUrl {

    String id;
    String title;
    String contents;
    Timestamp time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public PostDTO_Upload_NoUrl(String id, String title, String contents, Timestamp time) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.time = time;
    }
}
