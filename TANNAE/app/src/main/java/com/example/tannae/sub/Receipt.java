package com.example.tannae.sub;

import java.util.HashMap;

public class Receipt {
    private HashMap<String, Object> data = new HashMap<>();

    public Receipt(String usn, String origin, String destination, int cost) {
        data.put("usn", usn);
        data.put("origin", origin);
        data.put("destination", destination);
        data.put("cost", cost);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
