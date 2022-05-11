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
            // args[0] : flag / args[1] : path / args[2] : usn
            runOnUiThread(() -> {
                // Flag : -1 (Server Error) / 0 (No Vehicle) / 1 (Share vehicle) / 2 (Non-share vehicle to share user) / 3 (Non-share vehicle to non-share user)
                // 4 (Passenger boarding) / 5 (Passenger get off)
                int flag = (int) args[0];
                // Set message
                String message;
                switch (flag) {
                    case 0 : message = "이용 가능한 차량이 없습니다."; break;
                    case 1 : message = "동승 가능한 차량이 배차되었습니다."; break;
                    case 2 : message = "동승 가능한 차량이 없습니다.\n일반 차량이 배차되었습니다."; break;
                    case 3 : message = "일반 차량이 배차되었습니다."; break;
                    case 4 : message = "탑승자가 승차하였습니다.\n경로를 수정합니다."; break;
                    case 5 : message = "탑승자가 하차하였습니다.\n경로를 수정합니다."; break;
                    default: message = "배차 오류가 발생하였습니다.\n고객센터에 문의하세요."; break;
                }

                // Show message
                if (flag == 1 || flag == 2 || flag == 3)
                    if (((String)args[2]).equals(User.sp.getString("usn", "")))
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "추가 인원이 배차 되었습니다.\n경로를 수정합니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                ////////////////////////////////////// 밑에는 flag에 따른 처리 아직 안함
                // Event handle by flag number
                if (!(flag == -1 || flag == 0)) {
                    JSONObject path = (JSONObject) args[1];
                    ////////////////////////////////////////////////////////////// path 정보를 바탕으로 navigation 화면 수정
                    /////////////////////////////////////// state 값을 1로 변경하기 (다만, 운전자의 state는 미변경)
                    ///////////////////////// flag : int args[0] / path : JSONObject args[1] / usn : String args[2]
                    ///////////////////////// usn은 현재 경유지 탑승자의 usn : 내부 db의 usn과 일치하며 flag가 5이면 내부 db user의 state를 false(0)로 변경
                    // flag -1 0은 flag이외의 arg가 없으며 1 2 3은 path arg가 추가되고 4 5는 usn이 추가
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        });
        Network.socket.on("serviceEnd", args -> {
            runOnUiThread(() -> {
                JSONObject receipt = (JSONObject) args[0];

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
                    user.put("service", isChecked);
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
        if (type) // 내비게이션 화면에 있는데 운전자(state값이 0)이며 운행중(drive 값이 1)상태인경우에는
            Toast.makeText(getApplicationContext(), "운행중에는 내비게이션을 종료할 수 없습니다.", Toast.LENGTH_SHORT).show(); // 내비 종료 불가
        else {
            /////////////////////////// Main 화면으로 복귀
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent); // 탑승자인 경우 Main 화면으로 복귀하는 코드. FLAG_ACTIVITY_CLEAR_TOP을 사용하였기 때문에 내비게이션 화면 정보를 유지해주어야 함
        }
    }
}
