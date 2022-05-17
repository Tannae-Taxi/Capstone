package com.example.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.activity.user_service.UserServiceListActivity;

public class AccountActivity extends AppCompatActivity {
    private Button btnEdit;
    private Button btnSignOut;
    private TextView tvID, tvPW, tvGender, tvUname, tvRrn, tvEmail, tvPhone;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setViews();
        setEventListeners();
    }

    private void setViews(){
        btnEdit = findViewById(R.id.btn_edit_account);
        btnSignOut = findViewById(R.id.btn_sign_out_account);
        tvID = findViewById(R.id.tv_id_account);
        tvPW = findViewById(R.id.tv_pw_account);
        tvGender = findViewById(R.id.tv_gender_account);
        tvUname = findViewById(R.id.tv_uname_account);
        tvRrn = findViewById(R.id.tv_rrn_account);
        tvEmail = findViewById(R.id.tv_email_account);
        tvPhone = findViewById(R.id.tv_phone_account);
        toolbar = findViewById(R.id.topAppBar_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setEventListeners(){
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
