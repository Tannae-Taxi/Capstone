package com.example.tannae.sub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tannae.R;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Data data = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = data.getType().equals("Payment") ? R.layout.payment_listview_list_item
                    : data.getType().equals("Lost") ? R.layout.lost_listview_list_item
                    : data.getType().equals("History") ? R.layout.history_listview_list_item
                    : R.layout.content_listview_list_item;
            convertView = inflater.inflate(layout, parent, false);
        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        if (data.getType().equals("Payment")) {
            ((TextView) convertView.findViewById(R.id.tv_usn_payment)).setText("탑승자 : " + data.getData("ud").toString());
            ((TextView) convertView.findViewById(R.id.tv_origin_payment)).setText("출발지 : " + data.getData("origin").toString());
            ((TextView) convertView.findViewById(R.id.tv_destination_payment)).setText("도착지 : " + data.getData("destination").toString());
            ((TextView) convertView.findViewById(R.id.tv_cost_payment)).setText("요금 : " + data.getData("cost").toString());
        } else if (data.getType().equals("Lost")) {
            ((TextView) convertView.findViewById(R.id.tv_date_lost_listview)).setText("분실물 등록일 : " + data.getData("date").toString());
            ((TextView) convertView.findViewById(R.id.tv_license_lost_listview)).setText("차량번호 : " + data.getData("license").toString());
            ((TextView) convertView.findViewById(R.id.tv_type_lost_list_view)).setText("분실물 : " + data.getData("type").toString());
        } else if (data.getType().equals("History")) {
            ((TextView) convertView.findViewById(R.id.tv_date_history_listview)).setText("이용 날짜 : " + data.getData("ud").toString());
            ((TextView) convertView.findViewById(R.id.tv_origin_history_listview)).setText("출발지 : " + data.getData("origin").toString());
            ((TextView) convertView.findViewById(R.id.tv_destination_history_listview)).setText("도착지 : " + data.getData("destination").toString());
            ((TextView) convertView.findViewById(R.id.tv_cost_history_listview)).setText("요금 : " + data.getData("cost").toString());
        } else if (data.getType().equals("Content")) {

        }

        return convertView;
    }
}
