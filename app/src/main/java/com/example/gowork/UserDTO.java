package com.example.gowork;

public class UserDTO {

    String name;
    String id;
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserDTO(String name, String id, String phone){
        this.name = name;
        this.id = id;
        this.phone = phone;
    }
}
