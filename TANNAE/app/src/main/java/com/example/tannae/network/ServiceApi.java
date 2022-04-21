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
    Call<String> login(@Query("id") String id, @Query("pw") String pw);
    @GET("account/checkID")                 // Check ID Duplication
    Call<String> checkID(@Query("id") String id);
    @POST("/account/signup")                // Sign Up
    Call<String> signup(@Body JSONObject user);
    @GET("/account/findAccount")
    Call<String> findAccount(@Query("uname") String uname, @Query("rrn") String rrn, @Query("phone") String phone, @Query("email") String email);

    /* User */

    /* Driver */

    /* Passenger */
}