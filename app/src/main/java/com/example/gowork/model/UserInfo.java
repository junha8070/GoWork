package com.example.gowork.model;

public class UserInfo {

    String name;
    String email;
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserInfo(String email, String name, String phone){
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
