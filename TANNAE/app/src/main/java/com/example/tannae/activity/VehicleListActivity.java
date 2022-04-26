package com.example.tannae.activity;

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

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
