package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.sub.ListViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class QnAActivity extends AppCompatActivity {

    private Button btnsearch;
    private EditText etsearchtitle;
    private FloatingActionButton fabqna;
    private Toolbar toolbar;

    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        setViews();
        setEventListeners();
        adapter = new ListViewAdapter();
        /*adapter.addItem(new Content("제목1", "내용1"));
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
        btnsearch = findViewById(R.id.btn_search_qna);
        etsearchtitle = findViewById(R.id.et_search_title_qna);
        toolbar = findViewById(R.id.topAppBar_qna);
        listView = (ListView) findViewById(R.id.lv_list_qna);
        fabqna = findViewById(R.id.fab_qna);

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

        fabqna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QnAEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
