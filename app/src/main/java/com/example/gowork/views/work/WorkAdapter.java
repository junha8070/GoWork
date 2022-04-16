package com.example.gowork.views.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gowork.R;
import com.example.gowork.db.Work;

import java.util.ArrayList;
import java.util.List;

public class WorkAdapter extends BaseAdapter {
    private List<Work> works = new ArrayList<>();
    Context context;

    public WorkAdapter(List<Work> works, Context context){
        this.works = works;
        this.context = context;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    @Override
    public int getCount() {
        return works.size();
    }

    @Override
    public Object getItem(int position) {
        return works.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
//        if(convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.spin)
//        }
//        convertView = LayoutInflater.from(context).inflate(R.layout.spinner, null);
        TextView tv_workTitle = convertView.findViewById(R.id.tv_workTitle);
        tv_workTitle.setText(works.get(position).getWorkTitle());
        return convertView;
    }


}
