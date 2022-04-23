package com.example.tannae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.tannae.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

public class FindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        AppBarLayout appBarLayout3 = findViewById(R.id.appBarLayout3);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);

        TextView tvPersonal = findViewById(R.id.tv_personal);
        EditText etName = findViewById(R.id.et_name);
        EditText etBirth = findViewById(R.id.et_birth);
        RadioButton man = findViewById(R.id.man);
        RadioButton woman = findViewById(R.id.woman);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etPhoneNumber = findViewById(R.id.et_phone);
        Button btnFindAccount= findViewById(R.id.btn_findaccount);
        EditText etPinNumber = findViewById(R.id.et_pinnumber);
        Button btnCertificate = findViewById(R.id.btn_Certificate);
        TextView tvMyId = findViewById(R.id.tv_myid);
        TextView tvMyPw = findViewById(R.id.tv_mypw);

    }
}