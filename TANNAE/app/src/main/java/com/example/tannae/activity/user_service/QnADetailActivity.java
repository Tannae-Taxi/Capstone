package com.example.tannae.activity.user_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnADetailActivity extends AppCompatActivity {
    private TextView tvDate, tvTitle, tvContent, tvAnswer;
    private EditText etAnswer;
    private Button btnEdit, btnDelete, btnAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_detail);
        setViews();
        setEventListeners();
    }

    private void setViews() {
        (tvDate = findViewById(R.id.tv_date_qna_detail)).setText(getIntent().getStringExtra("date"));
        (tvTitle = findViewById(R.id.tv_title_qna_detail)).setText(getIntent().getStringExtra("title"));
        (tvContent = findViewById(R.id.tv_content_qna_detail)).setText(getIntent().getStringExtra("content"));
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
    }

    private void setEventListeners() {
        btnEdit.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), QnAEditActivity.class)
                .putExtra("csn", getIntent().getStringExtra("csn"))
                .putExtra("title", getIntent().getStringExtra("title"))
                .putExtra("content", getIntent().getStringExtra("content"))
                .putExtra("answer", getIntent().getStringExtra("answer"))));
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
    }
}
