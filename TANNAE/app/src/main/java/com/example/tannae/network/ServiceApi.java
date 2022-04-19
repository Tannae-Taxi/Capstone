package com.example.tannae.network;

import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {
    /* Account */
    @GET("/account/login")                  // Login
    Call<String> login(@Query("id") String id, @Query("pw") String pw, @Query("ip") String ip);
}