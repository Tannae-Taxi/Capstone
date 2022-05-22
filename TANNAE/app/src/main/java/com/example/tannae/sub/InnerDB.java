package com.example.tannae.sub;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class InnerDB extends AppCompatActivity {
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static Context context;

    public InnerDB(Context context) {
        this.context = context;
    }

    public void setSharedPreferences() {
        sp = context.getSharedPreferences("UserSP", MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void setUser(JSONObject user) throws JSONException {
        editor.putString("usn", user.getString("usn"))
                .putString("id", user.getString("id"))
                .putString("pw", user.getString("pw"))
                .putString("uname", user.getString("uname"))
                .putString("rrn", user.getString("rrn"))
                .putInt("gender", user.getInt("gender"))
                .putString("phone", user.getString("phone"))
                .putString("email", user.getString("email"))
                .putInt("drive", user.getInt("drive"))
                .putInt("points", user.getInt("points"))
                .putFloat("score", BigDecimal.valueOf(user.getDouble("score")).floatValue())
                .putInt("state", user.getInt("state"))
                .apply();
    }

    public static JSONObject getUser() throws JSONException {
        return new JSONObject()
                .put("usn", sp.getString("usn", ""))
                .put("id", sp.getString("id", ""))
                .put("pw", sp.getString("pw", ""))
                .put("uname", sp.getString("uname", ""))
                .put("rrn", sp.getString("rrn", ""))
                .put("gender", sp.getInt("gender", 1))
                .put("phone", sp.getString("phone", ""))
                .put("email", sp.getString("email", ""))
                .put("drive", sp.getInt("drive", 0))
                .put("points", sp.getInt("points", 0))
                .put("score", sp.getFloat("score", (float) 0.0))
                .put("state", sp.getInt("state", 0));
    }
}
