package com.example.tannae.activity.user_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.account.LoginActivity;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

public class PointActivity extends AppCompatActivity {
    private TextView tvPoint;
    private EditText etCharge;
    private Button btnCharge;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        setViews();
        tvPoint.setText(InnerDB.sp.getInt("points", 0) + "원");
        setEventListeners();
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setViews() {
        tvPoint = findViewById(R.id.tv_point_point);
        etCharge = findViewById(R.id.et_charge_point);
        btnCharge = findViewById(R.id.btn_charge_point);
        toolbar = findViewById(R.id.topAppBar_point);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setEventListeners() {
        etCharge.addTextChangedListener(new TextWatcher() {
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
        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String charge = etCharge.getText().toString();
                if(charge.matches("[0-9]+")) {
                    int currentPoint = InnerDB.sp.getInt("points", 0);
                    currentPoint += Integer.parseInt(etCharge.getText().toString());
                    etCharge.setText("");
                    InnerDB.editor.putInt("points", currentPoint).apply();
                    tvPoint.setText(InnerDB.sp.getInt("points", 0) + "원");
                    Toaster.show(getApplicationContext(), "포인트가 충전되었습니다.");
                }
                else
                    Toaster.show(getApplicationContext(), "충전 금액은 숫자만 입력해주세요.");
            }
        });
    }
}
