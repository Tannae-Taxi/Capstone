package com.example.tannae.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sqlite.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private Button btnCheckID, btnSignUp;
    private RadioGroup rgSex;
    private EditText etID, etPW, etPWR, etName, etRRN, etPhone, etEmail;
    private TextView tvCheckId, tvCheckPW;
    private boolean availableID = false, checkedID = false, availablePW = false, availablePWR = false, sexType = true, availableEmail = false, availablePhone = false;

    /* p0rivate DBHelper dbHelper;
    private SQLiteDatabase db; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /* dbHelper = new DBHelper(this,1);
        try{
            db = dbHelper.getReadableDatabase();
        }catch (SQLiteException e) {
            db = dbHelper.getWritableDatabase();
        }
        dbHelper.onCreate(db); */

        setViews();
        setEventListeners();

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setViews() {
        btnCheckID = findViewById(R.id.btn_checkID_sign_up);
        btnSignUp = findViewById(R.id.btn_sign_up);
        rgSex = findViewById(R.id.rg_sex_sign_up);
        etID = findViewById(R.id.et_id_sign_up);
        etPW = findViewById(R.id.et_pw_sign_up);
        etPWR = findViewById(R.id.et_checkpw_sign_up);
        etName = findViewById(R.id.et_name_sign_up);
        etRRN = findViewById(R.id.et_rrn_sign_up);
        etPhone = findViewById(R.id.et_phone_sign_up);
        etEmail = findViewById(R.id.et_email_sign_up);

        tvCheckId = findViewById(R.id.tv_checkID_sign_up);
        tvCheckPW = findViewById(R.id.tv_retrypw_sign_up);
    }

    private void setEventListeners() {
        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String id = etID.getText().toString();
                if (id.length() == 0) {
                    tvCheckId.setTextColor(0xAA000000);
                    tvCheckId.setText("영문 혹은 숫자를 사용하여 6자리 이상 작성하세요."); // 이부분 다크모드로 구현하기가 애매. xml의 원본 텍스트랑 같은 형식 사용하고 싶음. 비밀번호도
                    availableID = false;
                } else if (id.length() >= 6 && (id.matches(".*[a-zA-Z].*") || id.matches(".*[0-9].*"))
                        && !id.matches(".*[가-힣].*") && !id.matches(".*[\\W].*")) {
                    tvCheckId.setTextColor(0xAA0000FF);
                    tvCheckId.setText("사용 가능한 ID 형식입니다.");
                    availableID = true;
                } else {
                    tvCheckId.setTextColor(0xAAFF0000);
                    tvCheckId.setText("사용 불가능한 ID 형식입니다.");
                    availableID = false;
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });

        etPW.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = etPW.getText().toString();
                if (etPW.getText().toString().length() == 0) {
                    tvCheckPW.setTextColor(0xAA000000);
                    tvCheckPW.setText("영문, 숫자를 조합하여 8자리 이상으로 작성하세요.");
                    availablePW = false;
                } else if (pw.length() >= 8 && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[0-9].*")
                        && !pw.matches(".*[가-힣].*") && !pw.matches(".*[\\W].*")) {
                    tvCheckPW.setTextColor(0xAA0000FF);
                    tvCheckPW.setText("사용 가능한 PW 형식입니다.");
                    availablePW = true;
                } else {
                    tvCheckPW.setTextColor(0xAAFF0000);
                    tvCheckPW.setText("사용 불가능한 PW 형식입니다.");
                    availablePW = false;
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });

        etPWR.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwr = etPWR.getText().toString();
                if (!availablePW) {
                    tvCheckPW.setTextColor(0xAAFF0000);
                    tvCheckPW.setText("사용 불가능한 PW 형식입니다.");
                    availablePWR = false;
                } else {
                    if(etPW.getText().toString().equals(pwr)) {
                        tvCheckPW.setTextColor(0xAA0000FF);
                        tvCheckPW.setText("비밀번호가 일치합니다.");
                        availablePWR = true;
                    } else {
                        tvCheckPW.setTextColor(0xAAFF0000);
                        tvCheckPW.setText("비밀번호가 불일치합니다.");
                        availablePWR = false;
                    }
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sexType = (checkedId == R.id.rb_man_sign_up) ? true : false;
            }
        });

        etRRN.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String rrn = etRRN.getText().toString();
                if (rrn.length() == 7 && rrn.charAt(rrn.length() - 1) != '-') {
                    etRRN.setText(new StringBuffer(rrn).insert(6, '-').toString());
                    etRRN.setSelection(rrn.length() + 1);
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });

        btnCheckID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!availableID) {
                    Toast.makeText(getApplicationContext(), "지원되지 않는 ID 형식입니다. \n다른 ID를 사용해주세요.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), "사용 가능한 ID 형식입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                checkedID = false;
                                Toast.makeText(getApplicationContext(), "이미 사용 중인 ID 형식입니다.", Toast.LENGTH_SHORT).show();
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
                    if (!availableID || !availablePW)
                        Toast.makeText(getApplicationContext(), "허용되지 않은 ID or PW 형식입니다.", Toast.LENGTH_SHORT).show();
                    else if (!checkedID)
                        Toast.makeText(getApplicationContext(), "ID 중복을 확인하세요.", Toast.LENGTH_SHORT).show();
                    else if (!availablePWR)
                        Toast.makeText(getApplicationContext(), "PW가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    else if (etName.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if (etRRN.getText().toString().length() != 14)
                        Toast.makeText(getApplicationContext(), "주민등록번호를 정확하게 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                        Toast.makeText(getApplicationContext(), "Email 을 정확하게 작성하세요.", Toast.LENGTH_SHORT).show();
                    else if (!Patterns.PHONE.matcher(etPhone.getText().toString()).matches())
                        Toast.makeText(getApplicationContext(), "전화번호를 정확하게 작성하세요.", Toast.LENGTH_SHORT).show();
                    else {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", etID.getText().toString());
                        reqObj.put("pw", etPW.getText().toString());
                        reqObj.put("uname", etName.getText().toString());
                        reqObj.put("rrn", etRRN.getText().toString());
                        reqObj.put("sex", sexType);
                        reqObj.put("phone", etPhone.getText().toString());
                        reqObj.put("email", etEmail.getText().toString());

                        Network.service.signup(reqObj).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                try {
                                    JSONObject resObj = new JSONObject(response.body());
                                    String resType = resObj.getString("resType");
                                    if (resType.equals("OK")) {
                                        /* db.execSQL("INSERT INTO User('id') values('" + etID.getText().toString() + "');");
                                        db.execSQL("INSERT INTO User('pw') values('" + etPW.getText().toString() + "';");
                                        db.execSQL("INSERT INTO User('uname') values('" + etName.getText().toString() + "');");
                                        db.execSQL("INSERT INTO User('rrn') values('" + etRRN.getText().toString() + "');");
                                        db.execSQL("INSERT INTO User('sex') values('" + sexType + "');");
                                        db.execSQL("INSERT INTO User('phone') values('" + etPhone.getText().toString() + "');");
                                        db.execSQL("INSERT INTO User('email') values('" + etEmail.getText().toString() + "');"); */

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
