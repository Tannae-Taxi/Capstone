package com.example.tannae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tannae.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

public class FindActivity extends AppCompatActivity {
    private TextView tvPersonal, tvMyId, tvMyPw;
    private EditText etName, etRRN, etEmail, etPhoneNumber, etPinNumber;
    private RadioGroup rgSex;
    private Button btnFindAccount, btnCertificate;

    private boolean checkedSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        setViews();
        setEventListeners();
    }
    private void setViews(){
        tvPersonal = findViewById(R.id.tv_personal);
        etName = findViewById(R.id.et_name);
        etRRN = findViewById(R.id.et_rrn);
        rgSex = findViewById(R.id.rg_sex);
        etEmail = findViewById(R.id.et_email);
        etPhoneNumber = findViewById(R.id.et_phone);
        btnFindAccount= findViewById(R.id.btn_findaccount);
        etPinNumber = findViewById(R.id.et_pinnumber);
        btnCertificate = findViewById(R.id.btn_Certificate);
        tvMyId = findViewById(R.id.tv_myid);
        tvMyPw = findViewById(R.id.tv_mypw);
    }

    private void setEventListeners(){
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedSex = (checkedId == R.id.rb_man) ? true : false;
            }
        });
        etRRN.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String rrn = etRRN.getText().toString();
                if(rrn.length() == 7 && rrn.charAt(rrn.length() - 1) != '-' ) {
                    etRRN.setText(new StringBuffer(rrn).insert(6, '-').toString());
                    etRRN.setSelection(rrn.length() + 1);
                }
                return false;
            }
        });
    }

}