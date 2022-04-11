package com.example.tannae.data.request;

public class LoginResponse {
    private int flag;
    private String message;

    public LoginResponse(int flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public int getFlag() { return flag; }
    public String getMessage() { return message; }
}
