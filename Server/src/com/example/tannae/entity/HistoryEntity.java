package com.example.tannae.entity;

import java.util.HashMap;

public class HistoryEntity {
    // Data
    private HashMap<String, Object> history = new HashMap<>();

    // Constructor
    public HistoryEntity() {
        history.put("hsn", null);
        history.put("license", null);
        history.put("date", null);
        history.put("origin", null);
        history.put("dest", null);
        history.put("cost", null);
        history.put("usn", null);
    }
    public HistoryEntity(String hsn, String license, Integer date, String origin, String dest, Integer cost, String usn) {
        history.put("hsn", hsn);
        history.put("license", license);
        history.put("date", date);
        history.put("origin", origin);
        history.put("dest", dest);
        history.put("cost", cost);
        history.put("usn", usn);
    }

    // Get & Set
    public Object getData(String key) { return history.get(key); }
    public void setData(String key, Object value) { history.put(key, value); }
}
