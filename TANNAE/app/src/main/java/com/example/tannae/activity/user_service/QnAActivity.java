package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Data;
import com.example.tannae.sub.ListViewAdapter;
import com.example.tannae.sub.Toaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnAActivity extends AppCompatActivity {
    private FloatingActionButton fabQnA;
    private Toolbar toolbar;
    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        setViews();
        setAdapter();
    }


    private void setViews() {
        listView = (ListView) findViewById(R.id.lv_list_qna);
        (fabQnA = findViewById(R.id.fab_qna)).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), QnAEditActivity.class).putExtra("flag", true)));
        setSupportActionBar((toolbar = findViewById(R.id.topAppBar_qna)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        for (int i = 0; i < lists.length(); i++) {
                            JSONObject list = lists.getJSONObject(i);
                            adapter.addItem(new Data(list.getString("csn"), list.getString("usn"),
                                    list.getString("title"), list.getString("content"), list.getString("answer"), list.getString("date"),
                                    list.getInt("state"), getApplicationContext(), "QnA"));
                        }
                        listView.setAdapter(adapter);
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
