package com.example.tannae.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class ServiceReqActivity extends AppCompatActivity {
    private ImageButton ibMap;
    private EditText etOrigin, etDesti;
    private Button btnNext, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicereq);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        ibMap = findViewById(R.id.ib_map_servicereq);
        etOrigin = findViewById(R.id.et_origin_servicereq);
        etDesti = findViewById(R.id.et_Desti_servicereq);
        btnNext = findViewById(R.id.btn_next_detailservicereq);
        btnBack = findViewById(R.id.btn_back_detailservicereq);
    }

    private void setEventListeners() {
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        etOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etDesti.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
