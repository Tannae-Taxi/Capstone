package com.example.tannae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class ServiceUsingActivity extends AppCompatActivity {
    private ImageButton ibMap;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceusing);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        ibMap = findViewById(R.id.ib_map_serviceusing);
        btnBack = findViewById(R.id.btn_back_serviceusing);
    }
    private void setEventListeners() {
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
