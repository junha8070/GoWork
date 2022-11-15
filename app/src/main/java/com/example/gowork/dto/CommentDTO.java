package com.example.gowork.dto;

public class CommentDTO {

    String postId;
    String userUID;
    String profile;
    String name;
    String contents;
    String time;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getProfile() {
        return profile;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CommentDTO(String postId, String userUID, String profile, String name, String contents, String time) {
        this.postId = postId;
        this.userUID = userUID;
        this.profile = profile;
        this.name = name;
        this.contents = contents;
        this.time = time;
    }
}
