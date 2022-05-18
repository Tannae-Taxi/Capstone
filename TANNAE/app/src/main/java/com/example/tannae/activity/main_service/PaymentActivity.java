package com.example.tannae.activity.main_service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.user_service.UserServiceListActivity;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Receipt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setViews();
        setEventListeners();
        setAdapter();
    }

    private void setViews() {
        btnSend = findViewById(R.id.btn_send_payment);
        rbDriverRating = findViewById(R.id.rb_driverrating_payment);
        listView = findViewById(R.id.lv_receipt_payment);
        toolbar = findViewById(R.id.topAppBar_payment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setEventListeners() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject data = new JSONObject();
                Network.service.evaluate(data).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        /////////////////////////////////
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        /////////////////////////////
                    }
                });
            }
        });
        rbDriverRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        // rvReceipt는 RecyclerView 객체의 이벤트 처리 방법 찾아본 후 추가할 예정

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
            // Create ListViewAdapter
            adapter = new ListViewAdapter();

            // Set items
            JSONObject result = new JSONObject(getIntent().getStringExtra("result"));
            Iterator i = result.keys();
            while (i.hasNext()) {
                String usn = i.next().toString();
                if (usn.equals("license")) {
                    license = result.getString("license");
                    continue;
                }
                JSONObject res = result.getJSONObject(usn);
                String origin = res.getString("start");
                String destination = res.getString("end");
                int cost = res.getInt("cost");
                adapter.addItem(new Receipt("EXAMPLE"));
                /////////////////////////////////////////////////////////////아이템 추가하가
            }

            // Set adapter
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ListViewAdapter extends BaseAdapter {
        ArrayList<Receipt> items = new ArrayList<Receipt>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Receipt item) {
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
            final Receipt receipt = items.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.payment_listview_list_item, viewGroup, false);

            } else {
                View view = new View(context);
                view = (View) convertView;
            }

            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            name.setText(receipt.getName());

            return convertView;
        }
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
