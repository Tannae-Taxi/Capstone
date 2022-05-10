package com.example.tannae.activity.account;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// < Login Activity >
public class LoginActivity extends AppCompatActivity {
    private EditText etID, etPW;
    private Button btnLogin, btnFind, btnSignUp;
    private long backKeyPressedTime = 0;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Retrofit Client
        Network.service = RetrofitClient.getClient().create(ServiceApi.class);
        ///////////////////////////////////////////// User 정보가 등록되어 있다면 자동 로그인
        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Setting
        setPreferences();
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
                    startActivity(intent);                                                                      // 임시 코드
                    finish();                                                                                   // 임시 코드
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

                                JSONObject user = resArr.getJSONObject(1); // 이게 User data

                                editor.putString("usn", user.getString("usn"));
                                editor.putString("id", etID.getText().toString());
                                editor.putString("pw", etPW.getText().toString());
                                editor.putString("uname", user.getString("uname"));
                                editor.putString("rrn", user.getString("rrn"));
                                editor.putInt("gender", user.getInt("gender"));
                                editor.putString("phone", user.getString("phone"));
                                editor.putString("email", user.getString("email"));
                                editor.putInt("drive", user.getInt("drive"));
                                editor.putInt("points", user.getInt("points"));
                                editor.putFloat("score", BigDecimal.valueOf(user.getDouble("score")).floatValue());
                                editor.putInt("state", user.getInt("state"));
                                editor.apply();

                                /* System.out.println("usn이름:"+sp.getString("usn",""));
                                System.out.println("id이름:"+sp.getString("id",""));
                                System.out.println("pw이름:"+sp.getString("pw",""));
                                System.out.println("uname이름:"+sp.getString("uname",""));
                                System.out.println("rrn이름:"+sp.getString("rrn",""));
                                System.out.println("gender이름:"+sp.getInt("gender",0));
                                System.out.println("phone이름:"+sp.getString("phone",""));
                                System.out.println("email이름:"+sp.getString("email",""));
                                System.out.println("drive이름:"+sp.getInt("drive",0));
                                System.out.println("points이름:"+sp.getInt("points",22));
                                System.out.println("score이름:"+sp.getFloat("score",(float) 4.5));
                                System.out.println("state이름:"+sp.getInt("state",0)); */  // sp가 잘 구현 되었는지 테스트용 코드. 잘 됨!


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

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

    public void setPreferences(){
        sp = getSharedPreferences("TTdb", MODE_PRIVATE);
        editor = sp.edit();
    }
}