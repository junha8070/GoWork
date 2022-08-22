package com.example.gowork.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gowork.DTO.KakaoAddressRequest;
import com.example.gowork.DTO.KakaoAddressResponse;
import com.example.gowork.Model.AddressModel;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressViewModel extends AndroidViewModel {

    private AddressModel addressModel;

    private LiveData<KakaoAddressResponse> kakaoAddressResponses;

    public AddressViewModel(@NonNull Application application) {
        super(application);

        addressModel = new AddressModel(application);
        kakaoAddressResponses = addressModel.getAddressInfo();

    }

    public void responseAddressInfo(KakaoAddressRequest kakaoAddressRequest){
        addressModel.responseAddressInfo(kakaoAddressRequest);
    }

    public LiveData<KakaoAddressResponse> getAddressInfo() {
        return kakaoAddressResponses;
    }
}
