package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.activity.account.AccountActivity;
import com.example.tannae.activity.account.LoginActivity;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.activity.main_service.PaymentActivity;
import com.example.tannae.sub.InnerDB;

public class UserServiceListActivity extends AppCompatActivity {
    private LinearLayout layoutAccount;
    private Button btnPoint, btnQnA, btnHistory, btnLostFound, btnFAQ, btnPayment;
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
        btnPoint = findViewById(R.id.btn_point_userservicelist);
        btnQnA = findViewById(R.id.btn_qna_userservicelist);
        btnHistory = findViewById(R.id.btn_history_userservicelist);
        btnLostFound = findViewById(R.id.btn_lost_found_userservicelist);
        btnFAQ = findViewById(R.id.btn_faq_userservicelist);
        btnPayment = findViewById(R.id.btn_payment_userservicelist);
        btnLogout = findViewById(R.id.btn_logout_userservicelist);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setEventListeners() {

        layoutAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        // Service on

        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnQnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QnAActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnLostFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LostFoundActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FAQActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InnerDB.sp.edit().clear().apply(); // 내부 DB clear
                Toast.makeText(UserServiceListActivity.this, "안전하게 로그아웃 되었습니다다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class); //LoginActivity 로 전환 //// 근데 로그아웃 기능 따로 구현 안하고이 방식으로 할 것인지 궁금
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); // 로그아웃 기능 구현
            }
        });
    }
}