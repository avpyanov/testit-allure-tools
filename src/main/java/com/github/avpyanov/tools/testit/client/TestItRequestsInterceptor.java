package com.github.avpyanov.tools.testit.client;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TestItRequestsInterceptor implements Interceptor {

    private final String privateToken;

    public TestItRequestsInterceptor(String privateToken) {
        this.privateToken = privateToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request requestWithAuthorization = request.newBuilder()
                .addHeader("Authorization", "PrivateToken " + privateToken)
                .build();
        return chain.proceed(requestWithAuthorization);
    }
}
