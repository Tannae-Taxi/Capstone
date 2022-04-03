package com.example.tannae.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tannae.R;
import com.example.tannae.entity.UserEntity;

public class ActivityLogin extends AppCompatActivity {
    // Views
    private EditText editId, editPw;
    private Button btnLogin;
    private TextView tvFind, tvSignUp;

    // Data
    public UserEntity me;

    // < Methods >
    private void onClickFindAccount(View view) {}
    private void onClickSingUp(View view) {}
    private void onClickLogin(View view) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}