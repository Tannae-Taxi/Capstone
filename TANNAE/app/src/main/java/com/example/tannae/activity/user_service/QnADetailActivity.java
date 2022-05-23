package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class QnADetailActivity extends AppCompatActivity {
    private TextView tvAnswer;
    private EditText etAnswer;
    private Button btnEdit, btnDelete, btnAnswer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_detail);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        ((TextView) findViewById(R.id.tv_date_qna_detail)).setText(getIntent().getStringExtra("date"));
        ((TextView) findViewById(R.id.tv_title_qna_detail)).setText(getIntent().getStringExtra("title"));
        ((TextView) findViewById(R.id.tv_content_qna_detail)).setText(getIntent().getStringExtra("content"));
        (tvAnswer = findViewById(R.id.tv_answer_qna_detail)).setText(getIntent().getStringExtra("answer"));
        (btnEdit = findViewById(R.id.btn_edit_qna_detail))
                .setVisibility(InnerDB.sp.getString("usn", null)
                        .equals(getIntent().getStringExtra("usn")) ? View.VISIBLE : View.INVISIBLE);
        (btnDelete = findViewById(R.id.btn_delete_qna_detail))
                .setVisibility(InnerDB.sp.getString("usn", null)
                        .equals(getIntent().getStringExtra("usn")) ? View.VISIBLE : View.INVISIBLE);
        (etAnswer = findViewById(R.id.et_answer_qna_detail))
                .setVisibility(InnerDB.sp.getString("usn", null).charAt(0) == 'm'
                        ? View.VISIBLE : View.INVISIBLE);
        (btnAnswer = findViewById(R.id.btn_answer_qna_detail))
                .setVisibility(InnerDB.sp.getString("usn", null).charAt(0) == 'm'
                        ? View.VISIBLE : View.INVISIBLE);
        setSupportActionBar(toolbar = findViewById(R.id.topAppBar_qna_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setEventListeners() {
        btnEdit.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), QnAEditActivity.class)
                .putExtra("csn", getIntent().getStringExtra("csn"))
                .putExtra("title", getIntent().getStringExtra("title"))
                .putExtra("content", getIntent().getStringExtra("content"))
                .putExtra("answer", getIntent().getStringExtra("answer"))
                .putExtra("flag", false)));

        btnDelete.setOnClickListener(v -> {
            try {
                Network.service.deleteContent(InnerDB.getUser().put("csn", getIntent().getStringExtra("csn"))).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toaster.show(getApplicationContext(), "QnA가 삭제되었습니다.");
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

        btnAnswer.setOnClickListener(v -> {
            try {
                Network.service.postAnswer(InnerDB.getUser()
                        .put("answer", etAnswer.getText().toString())
                        .put("csn", getIntent().getStringExtra("csn")))
                        .enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                tvAnswer.setText(etAnswer.getText().toString());
                                etAnswer.setText("");
                                Toaster.show(getApplicationContext(), "답변이 등록되었습니다.");
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
