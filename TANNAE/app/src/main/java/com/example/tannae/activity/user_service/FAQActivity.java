package com.example.tannae.activity.user_service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.account.LoginActivity;
import com.example.tannae.activity.main_service.MainActivity;
import com.example.tannae.sub.List;

import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        setViews();
        adapter = new ListViewAdapter();
        adapter.addItem(new List("FAQ Title", "내용1"));
        adapter.addItem(new List("제목2", "내용2"));
        adapter.addItem(new List("제목1", "내용1"));
        adapter.addItem(new List("제목2", "내용2"));
        adapter.addItem(new List("제목1", "내용1"));
        adapter.addItem(new List("제목2", "내용2"));
        adapter.addItem(new List("제목1", "내용1"));
        adapter.addItem(new List("제목2", "내용2"));
        listView.setAdapter(adapter);
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void setViews() {
        toolbar = findViewById(R.id.topAppBar_faq);

        listView = (ListView) findViewById(R.id.lv_list_faq);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public class ListViewAdapter extends BaseAdapter {
        ArrayList<List> items = new ArrayList<List>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(List item) {
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
            final List list = items.get(position);

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.community_listview_list_item, viewGroup, false);

            }else {
                View view = new View(context);
                view = (View) convertView;

            }

            TextView title = (TextView) convertView.findViewById(R.id.tv_title);
            TextView content = (TextView) convertView.findViewById(R.id.tv_content);

            title.setText(list.getTitle());
            content.setText(list.getContent());

            return convertView;
        }

    }
}
