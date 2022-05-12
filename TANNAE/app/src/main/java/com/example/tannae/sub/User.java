package com.example.tannae.sub;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class User extends AppCompatActivity {
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static Context context;

    public User(Context context) {
        this.context = context;
    }

    public void setSharedPreferences() {
        sp = context.getSharedPreferences("UserSP", MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void setUserOutTOIn(JSONObject user) throws JSONException {
        editor.putString("usn", user.getString("usn"));
        editor.putString("id", user.getString("id"));
        editor.putString("pw", user.getString("pw"));
        editor.putString("uname", user.getString("uname"));
        editor.putString("rrn", user.getString("rrn"));
        editor.putInt("gender", user.getInt("gender"));
        editor.putString("phone", user.getString("phone"));
        editor.putString("email", user.getString("email"));
        editor.putInt("drive", user.getInt("drive"));
        editor.putInt("points", user.getInt("points"));
        editor.putFloat("score", BigDecimal.valueOf(user.getDouble("score")).floatValue());
        editor.putInt("state", user.getInt("state"));
        editor.apply();
    }

    public static void setUserInTOOut(JSONObject user) throws JSONException {
        user.put("usn", sp.getString("usn", ""));
        user.put("id", sp.getString("id", ""));
        user.put("pw", sp.getString("pw", ""));
        user.put("uname", sp.getString("uname", ""));
        user.put("rrn", sp.getString("rrn", ""));
        user.put("gender", sp.getInt("gender", 1));
        user.put("phone", sp.getString("phone", ""));
        user.put("email", sp.getString("email", ""));
        user.put("drive", sp.getInt("drive", 0));
        user.put("points", sp.getInt("points", 0));
        user.put("score", sp.getFloat("score", (float) 0.0));
        user.put("state", sp.getInt("state", 0));
    }
}
