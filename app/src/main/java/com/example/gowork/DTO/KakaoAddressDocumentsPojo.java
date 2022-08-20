package com.example.gowork.DTO;

import com.google.gson.annotations.SerializedName;

public class KakaoAddressDocumentsPojo {

    @SerializedName("address_name")
    String address_name;

    @SerializedName("category_name")
    String category_name;

    @SerializedName("place_name")
    String place_name;

    @SerializedName("road_address_name")
    String road_address_name;

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public void setRoad_address_name(String road_address_name) {
        this.road_address_name = road_address_name;
    }
}
