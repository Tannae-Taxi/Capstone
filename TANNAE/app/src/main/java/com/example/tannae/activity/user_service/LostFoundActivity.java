package com.example.tannae.activity.user_service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Lost;
import com.example.tannae.sub.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        setEventListeners();
        setAdapter();
    }

    private void setViews() {
        toolbar = findViewById(R.id.topAppBar_lost_found);

        listView = (ListView) findViewById(R.id.lv_list_lost_found);

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
                            adapter.addItem(new Lost(list.getString("data"), list.getString("license"), list.getString("type")));
                        }
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

        listView.setAdapter(adapter);
    }

    public class ListViewAdapter extends BaseAdapter {
        ArrayList<Lost> items = new ArrayList<Lost>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Lost item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final Lost list = items.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lost_listview_list_item, viewGroup, false);
            } else {
                View view = new View(context);
                view = (View) convertView;
            }

            TextView date = (TextView) convertView.findViewById(R.id.tv_date_lost_listview);
            TextView license = (TextView) convertView.findViewById(R.id.tv_license_lost_listview);
            TextView type = (TextView) convertView.findViewById(R.id.tv_type_lost_list_view);

            date.setText("분실물 등록일 : " + list.getData("date").toString());
            license.setText("차량번호 : " + list.getData("license").toString());
            type.setText("분실물 : " + list.getData("type").toString());

            return convertView;
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
