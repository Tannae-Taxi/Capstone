package com.team_seven.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.team_seven.tannae.R;
import com.team_seven.tannae.network.Network;
import com.team_seven.tannae.sub.InnerDB;
import com.team_seven.tannae.sub.Toaster;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// << AccountActivity >>
public class AccountActivity extends AppCompatActivity {
    private Button btnSignOut;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        btnSignOut = findViewById(R.id.btn_sign_out_account);
        findViewById(R.id.btn_edit_account).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AccountEditActivity.class)));

        ((TextView) findViewById(R.id.tv_id_account)).setText("ID : " + InnerDB.sp.getString("id", null));
        ((TextView) findViewById(R.id.tv_pw_account)).setText("PW : " + InnerDB.sp.getString("pw", null));
        ((TextView) findViewById(R.id.tv_gender_account)).setText("성별 : " + (InnerDB.sp.getInt("gender", 0) == 1 ? "남자" : "여자"));
        ((TextView) findViewById(R.id.tv_uname_account)).setText("이름 : " + InnerDB.sp.getString("uname", null));
        ((TextView) findViewById(R.id.tv_rrn_account)).setText("주민등록번호 : " + InnerDB.sp.getString("rrn", null));
        ((TextView) findViewById(R.id.tv_email_account)).setText("E-mail : " + InnerDB.sp.getString("email", null));
        ((TextView) findViewById(R.id.tv_phone_account)).setText("연락처 : " + InnerDB.sp.getString("phone", null));

        setSupportActionBar(toolbar = findViewById(R.id.topAppBar_account));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("회원 정보");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setEventListeners() {
        btnSignOut.setOnClickListener(v -> {
            Network.service.signout(InnerDB.getUser()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Toaster.show(getApplicationContext(), "TANNAE를 이용해주셔서 감사합니다.");
                    InnerDB.editor.clear().apply();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toaster.show(getApplicationContext(), "Error");
                    Log.e("Error", t.getMessage());
                }
            });
        });
    }
}
