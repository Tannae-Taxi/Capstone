package com.example.tannae.entity;

import java.util.HashMap;

public class UserEntity {
    // Data
    private HashMap<String, Object> user = new HashMap<>(); // usn, id, pw, uname, birth, sex, phone, email, drive, points, score

    // Constructor
    public UserEntity() {
        user.put("usn", null); user.put("id", null); user.put("pw", null);
        user.put("uname", null); user.put("birth", null); user.put("sex", true);
        user.put("phone", null); user.put("email", null);
        user.put("drive", null); user.put("points", null); user.put("score", null);
    }
    public UserEntity(String usn, String id, String pw, String uname, Integer birth, Boolean sex, String phone, String email, Boolean drive, Integer points, Float score) {
        user.put("usn", usn); user.put("id", id); user.put("pw", pw);
        user.put("uname", uname); user.put("birth", birth); user.put("sex", sex);
        user.put("phone", phone); user.put("email", email);
        user.put("drive", drive); user.put("points", points); user.put("score", score);
    }

    // Get & Set
    public Object getData(String key) { return user.get(key); }
    public void setData(String key, Object value) { user.put(key, value); }
}
