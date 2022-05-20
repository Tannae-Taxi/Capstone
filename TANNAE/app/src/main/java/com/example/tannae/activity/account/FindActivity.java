package com.example.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// << FindActivity >>
public class FindActivity extends AppCompatActivity {
    private TextView tvMyId, tvMyPw;
    private EditText etName, etRRN, etEmail, etPhone;
    private Button btnFindAccount;
    private Toolbar toolbar;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        // Setting
        setViews();
        setEventListeners();
    }

    // < Register views >
    private void setViews(){
        etName = findViewById(R.id.et_name_find);
        etRRN = findViewById(R.id.et_rrn_find);
        etEmail = findViewById(R.id.et_email_find);
        etPhone = findViewById(R.id.et_phone_find);
        btnFindAccount= findViewById(R.id.btn_find_find);
        tvMyId = findViewById(R.id.tv_myid_find);
        tvMyPw = findViewById(R.id.tv_mypw_find);
        toolbar = findViewById(R.id.topAppBar_find);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("type", false);
                startActivity(intent);
            }
        });
    }

    // < Register event listeners >
    private void setEventListeners(){
        // Store RRN by user input
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

        // Find account [RETROFIT]
        btnFindAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if entered info's are available
                if (etName.getText().toString().length() == 0)
                    Toaster.show(getApplicationContext(), "이름을 입력하세요.");
                    //Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if (etRRN.getText().toString().length() != 14)
                    Toaster.show(getApplicationContext(), "주민등록번호를 정확하게 입력하세요.");
                    //Toast.makeText(getApplicationContext(), "주민등록번호를 정확하게 입력하세요.", Toast.LENGTH_SHORT).show();
                else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                    Toaster.show(getApplicationContext(), "Email 을 정확하게 작성하세요.");
                    //Toast.makeText(getApplicationContext(), "Email 을 정확하게 작성하세요.", Toast.LENGTH_SHORT).show();
                else if (!Patterns.PHONE.matcher(etPhone.getText().toString()).matches())
                    Toaster.show(getApplicationContext(), "전화번호를 정확하게 작성하세요.");
                    //Toast.makeText(getApplicationContext(), "전화번호를 정확하게 작성하세요.", Toast.LENGTH_SHORT).show();
                // If available request server to find account [RETROFIT]
                else {
                    Network.service.findAccount(etName.getText().toString(), etRRN.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                JSONObject res = new JSONObject(response.body());
                                String message = res.getString("message");

                                if(message.equals("OK")) {
                                    // Show ID and PW at screen
                                    JSONObject user = res.getJSONObject("result");
                                    tvMyId.setText("ID: " + user.getString("id"));
                                    tvMyPw.setText("PW: " + user.getString("pw"));
                                } else
                                    Toaster.show(getApplicationContext(), message);
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toaster.show(getApplicationContext(), "Error");
                            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}