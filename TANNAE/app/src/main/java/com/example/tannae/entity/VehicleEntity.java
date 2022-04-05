package com.example.tannae.entity;

import java.util.HashMap;

public class VehicleEntity {
    // Data
    private HashMap<String, Object> vehicle = new HashMap<>();

    // Constructor
    public VehicleEntity() {
        vehicle.put("vsn", null);
        vehicle.put("state", null);
        vehicle.put("num", null);
        vehicle.put("pos", null);
        vehicle.put("license", null);
        vehicle.put("usn", null);
    }
    public VehicleEntity(String vsn, Boolean state, Integer num, String pos, String license, String usn) {
        vehicle.put("vsn", vsn);
        vehicle.put("state", state);
        vehicle.put("num", num);
        vehicle.put("pos", pos);
        vehicle.put("license", license);
        vehicle.put("usn", usn);
    }

    // Get & Set
    public Object getData(String key) { return vehicle.get(key); }
    public void setData(String key, Object value) { vehicle.put(key, value); }
}
