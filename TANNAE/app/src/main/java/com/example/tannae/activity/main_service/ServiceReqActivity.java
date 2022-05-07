package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

// << ServiceReq Activity >>
public class ServiceReqActivity extends AppCompatActivity {
    private EditText etOrigin, etDest;
    private Button btnNext, btnBack;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicereq);
        // Setting
        setViews();
        setEventListeners();

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

    }

    // < Register views >
    private void setViews() {
        etOrigin = findViewById(R.id.et_origin_servicereq);
        etDest = findViewById(R.id.et_dest_servicereq);
        btnNext = findViewById(R.id.btn_next_detailservicereq);
        btnBack = findViewById(R.id.btn_back_detailservicereq);
    }

    // < Register event listeners >
    private void setEventListeners() {
        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Request Service [SOCKET]
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Create JSON
                    JSONObject start = new JSONObject();
                    start.put("name", etOrigin.getText().toString());
                    start.put("x", 127.11024293202674); ////////////////////////////////////// 출발지 이름에 대한 x 좌표 가져오기
                    start.put("y", 37.394348634049784); ////////////////////////////////////// 출발지 이름에 대한 y 좌표 가져오기
                    JSONObject end = new JSONObject();
                    end.put("name", etDest.getText().toString());
                    end.put("x", 127.11024293202674); //////////////////////////////////////// 목적지 이름에 대한 x 좌표 가져오기
                    end.put("y", 37.394348634049784); //////////////////////////////////////// 목적지 이름에 대한 y 좌표 가져오기
                    JSONObject user = new JSONObject();
                    //////////////////////////////////////////////////// 현재 로그인되어 있는 User(SQLite 에 저장된) 정보를 json 형태로 전환
                    JSONObject data = new JSONObject();
                    data.put("start", start);
                    data.put("end", end);
                    data.put("share", true); ///////////////////////////////////////////////// Switch button 에서 동승 서비스 여부를 이용할 것인지에 대한 Boolean 값 삽입
                    data.put("user", user);
                    Network.socket.emit("requestService", data);
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.putExtra("type", false);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
