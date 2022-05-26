package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnAEditActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etTitle, etContent;
    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_edit);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        (etTitle = findViewById(R.id.et_title_qna_edit)).setText(getIntent().getStringExtra("title"));
        (etContent = findViewById(R.id.et_content_qna_edit)).setText(getIntent().getStringExtra("content"));
        btnEdit = findViewById(R.id.btn_edit_qna_edit);
        setSupportActionBar(toolbar = findViewById(R.id.topAppBar_qnaedit));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("QnA 수정");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setEventListeners() {
        btnEdit.setOnClickListener(v -> {
            try {
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();
                if (title.length() == 0)
                    Toaster.show(getApplicationContext(), "제목을 입력해주세요.");
                else if (content.length() == 0)
                    Toaster.show(getApplicationContext(), "내용을 입력해주세요.");
                else if (getIntent().getBooleanExtra("flag", true))
                    Network.service.postContent(InnerDB.getUser()
                            .put("title", etTitle.getText().toString())
                            .put("content", etContent.getText().toString()))
                            .enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Toaster.show(getApplicationContext(), "QnA가 등록되었습니다.");
                                    startActivity(new Intent(getApplicationContext(), QnAActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    Toaster.show(getApplicationContext(), "Error");
                                    Log.e("Error", t.getMessage());
                                }
                            });
                else
                    Network.service.editContent(InnerDB.getUser()
                            .put("csn", getIntent().getStringExtra("csn"))
                            .put("title", etTitle.getText().toString())
                            .put("content", etContent.getText().toString()))
                            .enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Toaster.show(getApplicationContext(), "QnA가 수정되었습니다.");
                                    startActivity(new Intent(getApplicationContext(), QnAActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    Toaster.show(getApplicationContext(), "Error");
                                    Log.e("Error", t.getMessage());
                                }
                            });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
