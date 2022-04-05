package com.example.tannae.entity;

import java.util.HashMap;

public class PathEntity {
    // Data
    private HashMap<String, Object> path = new HashMap<>();

    // Constructor
    public PathEntity() {
        path.put("vpsn", null);
        path.put("paths", null); path.put("op", null); path.put("dp", null);
        path.put("pp", null); path.put("opp", null); path.put("dpp", null);
        path.put("psgs", null); path.put("cost", null);
    }
    public PathEntity(String vpsn, String paths, String op, String dp, String pp, String opp, String dpp, String psgs, String cost) {
        path.put("vpsn", vpsn);
        path.put("paths", paths); path.put("op", op); path.put("dp", dp);
        path.put("pp", pp); path.put("opp", opp); path.put("dpp", dpp);
        path.put("psgs", psgs); path.put("cost", cost);
    }

    // Get & Set
    public Object getData(String key) { return path.get(key); }
    public void setData(String key, Object value) { path.put(key, value); }
}
