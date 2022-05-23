package com.example.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// << AccountEdit >>
public class AccountEditActivity extends AppCompatActivity {
    private EditText etID, etPW, etCheckPW, etEmail, etPhone;
    private TextView tvCheckID, tvCheckPW;
    private Button btnCheckID, btnEdit;
    private Toolbar toolbar;
    private boolean availableID = false, checkedID = false, availablePW = false, availablePWC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        etID = findViewById(R.id.et_id_account_edit);
        etPW = findViewById(R.id.et_pw_account_edit);
        etCheckPW = findViewById(R.id.et_checkpw_account_edit);
        etEmail = findViewById(R.id.et_email_account_edit);
        etPhone = findViewById(R.id.et_phone_account_edit);
        tvCheckID = findViewById(R.id.tv_checkid_account_edit);
        tvCheckPW = findViewById(R.id.tv_retrypw_account_edit);
        btnCheckID = findViewById(R.id.btn_checkid_account_edit);
        btnEdit = findViewById(R.id.btn_edit_account_edit);

        setSupportActionBar((toolbar = findViewById(R.id.topAppBar_accountedit)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AccountActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
    }

    private void setEventListeners() {
        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String id = etID.getText().toString();
                if (id.length() == 0) {
                    tvCheckID.setTextColor(0xAA000000);
                    tvCheckID.setText("영문 혹은 숫자를 사용하여 6자리 이상 작성하세요.");
                    availableID = false;
                } else if (id.length() >= 6 && (id.matches(".*[a-zA-Z].*") || id.matches(".*[0-9].*"))
                        && !id.matches(".*[가-힣].*") && !id.matches(".*[\\W].*")) {
                    tvCheckID.setTextColor(0xAA0000FF);
                    tvCheckID.setText("사용 가능한 ID 형식입니다.");
                    availableID = true;
                } else {
                    tvCheckID.setTextColor(0xAAFF0000);
                    tvCheckID.setText("사용 불가능한 ID 형식입니다.");
                    availableID = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnCheckID.setOnClickListener(v -> {
            if (!availableID)
                Toaster.show(getApplicationContext(), "지원되지 않는 ID 형식입니다. \n다른 ID를 사용해주세요.");
            else
                Network.service.checkID(etID.getText().toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String message = response.body();
                        checkedID = message.equals("OK");
                        Toaster.show(getApplicationContext(), message.equals("OK") ? "사용 가능한 ID 입니다." : message);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toaster.show(getApplicationContext(), "Error");
                        Log.e("Error", t.getMessage());
                    }
                });
        });

        // Check if PW type is available
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
                    tvCheckPW.setText("사용 불가능한 PW 형식입니다..");
                    availablePW = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Check if PWR is identical with PW
        etCheckPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwr = etCheckPW.getText().toString();
                if (!availablePW) {
                    tvCheckPW.setTextColor(0xAAFF0000);
                    tvCheckPW.setText("사용 불가능한 PW 형식입니다.");
                    availablePWC = false;
                } else {
                    if (etPW.getText().toString().equals(pwr)) {
                        tvCheckPW.setTextColor(0xAA0000FF);
                        tvCheckPW.setText("비밀번호가 일치합니다.");
                        availablePWC = true;
                    } else {
                        tvCheckPW.setTextColor(0xAAFF0000);
                        tvCheckPW.setText("비밀번호가 일치하지 않습니다.");
                        availablePWC = false;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        btnEdit.setOnClickListener(v -> {
            try {
                if (!availableID || !availablePW)
                    Toaster.show(getApplicationContext(), "허용되지 않은 ID or PW 형식입니다.");
                else if (!checkedID)
                    Toaster.show(getApplicationContext(), "ID 중복을 확인하세요");
                else if (!availablePWC)
                    Toaster.show(getApplicationContext(), "PW가 일치하지 않습니다.");
                else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                    Toaster.show(getApplicationContext(), "Email 을 정확하게 작성하세요.");
                else if (!Patterns.PHONE.matcher(etPhone.getText().toString()).matches())
                    Toaster.show(getApplicationContext(), "전화번호를 정확하게 작성하세요.");
                else {
                    Network.service.editAccount(new JSONObject().put("usn", InnerDB.sp.getString("usn", null))
                            .put("id", etID.getText().toString()).put("pw", etPW.getText().toString())
                            .put("email", etEmail.getText().toString()).put("phone", etPhone.getText().toString())).enqueue(new Callback<Boolean>() {

                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            InnerDB.editor.clear().apply();
                            Toaster.show(getApplicationContext(), "회원정보가 수정되었습니다.\n다시 로그인해주세요.");
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toaster.show(getApplicationContext(), "Error");
                            Log.e("Error", t.getMessage());
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
