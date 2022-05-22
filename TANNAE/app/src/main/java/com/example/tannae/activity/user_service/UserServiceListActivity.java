package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.activity.account.AccountActivity;
import com.example.tannae.activity.account.LoginActivity;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

public class UserServiceListActivity extends AppCompatActivity {
    private LinearLayout layoutAccount, layoutPoint, layoutQnA, layoutHistory, layoutLostFound, layoutFAQ;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userservicelist);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        layoutAccount = (LinearLayout) findViewById(R.id.layout_account_userservicelist);
        layoutPoint = (LinearLayout) findViewById(R.id.layout_point_userservicelist);
        layoutQnA = (LinearLayout) findViewById(R.id.layout_qna_userservicelist);
        layoutHistory = (LinearLayout) findViewById(R.id.layout_history_userservicelist);
        layoutLostFound = findViewById(R.id.layout_lost_found_userservicelist);
        layoutFAQ = findViewById(R.id.layout_faq_userservicelist);
        btnLogout = findViewById(R.id.btn_logout_userservicelist);
    }

    private void setEventListeners() {
        layoutAccount.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AccountActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        layoutPoint.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PointActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        layoutQnA.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), QnAActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        layoutHistory.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        layoutLostFound.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LostFoundActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        layoutFAQ.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FAQActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        btnLogout.setOnClickListener(v -> {
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