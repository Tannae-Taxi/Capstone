package com.example.tannae.entity;

import java.util.HashMap;

public class LostEntity {
    // Data
    private HashMap<String, Object> lost = new HashMap<>();

    // Constructor
    public LostEntity() {
        lost.put("lsn", null);
        lost.put("date", null);
        lost.put("type", null);
        lost.put("vsn", null);
    }
    public LostEntity(String lsn, Integer date, String type, String vsn) {
        lost.put("lsn", lsn);
        lost.put("date", date);
        lost.put("type", type);
        lost.put("vsn", vsn);
    }

    // Get & Set
    public Object getData(String key) { return lost.get(key); }
    public void setData(String key, Object value) { lost.put(key, value); }
}
