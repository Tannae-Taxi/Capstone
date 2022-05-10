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

import org.json.JSONException;
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
    private void setNetworks() { /////////////////////////////////////// 탑승자 유저가 배차가 성공하였을 경우 state 값을 1로 변경하기
        Network.socket.on("responseService", args -> {
            runOnUiThread(() -> {
                int flag = (int) args[0];
                String message = flag == -1 ? "배차 오류가 발생하였습니다.\n고객센터에 문의하세요." : (flag == 0 ? "이용 가능한 차량이 없습니다." :
                        (flag == 1 ? "동승 가능한 차량이 배차되었습니다." : (flag == 2 ? "동승 가능한 차량이 없습니다.\n일반 차량이 배차되었습니다." : "일반 차량이 배차되었습니다.")));
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (flag == 1 || flag == 2 || flag == 3) {
                    JSONObject path = (JSONObject) args[1];
                    ////////////////////////////////////////////////////////////// path 정보를 바탕으로 navigation 화면 수정
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
                /* try {
                    User.setUserInTOOut(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                } */ // user에 운전자의 user 정보를 삽입하는 코드. 잘 한건지 몰라서 일단 주석 처리해둠 SC

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
        switchDrive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  /////////////// 운전자 유저의 drive 값을 변경해주기
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONObject user = new JSONObject();

                ///////////////////////////////////////////////// user에 운전자의 user 정보 삽입
                /* try {
                    User.setUserInTOOut(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                } */ // user에 운전자의 user 정보를 삽입하는 코드. 잘 한건지 몰라서 일단 주석 처리해둠 SC

                Network.socket.emit(isChecked ? "serviceOn" : "serviceOff", user);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /////////////////////////////// Navigation 화면에 있는데 state 가 0인 사람은 운전자이고 1인 사람은 탑승자임을 알 수 있슴.
        /////////////////////////////// 운전자는 안전상의 이유로 운행 중일 때는 Navigation 종료 불가
        /////////////////////////////// 탑승자는 Main 화면으로 복귀
        if(User.sp.getInt("state", 0) == 0 && User.sp.getInt("drive",0) == 1) // 내비게이션 화면에 있는데 운전자(state값이 0)이며 운행중(drive 값이 1)상태인경우에는
            Toast.makeText(getApplicationContext(), "운행중에는 내비게이션을 종료할 수 없습니다.", Toast.LENGTH_SHORT).show(); // 내비 종료 불가
        else {
            /////////////////////////// Main 화면으로 복귀
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent); // 탑승자인 경우 Main 화면으로 복귀하는 코드. FLAG_ACTIVITY_CLEAR_TOP을 사용하였기 때문에 내비게이션 화면 정보를 유지해주어야 함
        }
    }
}
