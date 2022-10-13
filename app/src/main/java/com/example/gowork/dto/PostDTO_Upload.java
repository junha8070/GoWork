package com.example.gowork.dto;

import android.net.Uri;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PostDTO_Upload {

    String name;
    String title;
    String contents;
    Uri photo;
    String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PostDTO_Upload(String name, String title, String contents, Uri photo, String time) {
        this.name = name;
        this.title = title;
        this.contents = contents;
        this.photo = photo;
        this.time = time;
    }
}
