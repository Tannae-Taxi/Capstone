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

public class UserServiceListActivity extends AppCompatActivity {

    private Button btnAccount, btnPoint, btnQnA, btnLostFound, btnFAQ;
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
        btnLostFound = findViewById(R.id.btn_lost_found_userservicelist);
        btnFAQ = findViewById(R.id.btn_faq_userservicelist);
        toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("type", false);
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
    }
}