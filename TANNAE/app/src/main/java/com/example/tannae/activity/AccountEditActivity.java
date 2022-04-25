package com.example.tannae.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class AccountEditActivity extends AppCompatActivity {
    private EditText etID, etPW,etCheckPW, etEmail, etPhone;
    private TextView tvCheckID, tvCheckPW;
    private Button btnCheckID, btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        setViews();
        setEventListeners();
    }
    private void setViews(){
        etID = findViewById(R.id.et_id);
        etPW = findViewById(R.id.et_pw);
        etCheckPW = findViewById(R.id.et_checkpw);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        tvCheckID = findViewById(R.id.tv_checkid); // 아이디가 2개로 뜸
        btnEdit = findViewById(R.id.btn_edit);
        tvCheckPW = findViewById(R.id.tv_retrypw);
        btnCheckID = findViewById(R.id.btn_checkid); // 아이디가 2개로 뜸
        btnEdit = findViewById(R.id.btn_edit);
    }
    private void setEventListeners(){
    }
}
