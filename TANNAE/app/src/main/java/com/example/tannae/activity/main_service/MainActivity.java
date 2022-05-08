package com.example.tannae.activity.main_service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// << Main Activity >>
public class MainActivity extends AppCompatActivity {
    private Button btnDrive;
    private FloatingActionButton reqBtn;
    private long backKeyPressedTime = 0;
    private Toolbar toolbar;

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
        Network.socket.connect();
    }

    // < Register views >
    private void setViews() {
        btnDrive = findViewById(R.id.btn_drive_main);
        reqBtn = findViewById(R.id.req_button);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
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
                Intent intent = new Intent(getApplicationContext(), ServiceReqActivity.class);
                startActivity(intent);
            }
        });
    }

    // < BackPress >
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "종료하려면 한번 더 누르세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            Network.socket.disconnect();
            finish();
        }
    }
}