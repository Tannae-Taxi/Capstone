package com.example.tannae.activity.user_service;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Data;
import com.example.tannae.sub.ListViewAdapter;
import com.example.tannae.sub.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        setViews();
        setAdapter();
    }

    private void setViews() {
        listView = findViewById(R.id.lv_list_faq);
        setSupportActionBar(toolbar = findViewById(R.id.topAppBar_faq));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("FAQ");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setAdapter() {
        Network.service.getContent().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject res = new JSONObject(response.body());
                    String message = res.getString("message");
                    if (message.equals("OK")) {
                        JSONArray lists = res.getJSONArray("result");
                        adapter = new ListViewAdapter();
                        boolean faq = false;
                        for (int i = 0; i < lists.length(); i++) {
                            JSONObject list = lists.getJSONObject(i);
                            int state = list.getInt("state");
                            if (state == 1) {
                                adapter.addItem(new Data(list.getString("usn"),
                                        list.getString("title"), list.getString("content"), list.getString("answer"), list.getString("date"),
                                        state, "FAQ"));
                                faq = true;
                            }
                        }
                        if (!faq) {
                            Toaster.show(getApplicationContext(), "등록된 FAQ 가 없습니다.");
                            onBackPressed();
                        } else
                            listView.setAdapter(adapter);
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
}
