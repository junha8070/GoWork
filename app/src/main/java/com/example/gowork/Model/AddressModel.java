package com.example.gowork.Model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.DTO.KakaoAddressRequest;
import com.example.gowork.DTO.KakaoAddressResponse;
import com.example.gowork.Interface.KakaoAddressInterface;
import com.example.gowork.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressModel {

    private String TAG = "AddressModel";

    private Application application;

    private MutableLiveData<KakaoAddressResponse> kakaoAddressResponsesData;
    private KakaoAddressInterface kakaoAddressInterface;
    private KakaoAddressRequest kakaoAddressRequest;
    private KakaoAddressResponse kakaoAddressResponse;

    public AddressModel(Application application) {
        this.application = application;

        kakaoAddressResponsesData = new MutableLiveData<>();
    }

    public void responseAddressInfo(KakaoAddressRequest kakaoAddressRequest) {
        kakaoAddressInterface = ServiceGenerator.createService(KakaoAddressInterface.class);
//        kakaoAddressRequest = new KakaoAddressRequest();
        kakaoAddressInterface.getAddressInfo(kakaoAddressRequest).enqueue(new Callback<KakaoAddressResponse>() {
            @Override
            public void onResponse(Call<KakaoAddressResponse> call, Response<KakaoAddressResponse> response) {
                if (response.isSuccessful()) {
                    KakaoAddressResponse result = response.body();
                    kakaoAddressResponsesData.postValue(result);
                } else {

                }
            }

            @Override
            public void onFailure(Call<KakaoAddressResponse> call, Throwable t) {

            }
        });
    }

    public LiveData<KakaoAddressResponse> getAddressInfo() {
        return kakaoAddressResponsesData;
    }
}
