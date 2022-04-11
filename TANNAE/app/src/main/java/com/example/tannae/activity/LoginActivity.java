package com.example.tannae.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tannae.R;
import com.example.tannae.data.request.LoginRequest;
import com.example.tannae.data.request.LoginResponse;
import com.example.tannae.network.RetrofitClient;
import com.example.tannae.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etID;
    private EditText etPW;
    private Button btnLogin;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        etID = findViewById(R.id.et_id);
        etPW = findViewById(R.id.et_pw);
        btnLogin = findViewById(R.id.btn_login);
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    private void setEventListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etID.getText().toString();
                String pw = etPW.getText().toString();
                service.userLogin(new LoginRequest(id, pw)).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse res = response.body();
                        int flag = res.getFlag();
                        String message = res.getMessage();
                        if (flag == 1) {
                            etID.setText("");
                            etPW.setText("");
                        } else if (flag == 2)
                            etPW.setText("");
                        else if(flag == 3) {
                            // Change screen to MainActivity
                        }
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                        Log.e("로그인 에러 발생", t.getMessage());
                    }
                });
            }
        });
    }
}