package com.noobcoder.ytdownloader.api;

import com.noobcoder.ytdownloader.DTO.LoginRequest;
import com.noobcoder.ytdownloader.DTO.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface YTBackendApi {
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}