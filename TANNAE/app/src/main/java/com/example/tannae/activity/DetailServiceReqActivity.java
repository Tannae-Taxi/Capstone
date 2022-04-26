package com.example.tannae.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class DetailServiceReqActivity extends AppCompatActivity {
    private Button btnBack, btnEnd;
    private CheckedTextView ctvDetaila, ctvDetailb, ctvDetailc;
    private EditText etDetailReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailservicereq);
        setViews();
        setEventListeners();
    }
    private void setViews(){
        btnBack = findViewById(R.id.btn_back_detailservicereq);
        btnEnd = findViewById(R.id.btn_next_detailservicereq);
        ctvDetaila = findViewById(R.id.ctv_detaila_detailservicereq);
        ctvDetailb = findViewById(R.id.ctv_detailb_detailservicereq);
        ctvDetailc = findViewById(R.id.ctv_detailc_detailservicereq);
        etDetailReq = findViewById(R.id.et_detailreq_detailservicereq);
    }
    private void setEventListeners(){
    }
}
