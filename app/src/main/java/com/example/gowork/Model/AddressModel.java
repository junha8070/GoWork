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
    private String key = "KakaoAK dbfcf07ffa2f4988e3aa5784db8642df";

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
        Log.d(TAG, "주소 끌고오기 1");
        kakaoAddressInterface = ServiceGenerator.createService(KakaoAddressInterface.class);
//        kakaoAddressRequest = new KakaoAddressRequest();
        kakaoAddressInterface.getAddressInfo(key, kakaoAddressRequest.query).enqueue(new Callback<KakaoAddressResponse>() {
            @Override
            public void onResponse(Call<KakaoAddressResponse> call, Response<KakaoAddressResponse> response) {
                Log.d(TAG, "주소 끌고오는 중");
                if (response.isSuccessful()) {
                    KakaoAddressResponse result = response.body();
                    Log.d(TAG, call.request().toString());
                    Log.d(TAG, response.message());
                    Log.d(TAG, String.valueOf(result.getKakaoAddressDocumentsPojos().size()));
//                    Log.d(TAG, result.getKakaoAddressDocumentsPojos().get(0).getRoad_address_name());
                    kakaoAddressResponsesData.postValue(result);
                } else {
                    Log.d(TAG, "주소 실패");
                    Log.d(TAG, response.errorBody().toString());
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<KakaoAddressResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    public LiveData<KakaoAddressResponse> getAddressInfo() {
        return kakaoAddressResponsesData;
    }
}
