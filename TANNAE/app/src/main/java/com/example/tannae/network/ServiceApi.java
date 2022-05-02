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
    @POST("/account/signup")                // Sign Up
    Call<String> signup(@Body JSONObject user);
    @GET("/account/findAccount")            // Find Account
    Call<String> findAccount(@Query("uname") String uname, @Query("rrn") String rrn, @Query("email") String email, @Query("phone") String phone);
    @POST("/account/signout")               // Sign Out
    Call<String> signout(@Body JSONObject user);
    @POST("/account/editAccount")           // Edit Account
    Call<String> editAccount(@Body JSONObject user);

    /* User */
    @POST("/user/charge")                   // Charge Point
    Call<String> charge(@Body JSONObject point);
    @GET("/user/getHistory")                // Get history
    Call<String> getHistory(@Query("usn") String usn);
    @GET("/user/getLost")                   // Get lost
    Call<String> getLost();
    @POST("/user/postLost")                 // Post lost
    Call<String> postLost(@Body JSONObject lost);
    @GET("/user/getContent")                // Get content
    Call<String> getContent();
    @POST("/user/editContent")              // Edit content
    Call<String> editContent(@Body JSONObject content);
    @POST("/user/postContent")              // Post Content
    Call<String> postContent(@Body JSONObject content);

    /* Passenger */
    @GET("/passenger/reqVehicles")          // Request Vehicle list
    Call<String> getVehicle(@Query("originN") String originN, @Query("originX") double originX, @Query("originY") double oy, @Query("destN") String destN, @Query("destX") double destX, @Query("destY") double destY, @Query("share") boolean share);
}