package com.example.gowork.DTO;

import com.google.gson.annotations.SerializedName;

public class KakaoAddressMetaPojo {

    @SerializedName("total_count")
    int total_count;

    @SerializedName("pageable_count")
    int pageable_count;

    @SerializedName("is_end")
    Boolean is_end;

    @SerializedName("same_name")
    KakaoAddressSame_NamePojo kakaoAddressSame_namePojo;
}
