package com.example.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.user_service.UserServiceListActivity;

public class AccountEditActivity extends AppCompatActivity {
    private EditText etID, etPW,etCheckPW, etEmail, etPhone;
    private TextView tvCheckID, tvCheckPW;
    private Button btnCheckID, btnEdit;
    private Toolbar toolbar;
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
        toolbar = findViewById(R.id.topAppBar_accountedit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setEventListeners(){

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
