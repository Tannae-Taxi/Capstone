package com.team_seven.tannae.sub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team_seven.tannae.R;
import com.team_seven.tannae.activity.user_service.QnADetailActivity;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Data> items = new ArrayList<>();

    public void addItem(Data item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Data data = items.get(position);
        System.out.println(data.getType());

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = data.getType().equals("Payment") ? R.layout.payment_listview_list_item
                    : data.getType().equals("Lost") ? R.layout.lost_listview_list_item
                    : data.getType().equals("History") ? R.layout.history_listview_list_item
                    : data.getType().equals("QnA") ? R.layout.qna_listview_list_item
                    : R.layout.faq_listview_list_item;
            convertView = inflater.inflate(layout, parent, false);
        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        if (data.getType().equals("Payment")) {
            ((TextView) convertView.findViewById(R.id.tv_usn_payment)).setText("탑승자 : " + data.getData("ud").toString());
            ((TextView) convertView.findViewById(R.id.tv_origin_payment)).setText("출발지 : " + data.getData("origin").toString());
            ((TextView) convertView.findViewById(R.id.tv_destination_payment)).setText("도착지 : " + data.getData("destination").toString());
            ((TextView) convertView.findViewById(R.id.tv_cost_payment)).setText("요금 : " + data.getData("cost").toString() + "원");
            InnerDB.editor.putInt("point", InnerDB.sp.getInt("point", 0) - (int)data.getData("cost")).apply();
        } else if (data.getType().equals("Lost")) {
            ((TextView) convertView.findViewById(R.id.tv_date_lost_listview)).setText("분실물 등록일 : " + data.getData("date").toString());
            ((TextView) convertView.findViewById(R.id.tv_license_lost_listview)).setText("차량번호 : " + data.getData("license").toString());
            ((TextView) convertView.findViewById(R.id.tv_type_lost_list_view)).setText("분실물 : " + data.getData("type").toString());
        } else if (data.getType().equals("History")) {
            ((TextView) convertView.findViewById(R.id.tv_date_history_listview)).setText("이용 날짜 : " + data.getData("ud").toString());
            ((TextView) convertView.findViewById(R.id.tv_origin_history_listview)).setText("출발지 : " + data.getData("origin").toString());
            ((TextView) convertView.findViewById(R.id.tv_destination_history_listview)).setText("도착지 : " + data.getData("destination").toString());
            ((TextView) convertView.findViewById(R.id.tv_cost_history_listview)).setText("요금 : " + data.getData("cost").toString() + "원");
        } else if (data.getType().equals("QnA")) {
            ((TextView) convertView.findViewById(R.id.tv_title_qna_listview)).setText(data.getData("title").toString());
            convertView.findViewById(R.id.layout_qna_listview).setOnClickListener(v -> context.startActivity(new Intent(context, QnADetailActivity.class)
                    .putExtra("csn", data.getData("csn").toString())
                    .putExtra("usn", data.getData("usn").toString())
                    .putExtra("title", data.getData("title").toString())
                    .putExtra("content", data.getData("content").toString())
                    .putExtra("answer", data.getData("answer").toString())
                    .putExtra("date", data.getData("date").toString())));
        } else if(data.getType().equals("FAQ")) {
            ((TextView) convertView.findViewById(R.id.tv_title_faq_listview)).setText(data.getData("title").toString());
            ((TextView) convertView.findViewById(R.id.tv_content_faq_listview)).setText(data.getData("content").toString());
            ((TextView) convertView.findViewById(R.id.tv_answer_faq_listview)).setText(data.getData("answer").toString());
            ((TextView) convertView.findViewById(R.id.tv_date_faq_listview)).setText(data.getData("date").toString());
        }

        return convertView;
    }
}
