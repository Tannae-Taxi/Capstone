package com.example.tannae.sub;

import java.util.HashMap;

public class Data<T> {
    private HashMap<String, Object> data = new HashMap<>();
    private String cType;

    public Data(String date, String license, String type, String cType) {
        data.put("date", date);
        data.put("license", license);
        data.put("type", type);
        this.cType = cType;
    }
    public Data(String ud, String origin, String destination, int cost, String cType) {
        data.put("ud", ud);
        data.put("origin", origin);
        data.put("destination", destination);
        data.put("cost", cost);
        this.cType = cType;
    }
    public Object getData(String key) {
        return data.get(key);
    }
    public String getType() {
        return cType;
    }
}
