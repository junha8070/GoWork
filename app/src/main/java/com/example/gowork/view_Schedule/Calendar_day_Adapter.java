package com.example.gowork.view_Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gowork.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Calendar_day_Adapter extends BaseAdapter {

    private Context context;
    int Year;
    int Month;
    private HashMap<Integer, Integer> days;

    public Calendar_day_Adapter(Context context, int Year, int Month, HashMap<Integer, Integer> days){
        this.context = context;
        this.Year = Year;
        this.Month = Month;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return this.days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;
        //초기에는 convertview가 null값을 가짐
        //ViewHolder에 등록해서 화면에 다시 layout을 그리는 일 방지
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calender_day, parent, false);

            TextView tv_day = (TextView) view.findViewById(R.id.tv_day);

            holder.tv_day = tv_day;

            view.setTag(holder);
        }
        //null이 아니면 이미 viewholder에 등록되있어서 Tag를 가져옴
        else {
            holder = (ViewHolder) view.getTag();
        }

        days.keySet();
        for(Integer key : days.keySet()){
            holder.tv_day.setText(key);
        }

        //화면 출력
        return view;
    }

    static class ViewHolder {
        private TextView tv_day;
    }
}
