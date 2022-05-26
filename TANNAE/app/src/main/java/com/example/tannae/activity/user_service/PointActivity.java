package com.example.tannae.activity.user_service;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        setEventListeners();
    }

    private void setViews() {
        etCharge = findViewById(R.id.et_charge_point);
        btnCharge = findViewById(R.id.btn_charge_point);
        (tvPoint = findViewById(R.id.tv_point_point)).setText(InnerDB.sp.getInt("points", 0) + "원");
        setSupportActionBar(toolbar = findViewById(R.id.topAppBar_point));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("포인트");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setEventListeners() {
        btnCharge.setOnClickListener(v -> {
            String chargeStr = etCharge.getText().toString();
            if (chargeStr.length() == 0)
                Toaster.show(getApplicationContext(), "충전 금액을 입력해주세요.");
            else {
                int charge = Integer.parseInt(etCharge.getText().toString());
                final int currentPoint = InnerDB.sp.getInt("points", 0) + charge;
                InnerDB.editor.putInt("points", currentPoint).apply();

                Network.service.charge(InnerDB.getUser()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        etCharge.setText("");
                        tvPoint.setText(InnerDB.sp.getInt("points", 0) + "원");
                        Toaster.show(getApplicationContext(), "포인트가 충전되었습니다.");
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        InnerDB.editor.putInt("points", currentPoint - charge).apply();
                        Toaster.show(getApplicationContext(), "Error");
                        Log.e("Error", t.getMessage());
                    }
                });
            }
        });
    }
}
