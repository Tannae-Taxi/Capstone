package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

import org.json.JSONException;

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
        tvPoint.setText(InnerDB.sp.getInt("points", 0) + "원");
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
        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int charge = Integer.parseInt(etCharge.getText().toString());
                final int currentPoint = InnerDB.sp.getInt("points", 0) + charge;
                InnerDB.editor.putInt("points", currentPoint).apply();

                try {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
