package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;

import org.json.JSONObject;

// << Navigation Activity >>
public class NavigationActivity extends AppCompatActivity {
    private Button btnEndService, btnPass;
    private Switch switchDrive;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // Setting
        setViews();
        setEventListeners();
        setNetworks();
        switchDrive.setChecked(true);
        // If main -> navigation ? true : false
        boolean type = getIntent().getBooleanExtra("type", false);
        if (type) {
            JSONObject user = new JSONObject();
            Network.socket.emit("serviceOn");   ///////////////////////////////////////////////////// User 정보를 JSONObject 형태로 전송
        }
        // If not driver than set driver views invisible
        else {
            btnPass.setVisibility(View.INVISIBLE);
            btnEndService.setVisibility(View.INVISIBLE);
            switchDrive.setVisibility(View.INVISIBLE);
        }
    }

    // < Set Socket.io >
    private void setNetworks() {
        Network.socket.on("responseService", args -> {
            boolean flag = (boolean) args[0];
            if (flag) {
                JSONObject path = (JSONObject) args[1];
                ////////////////////////////////////////////////////////////// path 정보를 바탕으로 navigation 화면 수정
            } else {
                Toast.makeText(getApplicationContext(), "이용 가능한 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    // < Register views >
    private void setViews() {
        btnPass = findViewById(R.id.btn_pass_navigation);
        btnEndService = findViewById(R.id.btn_end_service_navigation);
        switchDrive = findViewById(R.id.switch_drive_state_navigation);
    }

    // < Register event listeners >
    private void setEventListeners() {
        // Pass next waypoint [SOCKET]
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject user = new JSONObject();
                /////////////////////////////////////////////////////// user에 운전자의 user 정보 삽입
                Network.socket.emit("passWaypoint", user);
            }
        });
        // End service [SOCKET]
        btnEndService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network.socket.emit("serviceEnd"); ///////////////////////////////////////////////// User 정보를 JSONObject 형태로 전송
                Toast.makeText(getApplicationContext(), "운행이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        // Change service availability [SOCKET]
        switchDrive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONObject user = new JSONObject();     /////////////////////////////////////////////////////////// User 정보를 json 형태로 저장
                Network.socket.emit("changeServiceState", user, isChecked);
            }
        });
    }
}
