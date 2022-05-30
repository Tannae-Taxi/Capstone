package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Data;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.ListViewAdapter;
import com.example.tannae.sub.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private Button btnSend;
    private RatingBar rbDriverRating;

    private ListView listView = null;
    private ListViewAdapter adapter = null;
    private Toolbar toolbar;

    private String license;
    private JSONObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        try {
            result = new JSONObject(getIntent().getStringExtra("result"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setViews();
        setEventListeners();
        setAdapter();
        if(InnerDB.sp.getInt("point", 0) < 0)
            Toaster.show(getApplicationContext(), "요금이 부족합니다.\n요금을 충전하기 전까지 이용이 제합됩니다.");
    }

    private void setViews() {
        btnSend = findViewById(R.id.btn_send_payment);
        rbDriverRating = findViewById(R.id.rb_driverrating_payment);
        listView = findViewById(R.id.lv_receipt_payment);
    }

    private void setEventListeners() {
        btnSend.setOnClickListener(v -> {
            try {
                if (InnerDB.getUser().getString("usn").equals(result.getString("driver")))
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                else {
                    Network.service.evaluate(new JSONObject()
                            .put("license", license).put("score", rbDriverRating.getRating()).put("usn", InnerDB.getUser().getString("usn")))
                            .enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Toaster.show(getApplicationContext(), "평가를 완료하였습니다.");
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    private void setAdapter() {
        try {
            // Create ListViewAdapter
            adapter = new ListViewAdapter();

            // Set items
            if (InnerDB.getUser().getString("usn").equals(result.getString("driver")))
                rbDriverRating.setVisibility(View.INVISIBLE);
            Iterator i = result.keys();
            while (i.hasNext()) {
                String usn = i.next().toString();
                if (usn.equals("license")) {
                    license = result.getString("license");
                    continue;
                } else if (usn.equals("driver"))
                    continue;
                JSONObject res = result.getJSONObject(usn);
                adapter.addItem(new Data(usn, res.getString("start"), res.getString("end"), res.getInt("cost"), "Payment"));
            }

            // Set adapter
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Toaster.show(getApplicationContext(), "확인 버튼을 눌러주세요.");
    }
}
