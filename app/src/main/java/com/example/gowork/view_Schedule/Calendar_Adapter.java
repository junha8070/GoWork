package com.example.gowork.view_Schedule;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gowork.R;
import com.example.gowork.view_Community.Community_Adapter;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Calendar_Adapter extends RecyclerView.Adapter<Calendar_Adapter.ViewHolder> {

    private String TAG = "Calendar_Adapter_TAG";

    int Year;
    int Month;
    private HashMap<Integer, Integer> days;
    ArrayList<HashMap<String, Object>> entire_arr_schedule;
    StringBuilder sb;

    int prev_select_pos = -1;
    int select_pos = -1;

    // 아이템 클릭 리스터 인터페이스
    interface OnItemClickListener {
        void onItemClick(View v, int position); // 뷰와 포지션 값
    }

    // 리스너 객체 참조 변수
    private OnItemClickListener mListener = null;

    // 리스너 객체 참조를 어댑터에 전달 매서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day;
        ImageView iv_indicator;

        ViewHolder(View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);
            iv_indicator = itemView.findViewById(R.id.iv_indicator);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    Calendar_Adapter(int Year, int Month, HashMap<Integer, Integer> days, ArrayList<HashMap<String, Object>> entire_arr_schedule) {
        this.Year = Year;
        this.Month = Month;
        this.days = days;
        this.entire_arr_schedule = entire_arr_schedule;
        sb = new StringBuilder();
        if (entire_arr_schedule != null) {
            for (int i = 0; i < entire_arr_schedule.size(); i++) {
                if (entire_arr_schedule.get(i).get("type").equals("everyday")) {
                    sb.append("everyday");
                } else if (entire_arr_schedule.get(i).get("type").equals("weekly")) {
                    sb.append(sortDate((HashMap<String, Boolean>) entire_arr_schedule.get(i).get("week_day")));
                    Log.d(TAG, sb.toString());
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_calender_day, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set<Integer> key = days.keySet();
        Collection<Integer> value = days.values();
        Iterator<Integer> key_iter = key.iterator();
        Iterator<Integer> value_iter = value.iterator();
        ArrayList arr_date = new ArrayList();
        ArrayList arr_day_of_week = new ArrayList();
        while (key_iter.hasNext()) {
            arr_date.add(key_iter.next());
        }
        while (value_iter.hasNext()) {
            arr_day_of_week.add(value_iter.next());
        }
        Log.d(TAG, String.valueOf(arr_date));
        if ((int) arr_day_of_week.get(position) != 0) {
            holder.tv_day.setText(String.valueOf(arr_date.get(position)));
            if(sb.toString().contains(convert_day_of_week_num_to_kor((int) arr_day_of_week.get(position)))){
                holder.iv_indicator.setVisibility(View.VISIBLE);
            }else if(sb.toString().contains("everyday")){
                holder.iv_indicator.setVisibility(View.VISIBLE);
            }
        } else {
            holder.itemView.setVisibility(View.GONE);
        }

        if (select_pos == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#00aca3"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#33333c"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev_select_pos = select_pos;
                select_pos = position;

                notifyItemChanged(prev_select_pos);
                notifyItemChanged(select_pos);

                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (mListener != null) {
                        mListener.onItemClick(view, position);
                        Log.d(TAG, String.valueOf(position));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public StringBuilder sortDate(HashMap<String, Boolean> dateValue) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> sort_date = new ArrayList<>();
        for (String key : dateValue.keySet()) {
            if (dateValue.get(key)) {
                switch (key) {
                    case "sun":
                        sort_date.add(0);
                        break;
                    case "mon":
                        sort_date.add(1);
                        break;
                    case "tue":
                        sort_date.add(2);
                        break;
                    case "wed":
                        sort_date.add(3);
                        break;
                    case "thu":
                        sort_date.add(4);
                        break;
                    case "fri":
                        sort_date.add(5);
                        break;
                    case "sat":
                        sort_date.add(6);
                        break;
                }
            }
        }
        Collections.sort(sort_date);
        for (Integer value : sort_date) {
            switch (value) {
                case 0:
                    sb.append("일");
                    sb.append(", ");
                    break;
                case 1:
                    sb.append("월");
                    sb.append(", ");
                    break;
                case 2:
                    sb.append("화");
                    sb.append(", ");
                    break;
                case 3:
                    sb.append("수");
                    sb.append(", ");
                    break;
                case 4:
                    sb.append("목");
                    sb.append(", ");
                    break;
                case 5:
                    sb.append("금");
                    sb.append(", ");
                    break;
                case 6:
                    sb.append("토");
                    sb.append(", ");
                    break;
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(", "));
        return sb;
    }

    private String convert_day_of_week_num_to_kor(int day_of_week) {
        switch (day_of_week) {
            case 1:
                return "월";
            case 2:
                return "화";
            case 3:
                return "수";
            case 4:
                return "목";
            case 5:
                return "금";
            case 6:
                return "토";
            case 7:
                return "일";
            default:
                return "미정";
        }
    }
}
