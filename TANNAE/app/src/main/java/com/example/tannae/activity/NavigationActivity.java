package com.example.tannae.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class NavigationActivity extends AppCompatActivity {
    private Button btnBack, btnNext;
    private ImageButton ibMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        btnBack = findViewById(R.id.btn_back_navigation);
        btnNext = findViewById(R.id.btn_next_navigation);
        ibMap = findViewById(R.id.ib_map_navigation);
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
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
