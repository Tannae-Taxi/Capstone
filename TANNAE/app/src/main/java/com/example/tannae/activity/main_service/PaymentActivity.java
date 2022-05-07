package com.example.tannae.activity.main_service;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tannae.R;

public class PaymentActivity extends AppCompatActivity {
    private Button btnBack, btnSend;
    private RatingBar rbDriverRating;
    private RecyclerView rvReceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        btnBack = findViewById(R.id.btn_back_payment);
        btnSend = findViewById(R.id.btn_send_payment);
        rbDriverRating = findViewById(R.id.rb_driverrating_payment);
        rvReceipt = findViewById(R.id.rv_receipt_payment);
    }

    private void setEventListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rbDriverRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        // rvReceipt는 RecyclerView 객체의 이벤트 처리 방법 찾아본 후 추가할 예정
    }
}
