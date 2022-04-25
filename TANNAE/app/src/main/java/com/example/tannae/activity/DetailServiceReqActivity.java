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
        btnBack = findViewById(R.id.btn_back);
        btnEnd = findViewById(R.id.btn_end);
        ctvDetaila = findViewById(R.id.ctv_detaila);
        ctvDetailb = findViewById(R.id.ctv_detailb);
        ctvDetailc = findViewById(R.id.ctv_detailc);
        etDetailReq = findViewById(R.id.et_detailreq);
    }
    private void setEventListeners(){
    }
}
