package com.example.gowork.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gowork.dto.KakaoAddressRequest;
import com.example.gowork.dto.KakaoAddressResponse;
import com.example.gowork.model.AddressModel;

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
