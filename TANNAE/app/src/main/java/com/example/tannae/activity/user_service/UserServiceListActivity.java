package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.account.AccountActivity;
import com.example.tannae.activity.account.LoginActivity;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.activity.main_service.NavigationActivity;
import com.example.tannae.activity.main_service.PaymentActivity;

public class UserServiceListActivity extends AppCompatActivity {

    private Button btnAccount, btnPoint, btnQnA, btnHistory, btnLostFound, btnFAQ;
    private Button btnPayment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userservicelist);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        btnAccount = findViewById(R.id.btn_account_userservicelist);
        btnPoint = findViewById(R.id.btn_point_userservicelist);
        btnQnA = findViewById(R.id.btn_qna_userservicelist);
        btnHistory = findViewById(R.id.btn_history_userservicelist);
        btnLostFound = findViewById(R.id.btn_lost_found_userservicelist);
        btnFAQ = findViewById(R.id.btn_faq_userservicelist);
        btnPayment = findViewById(R.id.btn_payment_userservicelist);
        toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("type", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setEventListeners() {
        // Service on
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });

        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });

        btnQnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QnAActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });

        btnLostFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LostFoundActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FAQActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });
    }
}