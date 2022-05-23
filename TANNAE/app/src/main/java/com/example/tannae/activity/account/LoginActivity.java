package com.example.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.network.Network;
import com.example.tannae.network.RetrofitClient;
import com.example.tannae.network.ServiceApi;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Network.service = RetrofitClient.getClient().create(ServiceApi.class);
        new InnerDB(getApplicationContext()).setSharedPreferences();

        String id = InnerDB.sp.getString("id", null);
        String pw = InnerDB.sp.getString("pw", null);
        if (id != null) login(id, pw, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
    }

    private void setViews() {
        etID = findViewById(R.id.et_id_account_edit);
        etPW = findViewById(R.id.et_pw_account_edit);
        (btnFind = findViewById(R.id.btn_find)).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FindActivity.class)));;
        (btnSignUp = findViewById(R.id.btn_signup)).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        (btnLogin = findViewById(R.id.btn_login)).setOnClickListener(v -> {
            String id = etID.getText().toString();
            String pw = etPW.getText().toString();
            if (id.length() == 0 || pw.length() == 0)
                Toaster.show(getApplicationContext(), "로그인 정보를 입력하세요.");
            else
                login(id, pw, false);
        });
    }

    // < Login >
    public void login(String id, String pw, boolean auto) {
        // Check if entered ID/PW is a user
        Network.service.login(id, pw).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject res = new JSONObject(response.body());
                    String message = res.getString("message");

                    if (message.equals("OK")) {
                        InnerDB.setUser(res.getJSONObject("result"));
                        if (auto)
                            Toaster.show(getApplicationContext(), InnerDB.sp.getString("uname", null) + "님이 자동로그인 되었습니다.");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else
                        Toaster.show(getApplicationContext(), message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toaster.show(getApplicationContext(), "Error");
                Log.e("Error", t.getMessage());
            }
        });
    }

    // < BackPress >
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toaster.show(getApplicationContext(), "종료하려면 한번 더 누르세요.");
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
}