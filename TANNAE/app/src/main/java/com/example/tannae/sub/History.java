package com.example.tannae.sub;

import java.util.HashMap;

public class History {
    private HashMap<String, Object> data = new HashMap<>();

    public History(String date, String origin, String destination, int cost) {
        data.put("date", date);
        data.put("origin", origin);
        data.put("destination", destination);
        data.put("cost", cost);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
