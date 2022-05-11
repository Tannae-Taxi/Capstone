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
import com.example.tannae.sub.User;

import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

// << Navigation Activity >>
public class NavigationActivity extends AppCompatActivity {
    private Button btnEndService, btnPass;
    private Switch switchDrive;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private boolean type;

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
        type = getIntent().getBooleanExtra("type", false);
        if (!type) {
            btnPass.setVisibility(View.INVISIBLE);
            btnEndService.setVisibility(View.INVISIBLE);
            switchDrive.setVisibility(View.INVISIBLE);
            //////////////////////////// 내부 state 가 1이면 (차를 타고 있는데 Navigation 화면에서 나갔다가 다시 들어온 경우이면) 내부에 저장된 path 를 바탕으로 MapView 다시 그리기
        }
    }

    // < onResume >
    @Override
    protected void onResume() {
        super.onResume();
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view_navigation);
        mapViewContainer.addView(mapView);
    }

    // < onPause >
    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }

    // < Set Socket.io >
    private void setNetworks() {
        // Response service
        Network.socket.on("responseService", args -> {
            // args[0] = flag:int / args[1] = path:JSONObject / args[2] = usn:String
            runOnUiThread(() -> {
                // Flag : -1 (Server Error) / 0 (No Vehicle) / 1 (Share vehicle) / 2 (Non-share vehicle to share user) / 3 (Non-share vehicle to non-share user)
                // 4 (Passenger boarding) / 5 (Passenger get off)
                int flag = (int) args[0];
                JSONObject path = (JSONObject) args[1];
                String usnOut = (String) args[2];
                String usnIn = User.sp.getString("usn", "");

                // Update inner DB

                // Toast
                String message;
                String add = "추가 인원이 배차되었습니다.\n경로를 수정합니다.";
                switch (flag) {
                    case 0 : message = "이용 가능한 차량이 없습니다."; break;
                    case 1 : message = !usnOut.equals(usnIn) ? add : "동승 차량이 배차되었습니다."; break;
                    case 2 : message = !usnOut.equals(usnIn) ? add : "동승 가능한 차량이 없습니다.\n일반 차량이 배차되었습니다."; break;
                    case 3 : message = !usnOut.equals(usnIn) ? add : "일반 차량이 배차되었습니다."; break;
                    case 4 : message = usnOut.equals(usnIn) ? "차량이 도착하였습니다.\n탑승해 주시기 바랍니다." : "탑승자가 승차하였습니다.\n경로를 수정합니다."; break;
                    case 5 : message = usnOut.equals(usnOut) ? "목적지에 도착하였습니다.\n하차해 주시기 바랍니다." : "탑승자가 하차하였습니다.\n경로를 수정합니다."; break;
                    default: message = "배차 오류가 발생하였습니다.\n고객센터에 문의하세요."; break;
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // Event handle by flag number
                if (!(flag == -1 || flag == 0)) {
                    //////////////////////////////////////// path 정보를 문자열로 변환하여 내부 DB의 path 속성에 저장
                    if (flag == 1 || flag == 2 || flag == 3) {
                        //////////////////////// 외부 usn(usnOut) 과 내부 usn(usnIn) 을 비교하여 일치하면 내부 state 을 1로 설정
                    } else if (flag == 5){
                        //////////////////////// 외부 usn(usnOut) 과 내부 usn(usnIn) 을 비교하여 일치하면 내부 state 을 0으로 설정
                        //////////////////////// 외부 usn(usnOut) 과 내부 usn(usnIn) 을 비교하여 일치하면 내부 path 정보를 null 로 변경
                        //////////////////////// 외부 usn(usnOut) 과 내부 usn(usnIn) 이 일치하고 현재 화면이 navigation 이면 Main 화면으로 복귀
                    }
                    //////////////////////// DH : 가져온 path 정보를 바탕으로 navigation mapview 표시
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        });

        // End service
        Network.socket.on("serviceEnd", args -> {
            runOnUiThread(() -> {
                JSONObject receipt = (JSONObject) args[0];
                ///////////////////////////////// DH : receipt 는 메모장에서 [ Result of service end ]  형태이며 이를 바탕으로 영수증 화면 구성
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
                try {
                    JSONObject driver = new JSONObject();
                    User.setUserInTOOut(driver);
                    Network.socket.emit("passWaypoint", driver);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // End service [SOCKET]
        btnEndService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject driver = new JSONObject();
                    User.setUserInTOOut(driver);
                    Network.socket.emit("serviceEnd", driver);
                    Toast.makeText(getApplicationContext(), "운행이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                    /////////////////////////////////////////// MapView clear
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Change service availability [SOCKET]
        switchDrive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONObject driver = new JSONObject();
                try {
                    User.setUserInTOOut(driver);
                    driver.put("service", isChecked);
                    Network.socket.emit(isChecked ? "serviceOn" : "serviceOff", driver);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (type)
            Toast.makeText(getApplicationContext(), "운행중에는 내비게이션을 종료할 수 없습니다.", Toast.LENGTH_SHORT).show(); // 내비 종료 불가
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
