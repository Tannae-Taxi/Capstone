package com.example.tannae.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class AccountActivity extends AppCompatActivity {
    private Button btnEdit;
    private Button btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setViews();
        setEventListeners();
    }
    private void setViews(){
        btnEdit = findViewById(R.id.btn_edit);
        btnSignOut = findViewById(R.id.btn_sign_out);
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
