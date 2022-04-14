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
    private Button btnLogin;
    private String url = ";";
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        etID = findViewById(R.id.et_id);
        etPW = findViewById(R.id.et_pw);
        btnLogin = findViewById(R.id.btn_login);
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    private void setEventListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etID.getText().toString();
                String pw = etPW.getText().toString();

                JSONObject req = new JSONObject();

                try {
                    req.put("id", id);
                    req.put("pw", pw);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                service.login(req).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            JSONArray jsArr = new JSONArray(response.body());
                            JSONObject jsObjErr = jsArr.getJSONObject(0);
                            String err = jsObjErr.getString("error");
                            if (err.equals("false")) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else
                                Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                        Log.e("로그인 에러 발생", t.getMessage());
                    }
                });
            }
        });
    }
}