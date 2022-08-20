package com.example.gowork.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class KakaoAddressResponse {

    @SerializedName("documents")
    ArrayList<KakaoAddressDocumentsPojo> kakaoAddressDocumentsPojos;

    @SerializedName("meta")
    KakaoAddressMetaPojo kakaoAddressMetaPojo;

    public ArrayList<KakaoAddressDocumentsPojo> getKakaoAddressDocumentsPojos() {
        return kakaoAddressDocumentsPojos;
    }

    public KakaoAddressMetaPojo getKakaoAddressMetaPojo() {
        return kakaoAddressMetaPojo;
    }
}
