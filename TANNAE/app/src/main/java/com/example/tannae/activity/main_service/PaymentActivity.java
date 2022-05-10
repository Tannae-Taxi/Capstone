package com.example.tannae.activity.main_service;

import android.content.Context;
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

import com.example.tannae.R;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    private Button btnBack, btnSend;
    private RatingBar rbDriverRating;

    private String TAG = PaymentActivity.class.getSimpleName();
    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setViews();
        setEventListeners();
        adapter = new ListViewAdapter();
        adapter.addItem(new Receipt("파랑이"));
        adapter.addItem(new Receipt("노랑이"));
        adapter.addItem(new Receipt("하양이"));

        listView.setAdapter(adapter);
    }

    private void setViews() {
        btnBack = findViewById(R.id.btn_back_payment);
        btnSend = findViewById(R.id.btn_send_payment);
        rbDriverRating = findViewById(R.id.rb_driverrating_payment);
        listView = findViewById(R.id.lv_receipt_payment);
    }

    private void setEventListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rbDriverRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        // rvReceipt는 RecyclerView 객체의 이벤트 처리 방법 찾아본 후 추가할 예정
    }

    public class ListViewAdapter extends BaseAdapter {
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

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.payment_listview_list_item, viewGroup, false);

            }else {
                View view = new View(context);
                view = (View) convertView;
            }

            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            tv_name.setText(Receipt.getName());

            return convertView;
        }
    }
}
