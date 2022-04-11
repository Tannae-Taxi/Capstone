package com.example.tannae.network;

import com.example.tannae.data.request.LoginRequest;
import com.example.tannae.data.request.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginRequest data);
}