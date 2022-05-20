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
    Call<JSONObject> login(@Query("id") String id, @Query("pw") String pw);
    @GET("/account/checkID")                 // Check ID Duplication
    Call<String> checkID(@Query("id") String id);
    @POST("/account/signup")                // Sign Up
    Call<Boolean> signup(@Body JSONObject user);
    @GET("/account/findAccount")            // Find Account
    Call<JSONObject> findAccount(@Query("uname") String uname, @Query("rrn") String rrn, @Query("email") String email, @Query("phone") String phone);
    @POST("/account/signout")               // Sign Out
    Call<Boolean> signout(@Body JSONObject user);
    @POST("/account/editAccount")           // Edit Account
    Call<Boolean> editAccount(@Body JSONObject user);

    /* User */
    @POST("/user/charge")                   // Charge Point
    Call<Boolean> charge(@Body JSONObject point);
    @GET("/user/getHistory")                // Get history
    Call<JSONObject> getHistory(@Query("usn") String usn);
    @GET("/user/getLost")                   // Get lost
    Call<JSONObject> getLost();
    @POST("/user/postLost")                 // Post lost
    Call<Boolean> postLost(@Body JSONObject lost);
    @GET("/user/getContent")                // Get content
    Call<JSONObject> getContent();
    @POST("/user/editContent")              // Edit content
    Call<Boolean> editContent(@Body JSONObject content);
    @POST("/user/postContent")              // Post Content
    Call<Boolean> postContent(@Body JSONObject content);
    @POST("/user/evaluate")                 // Post evaluate
    Call<Boolean> evaluate(@Body JSONObject data);
}