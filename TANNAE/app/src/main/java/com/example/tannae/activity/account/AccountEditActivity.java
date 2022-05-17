package com.example.tannae.activity.account;

import android.os.Bundle;
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
        etID = findViewById(R.id.et_id_account_edit);
        etPW = findViewById(R.id.et_pw_account_edit);
        etCheckPW = findViewById(R.id.et_checkpw_account_edit);
        etEmail = findViewById(R.id.et_email_account_edit);
        etPhone = findViewById(R.id.et_phone_account_edit);
        tvCheckID = findViewById(R.id.tv_checkid_account_edit); // 아이디가 2개로 뜸
        btnEdit = findViewById(R.id.btn_edit_account_edit);
        tvCheckPW = findViewById(R.id.tv_retrypw_account_edit);
        btnCheckID = findViewById(R.id.btn_checkid_account_edit); // 아이디가 2개로 뜸
        btnEdit = findViewById(R.id.btn_edit_account_edit);
    }
    private void setEventListeners(){
    }
}
