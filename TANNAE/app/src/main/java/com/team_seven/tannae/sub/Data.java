package com.team_seven.tannae.sub;

import android.content.Context;

import java.util.HashMap;

public class Data {
    private final HashMap<String, Object> data = new HashMap<>();
    private Context context;
    private final String cType;

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

    public Data(String usn, String title, String content, String answer, String date, int state, String cType) {
        data.put("usn", usn);
        data.put("title", title);
        data.put("content", content);
        data.put("answer", answer);
        data.put("date", date);
        data.put("state", state == 1);
        this.cType = cType;
    }

    public Data(String csn, String usn, String title, String content, String answer, String date, int state, Context context, String cType) {
        data.put("csn", csn);
        data.put("usn", usn);
        data.put("title", title);
        data.put("content", content);
        data.put("answer", answer);
        data.put("date", date);
        data.put("state", state == 1);
        this.context = context;
        this.cType = cType;
    }
    public Object getData(String key) {
        return data.get(key);
    }
    public String getType() {
        return cType;
    }
}
