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
import com.example.tannae.sub.InnerDB;

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
        tvID.setText("아이디: " + InnerDB.sp.getString("id", null));

        tvPW = findViewById(R.id.tv_pw_account);
        tvPW.setText("비밀번호: " + InnerDB.sp.getString("pw", null));

        tvGender = findViewById(R.id.tv_gender_account);
        String gender = InnerDB.sp.getInt("gender", 0) == 1? "남" : "여";
        tvGender.setText("성별: " + gender);

        tvUname = findViewById(R.id.tv_uname_account);
        tvUname.setText("이름: " + InnerDB.sp.getString("uname", null));

        tvRrn = findViewById(R.id.tv_rrn_account);
        tvRrn.setText("주민등록번호: " + InnerDB.sp.getString("rrn", null));

        tvEmail = findViewById(R.id.tv_email_account);
        tvEmail.setText("이메일: " + InnerDB.sp.getString("email", null));

        tvPhone = findViewById(R.id.tv_phone_account);
        tvPhone.setText("연락처: " + InnerDB.sp.getString("phone", null));


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
                Intent intent = new Intent(getApplicationContext(), AccountEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
