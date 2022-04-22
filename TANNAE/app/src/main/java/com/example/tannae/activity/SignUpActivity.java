package com.example.tannae.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private Button btnCheckID, btnSignUp;
    private EditText etID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        btnCheckID = findViewById(R.id.btn_checkID);
        btnSignUp = findViewById(R.id.btn_sign_up);
        etID = findViewById(R.id.et_id);
    }

    private void setEventListeners() {
        btnCheckID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network.service.checkID(etID.getText().toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonReq = new JSONObject();
                    jsonReq.put("id", etID.getText().toString());
                    Network.service.signup(jsonReq).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
