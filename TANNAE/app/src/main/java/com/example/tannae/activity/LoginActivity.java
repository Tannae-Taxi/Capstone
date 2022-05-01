package com.example.tannae.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.network.RetrofitClient;
import com.example.tannae.network.ServiceApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etID, etPW;
    private Button btnLogin, btnFind, btnSignUp;

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Network.service = RetrofitClient.getClient().create(ServiceApi.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        etID = findViewById(R.id.et_id_account_edit);
        etPW = findViewById(R.id.et_pw_account_edit);
        btnLogin = findViewById(R.id.btn_login);
        btnFind = findViewById(R.id.btn_find);
        btnSignUp = findViewById(R.id.btn_signup);
    }

    private void setEventListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etID.getText().toString();
                String pw = etPW.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                /*if(id.length() == 0 || pw.length() == 0) {
                    Toast.makeText(getApplicationContext(), "로그인 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Network.service.login(id, pw).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            JSONArray resArr = new JSONArray(response.body());
                            JSONObject resObj = resArr.getJSONObject(0);
                            String resType = resObj.getString("resType");
                            if (resType.equals("OK")) {
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
                });*/
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this,"\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            finish();
        }
    }
}