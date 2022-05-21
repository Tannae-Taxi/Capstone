package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Data;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.ListViewAdapter;
import com.example.tannae.sub.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setViews();
        setEventListeners();
        setAdapter();
    }


    private void setViews() {
        toolbar = findViewById(R.id.topAppBar_history);
        listView = findViewById(R.id.lv_list_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setEventListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setAdapter() {
        try {
            Network.service.getHistory(InnerDB.getUser().getString("usn")).enqueue(new Callback<String>() {
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
                                adapter.addItem(new Data(list.getString("date"), list.getString("origin"), list.getString("dest"), list.getInt("cost"), "History"));
                            }
                            listView.setAdapter(adapter);
                        } else {
                            Toaster.show(getApplicationContext(), message);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
