package com.example.gowork.dto;

import android.net.Uri;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PostDTO {

    String postId;
    String name;
    String title;
    String contents;
    Uri photo;
    String timestamp;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public PostDTO(String postId, String name, String title, String contents, Uri photo, String timestamp) {
        this.postId = postId;
        this.name = name;
        this.title = title;
        this.contents = contents;
        this.photo = photo;
        this.timestamp = timestamp;
    }
}
