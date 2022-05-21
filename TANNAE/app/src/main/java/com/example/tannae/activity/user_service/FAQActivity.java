package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.sub.ListViewAdapter;

public class FAQActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        setViews();
        setEventListeners();
        adapter = new ListViewAdapter();
        /*adapter.addItem(new Content("FAQ Title", "내용1"));
        adapter.addItem(new Content("제목2", "내용2"));
        adapter.addItem(new Content("제목1", "내용1"));
        adapter.addItem(new Content("제목2", "내용2"));
        adapter.addItem(new Content("제목1", "내용1"));
        adapter.addItem(new Content("제목2", "내용2"));
        adapter.addItem(new Content("제목1", "내용1"));
        adapter.addItem(new Content("제목2", "내용2"));*/
        listView.setAdapter(adapter);
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void setViews() {
        toolbar = findViewById(R.id.topAppBar_faq);

        listView = (ListView) findViewById(R.id.lv_list_faq);

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
}
