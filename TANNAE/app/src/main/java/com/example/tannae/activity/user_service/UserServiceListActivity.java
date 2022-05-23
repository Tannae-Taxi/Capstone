package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.activity.account.AccountActivity;
import com.example.tannae.activity.account.LoginActivity;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

public class UserServiceListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userservicelist);
        setViews();
    }

    private void setViews() {
        findViewById(R.id.layout_account_userservicelist).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AccountActivity.class)));
        findViewById(R.id.layout_point_userservicelist).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PointActivity.class)));
        findViewById(R.id.layout_qna_userservicelist).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), QnAActivity.class)));
        findViewById(R.id.layout_history_userservicelist).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), HistoryActivity.class)));
        (findViewById(R.id.layout_lost_found_userservicelist)).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LostFoundActivity.class)));
        (findViewById(R.id.layout_faq_userservicelist)).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FAQActivity.class)));
        (findViewById(R.id.btn_logout_userservicelist)).setOnClickListener(v -> {
            InnerDB.sp.edit().clear().apply();
            Toaster.show(getApplicationContext(), "안전하게 로그아웃 되었습니다.");
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}