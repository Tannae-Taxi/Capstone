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
    @GET("/account/checkID")                 // Check ID Duplication
    Call<String> checkID(@Query("id") String id);
    @GET("/account/checkUser")
    Call<String> checkUser(@Query("name") String name, @Query("rrn") String rrn);
    @POST("/account/signup")                // Sign Up
    Call<Boolean> signup(@Body JSONObject user);
    @GET("/account/findAccount")            // Find Account
    Call<String> findAccount(@Query("uname") String uname, @Query("rrn") String rrn, @Query("email") String email, @Query("phone") String phone);
    @POST("/account/signout")               // Sign Out
    Call<Boolean> signout(@Body JSONObject user);
    @POST("/account/editAccount")           // Edit Account
    Call<Boolean> editAccount(@Body JSONObject user);

    /* User */
    @POST("/user/charge")                   // Charge Point
    Call<Boolean> charge(@Body JSONObject user);
    @GET("/user/getHistory")                // Get history
    Call<String> getHistory(@Query("usn") String usn);
    @GET("/user/getLost")                   // Get lost
    Call<String> getLost();
    @POST("/user/postLost")                 // Post lost
    Call<Boolean> postLost(@Body JSONObject lost);
    @GET("/user/getContent")                // Get content
    Call<String> getContent();
    @POST("/user/editContent")              // Edit content
    Call<Boolean> editContent(@Body JSONObject content);
    @POST("/user/deleteContent")            // Delete content
    Call<Boolean> deleteContent(@Body JSONObject content);
    @POST("/user/postContent")              // Post Content
    Call<Boolean> postContent(@Body JSONObject content);
    @POST("/user/evaluate")                 // Post evaluate
    Call<Boolean> evaluate(@Body JSONObject data);
}