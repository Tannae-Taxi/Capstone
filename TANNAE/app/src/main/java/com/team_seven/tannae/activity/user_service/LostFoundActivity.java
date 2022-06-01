package com.team_seven.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.team_seven.tannae.R;
import com.team_seven.tannae.network.Network;
import com.team_seven.tannae.sub.Data;
import com.team_seven.tannae.sub.ListViewAdapter;
import com.team_seven.tannae.sub.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostFoundActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);
        setViews();
        setAdapter();
    }

    private void setViews() {
        listView = findViewById(R.id.lv_list_lost_found);
        setSupportActionBar(toolbar = findViewById(R.id.topAppBar_lost_found));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("분실물 조회");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setAdapter() {
        Network.service.getLost().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject res = new JSONObject(response.body());
                    String message = res.getString("message");
                    if (message.equals("OK")) {
                        JSONArray lists = res.getJSONArray("result");
                        adapter = new ListViewAdapter();
                        for (int i = 0; i < lists.length(); i++) {
                            JSONObject list = lists.getJSONObject(i);
                            adapter.addItem(new Data(list.getString("date"), list.getString("license"), list.getString("type"), "Lost"));
                        }
                        listView.setAdapter(adapter);
                    } else {
                        Toaster.show(getApplicationContext(), message);
                        startActivity(new Intent(getApplicationContext(), UserServiceListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
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
