package com.example.gowork.Interface;

import com.example.gowork.DTO.KakaoAddressRequest;
import com.example.gowork.DTO.KakaoAddressResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KakaoAddressInterface {

    //@통신 방식("통신 API명")
    @GET("/v2/local/search/keyword.jsp")
    Call<KakaoAddressResponse> getAddressInfo(@Query("query") KakaoAddressRequest query);

}
