package com.example.gowork.Interface;

import com.example.gowork.dto.KakaoAddressResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoAddressInterface {

    //@통신 방식("통신 API명")
    @GET("/v2/local/search/keyword.json")
    Call<KakaoAddressResponse> getAddressInfo(@Header ("Authorization") String key, @Query("query") String query);

}
