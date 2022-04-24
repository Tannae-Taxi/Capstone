package com.example.tannae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private Button btnCheckID, btnSignUp;
    private RadioGroup rgSex;
    private EditText etID, etPW, etPWR, etName, etRRN, etPhone, etEmail;
    private boolean availableID = false, availablePW = false, availablePWR = false, checkedID = false, checkedSex;

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
        rgSex = findViewById(R.id.rg_sex);
        etID = findViewById(R.id.et_id);
        etPW = findViewById(R.id.et_pw);
        etPWR = findViewById(R.id.et_checkpw);
        etName = findViewById(R.id.et_name);
        etRRN = findViewById(R.id.et_rrn);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
    }

    private void setEventListeners() {
        etID.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 여기에 ID 입력 할 때마다의 ID 형식 체크 작성
                // 형식을 체크했을 때 올바른 ID 형식이면 availableID = true;
                // 형식을 체크했을 때 올바르지 않은 ID 형식이면 availableID = false;
                return false;
            }
        });
        // etPW 에도 etID 와 동일하게 PW 형식을 체크하는 이벤트 리스너를 작성하고 올바름 여부에 따라 availablePW 체크
        // 다만, PW를 체크할 때는 PW 확인을 위해 재입력하는 etPWR 또한 고려. 이 때는 availablePWR 사용

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
        btnCheckID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!availableID) {
                    Toast.makeText(getApplicationContext(), "지원되지 않는 ID 형식입니다. 다른 ID를 사용해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Network.service.checkID(etID.getText().toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            JSONObject resObj = new JSONObject(response.body());
                            String resType = resObj.getString("resType");
                            if (resType.equals("OK")) {
                                checkedID = true;
                                Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                checkedID = false;
                                Toast.makeText(getApplicationContext(), "이미 사용 중인 ID 입니다.", Toast.LENGTH_SHORT).show();
                            }
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
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!availableID || availablePW)
                        Toast.makeText(getApplicationContext(), "허용되지 않은 ID or PW 형식입니다.", Toast.LENGTH_SHORT).show();
                    else if (!checkedID)
                        Toast.makeText(getApplicationContext(), "ID 중복을 확인하세요.", Toast.LENGTH_SHORT).show();
                    else {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", etID.getText().toString());
                        reqObj.put("pw", etPW.getText().toString());
                        reqObj.put("uname", etName.getText().toString());
                        reqObj.put("rrn", etRRN.getText().toString());
                        reqObj.put("sex", checkedSex);
                        reqObj.put("phone", etPhone.getText().toString());
                        reqObj.put("email", etEmail.getText().toString());
                        Network.service.signup(reqObj).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                try {
                                    JSONArray resArr = new JSONArray(response.body());
                                    JSONObject resObj = resArr.getJSONObject(0);
                                    String resType = resObj.getString("resType");
                                    if(resType.equals("OK")) {
                                        Toast.makeText(getApplicationContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
