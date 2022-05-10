package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.user.User;

import net.daum.mf.map.api.MapView;

import org.json.JSONObject;

// << Navigation Activity >>
public class NavigationActivity extends AppCompatActivity {
    private Button btnEndService, btnPass;
    private Switch switchDrive;
    private MapView mapView;
    private ViewGroup mapViewContainer;

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
        // If main -> navigation ? true : false
        boolean type = getIntent().getBooleanExtra("type", false);
        if (!type) {
            btnPass.setVisibility(View.INVISIBLE);
            btnEndService.setVisibility(View.INVISIBLE);
            switchDrive.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view_navigation);
        mapViewContainer.addView(mapView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }

    // < Set Socket.io >
    private void setNetworks() {
        Network.socket.on("responseService", args -> {
            runOnUiThread(() -> {
                int flag = (int) args[0];
                if (flag == 1) {
                    Toast.makeText(getApplicationContext(), "배차가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    JSONObject path = (JSONObject) args[1];
                    
                    ////////////////////////////////////////////////////////////// path 정보를 바탕으로 navigation 화면 수정
                } else if (flag == 0){
                    Toast.makeText(getApplicationContext(), "이용 가능한 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "배차 오류가 발생하였습니다.\n고객센터에 문의하세요.", Toast.LENGTH_SHORT).show();
                }
            });

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
            }
        });
        // Change service availability [SOCKET]
        switchDrive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONObject user = new JSONObject();
                ///////////////////////////////////////////////// user에 운전자의 user 정보 삽입
                Network.socket.emit(isChecked ? "serviceOn" : "serviceOff", user);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /////////////////////////////// Navigation 화면에 있는데 state 가 0인 사람은 운전자이고 1인 사람은 탑승자임을 알 수 있슴.
        /////////////////////////////// 운전자는 안전상의 이유로 운행 중일 때는 Navigation 종료 불가
        /////////////////////////////// 사용자는 Main 화면으로 복귀
        if(User.sp.getInt("state", 0) == 0)
            Toast.makeText(getApplicationContext(), "운행중에는 내비게이션을 종료할 수 없습니다.", Toast.LENGTH_SHORT).show();
        else {
            /////////////////////////// Main 화면으로 복귀
        }
    }
}
