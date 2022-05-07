package com.example.tannae.activity.user_service;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

    }

    public static class HistoryActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);

        }
    }
}
