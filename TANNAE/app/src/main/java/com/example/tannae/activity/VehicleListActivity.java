package com.example.tannae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class VehicleListActivity extends AppCompatActivity {
    private Button btnBack, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiclelist);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        btnBack = findViewById(R.id.btn_back_vehiclelist);
        btnNext = findViewById(R.id.btn_next_vehiclelist);
    }

    private void setEventListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ServiceReqActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ServiceUsingActivity.class);
                startActivity(intent);
            }
        });
    }
}
