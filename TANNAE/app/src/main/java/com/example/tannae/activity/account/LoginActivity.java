package com.example.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.network.Network;
import com.example.tannae.network.RetrofitClient;
import com.example.tannae.network.ServiceApi;
import com.example.tannae.sub.InnerDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// < Login Activity >
public class LoginActivity extends AppCompatActivity {
    private EditText etID, etPW;
    private Button btnLogin, btnFind, btnSignUp;
    private long backKeyPressedTime = 0;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Retrofit Client
        Network.service = RetrofitClient.getClient().create(ServiceApi.class);
        new InnerDB(getApplicationContext()).setSharedPreferences();

        if(InnerDB.sp.getString("id", null) != null && InnerDB.sp.getString("pw", null) != null){
            Toast.makeText(LoginActivity.this, InnerDB.sp.getString("uname", null )+"님이 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Setting
        setViews();
        setEventListeners();
    }

    // < Register views >
    private void setViews() {
        etID = findViewById(R.id.et_id_account_edit);
        etPW = findViewById(R.id.et_pw_account_edit);
        btnLogin = findViewById(R.id.btn_login);
        btnFind = findViewById(R.id.btn_find);
        btnSignUp = findViewById(R.id.btn_signup);
    }

    // < Register event listeners >
    private void setEventListeners() {
        // Login [RETROFIT]
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if ID and PW is entered
                String id = etID.getText().toString();
                String pw = etPW.getText().toString();
                if(id.length() == 0 || pw.length() == 0) {
                    Toast.makeText(getApplicationContext(), "로그인 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);                    // 임시 코드
                    startActivity(intent); // 임시 코드
                    finish(); // 임시 코드 // finish를 넣어주지 않으면 메인 화면에서 뒤로가기 두번으로 앱 종료가 안돼서 다시 추가함
                    return;
                }
                // Check if entered ID/PW is a user
                Network.service.login(id, pw).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            JSONArray resArr = new JSONArray(response.body());
                            JSONObject resObj = resArr.getJSONObject(0);
                            String resType = resObj.getString("resType");

                            if (resType.equals("OK")) {
                                JSONObject user = resArr.getJSONObject(1); // 이게 InnerDB data
                                InnerDB.setUserOutTOIn(user);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else
                                Toast.makeText(getApplicationContext(), resType, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    }
                });
            }
        });

        // Start Find Activity
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(intent);
            }
        });

        // Start Sign up Activity
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    // < BackPress >
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this,"종료하려면 한번 더 누르세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            finish();
        }
    }
}