package com.example.tannae.entity;

import java.util.HashMap;

public class ContentEntity {
    // Data
    private HashMap<String, Object> content = new HashMap<>();

    // Constructor
    public ContentEntity() {
        content.put("csn", null);
        content.put("title", null);
        content.put("que", null);
        content.put("ans", null);
        content.put("usn", null);
    }
    public ContentEntity(String csn, String title, String que, String ans, String usn) {
        content.put("csn", csn);
        content.put("title", title);
        content.put("que", que);
        content.put("ans", ans);
        content.put("usn", usn);
    }

    // Get & Set
    public Object getData(String key) { return content.get(key); }
    public void setData(String key, Object value) { content.put(key, value); }
}
