package com.example.tannae.activity.main_service;

public class Receipt {

    private static String name;

    public Receipt(String name) {
        this.name = name;
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
