package com.example.tannae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class ServiceReqActivity extends AppCompatActivity {
    private ImageButton ibMap;
    private EditText etOrigin, etDest;
    private Button btnNext, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicereq);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        ibMap = findViewById(R.id.ib_map_servicereq);
        etOrigin = findViewById(R.id.et_origin_servicereq);
        etDest = findViewById(R.id.et_Desti_servicereq);
        btnNext = findViewById(R.id.btn_next_detailservicereq);
        btnBack = findViewById(R.id.btn_back_detailservicereq);
    }

    private void setEventListeners() {
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Network.socket.connect();
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
                    Network.socket.on("responseFail", args -> {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "이용 가능한 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                        });
                    });
                    Network.socket.on("startNavigation", args -> {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            startActivity(intent);
                        });
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
