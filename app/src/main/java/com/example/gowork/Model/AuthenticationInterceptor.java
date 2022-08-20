package com.example.gowork.Model;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

     private String authToken;

    public AuthenticationInterceptor() {
        // 원래 코드
        this.authToken = "dbfcf07ffa2f4988e3aa5784db8642df";

        // 임시 코드
//        this.authToken = "PMAK-62c2bb573eaf6129f000481d-7a7ec096d3dd182e419dc0fb7e0473d544";
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                // Original Token Code
                .header("Authorization", "KakaoAK " + authToken);
                // MockServer Token Code
//                .header("X-Api-Key", "PMAK-62c2bb573eaf6129f000481d-7a7ec096d3dd182e419dc0fb7e0473d544");

        Request request = builder.build();
        return chain.proceed(request);
    }
}
