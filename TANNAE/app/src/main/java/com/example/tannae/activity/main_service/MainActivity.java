package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.account.LoginActivity;
import com.example.tannae.activity.user_service.UserServiceListActivity;
import com.example.tannae.network.Network;
import com.example.tannae.sub.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

// << Main Activity >>
public class MainActivity extends AppCompatActivity {
    private Button btnDrive;
    private FloatingActionButton reqBtn;
    private long backKeyPressedTime = 0;
    private Toolbar toolbar;
    private MapView mapView;
    private ViewGroup mapViewContainer;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setting
        setViews();
        setEventListeners();
        // Connect Socket.io
        if (!Network.socket.isActive())
            Network.socket.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view_main);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord( 37.566406178655534, 126.97786868931414), true);
        mapViewContainer.addView(mapView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }

    // < Register views >
    private void setViews() {
        btnDrive = findViewById(R.id.btn_drive_main);
        reqBtn = findViewById(R.id.req_button_main);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
                intent.putExtra("type", false);
                startActivity(intent);
            }
        });
    }

    // < Register event listeners >
    private void setEventListeners() {
        // Service on
        btnDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });
        // Request service
        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.sp.getInt("state", 0) == 1) {
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.putExtra("type", false);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ServiceReqActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    // < BackPress >
    public void onBackPressed() {
        //////////////////////////////////////////////////////////////// 뒤로가기 두 번 하면 Logout 되면서 LoginActivity 로 전환되고 내부 DB clear
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "로그아웃하려면 한번 더 누르세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) { // 뒤로가기 두 번 하면
            User.sp.edit().clear().apply(); // 내부 DB clear

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class); //LoginActivity 로 전환 //// 근데 로그아웃 기능 따로 구현 안하고 이 방식으로 할 것인지 궁금
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            /* Network.socket.disconnect();
            finish();*/
        }
    }
}