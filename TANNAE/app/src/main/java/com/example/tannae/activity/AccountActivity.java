package com.example.tannae.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class AccountActivity extends AppCompatActivity {
    private Button btnEdit;
    private Button btnSignOut;
    private TextView tvID, tvPW, tvGender, tvUname, tvBirth, tvEmail, tvPhone;
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
        tvBirth = findViewById(R.id.tv_birth_account);
        tvEmail = findViewById(R.id.tv_email_account);
        tvPhone = findViewById(R.id.tv_phone_account);
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
    }
}
