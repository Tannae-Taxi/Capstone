package com.example.tannae.sub;

import java.util.HashMap;

public class Lost {
    private HashMap<String, Object> data = new HashMap<>();

    public Lost(String date, String license, String type) {
        data.put("date", date);
        data.put("license", license);
        data.put("type", type);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
