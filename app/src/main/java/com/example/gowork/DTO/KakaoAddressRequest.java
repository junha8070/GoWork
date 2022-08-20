package com.example.gowork.DTO;

import com.google.gson.annotations.SerializedName;

public class KakaoAddressRequest {

//    @SerializedName("format")
//    public String format;

    @SerializedName("query")
    public String query;

//    public String getFormat() {
//        return format;
//    }
//
//    public void setFormat(String format) {
//        this.format = format;
//    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
