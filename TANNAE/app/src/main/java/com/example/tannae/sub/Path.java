package com.example.tannae.sub;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Path extends AppCompatActivity {
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static Context context;

    public Path(Context context) { this.context = context; }

    public void setSharedPreferences() {
        sp = context.getSharedPreferences("PathSP", MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void setUserOutTOIn(JSONObject path) throws JSONException {
        editor.putString("vpsn", path.getString("vpsn"));
        editor.putString("paths", path.getString("paths"));
        editor.putString("op", path.getString("op"));
        editor.putString("dp", path.getString("dp"));
        editor.putString("pp", path.getString("pp"));
        editor.putString("opp", path.getString("opp"));
        editor.putString("dpp", path.getString("dpp"));
        editor.putString("psgs", path.getString("psgs"));
        editor.putInt("cost", path.getInt("cost"));
        editor.apply();
    }

    public static void setUserInTOOut(JSONObject path) throws JSONException {
        path.put("vpsn", sp.getString("vpsn", ""));
        path.put("paths", sp.getString("paths", ""));
        path.put("op", sp.getString("op", ""));
        path.put("dp", sp.getString("dp", ""));
        path.put("pp", sp.getString("pp", ""));
        path.put("opp", sp.getString("opp", ""));
        path.put("dpp", sp.getString("dpp", ""));
        path.put("psgs", sp.getString("psgs", ""));
        path.put("cost", sp.getInt("cost", 0));
    }

    public static void setNull(){
        editor.putString("vpsn", null);
        editor.putString("paths", null);
        editor.putString("op", null);
        editor.putString("dp", null);
        editor.putString("pp", null);
        editor.putString("opp", null);
        editor.putString("dpp", null);
        editor.putString("psgs", null);
        editor.putInt("cost", 0); ////////// null 설정이 0이 맞는지
        editor.apply();
    }
}
