package com.example.gowork.view_Home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gowork.R;
import com.example.gowork.dto.PostDTO;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.view_Community.Community_Adapter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class Home_Rv_Adapter extends RecyclerView.Adapter<Home_Rv_Adapter.ViewHolder> {

    private String TAG = "Home_Rv_Adapter_TAG";

    ArrayList<WorkInfo> workInfo;
    ArrayList<HashMap<String, Object>> schedule;

    @NonNull
    @Override
    public Home_Rv_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_rv, parent, false);
        Home_Rv_Adapter.ViewHolder viewHolder = new Home_Rv_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Home_Rv_Adapter.ViewHolder holder, int position) {
        String type = String.valueOf(schedule.get(position).get("type"));
        if (type.equals("everyday")) {
            holder.tv_date.setText("매일");
            holder.tv_workName.setText(workInfo.get(position).getPlace_name());
            holder.tv_time.setText(workInfo.get(position).getStart_time() + " ~ " + workInfo.get(position).getEnd_time());
            holder.tv_workPlace.setText(workInfo.get(position).getPlace_address());
            holder.tv_pay.setText(workInfo.get(position).getPay());
        } else if (type.equals("weekly")) {
            Log.d(TAG, String.valueOf(schedule.get(position).get("week_day").getClass()));
            HashMap<String, Boolean> weekly_schedule = (HashMap<String, Boolean>) schedule.get(position).get("week_day");
            StringBuilder sb = sortDate(weekly_schedule);
            holder.tv_date.setText(sb);
            String sbToString = sb.toString();
            Log.d(TAG, todayDate());
            holder.tv_workName.setText(workInfo.get(position).getPlace_name());
            holder.tv_time.setText(workInfo.get(position).getStart_time() + " ~ " + workInfo.get(position).getEnd_time());
            holder.tv_workPlace.setText(workInfo.get(position).getPlace_address());
            holder.tv_pay.setText(workInfo.get(position).getPay());
        }
    }

    @Override
    public int getItemCount() {
        return workInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_workName, tv_date, tv_time, tv_workPlace, tv_pay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_workName = itemView.findViewById(R.id.tv_workName);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_workPlace = itemView.findViewById(R.id.tv_workPlace);
            tv_pay = itemView.findViewById(R.id.tv_pay);
        }
    }

    public Home_Rv_Adapter(ArrayList<WorkInfo> workInfo, ArrayList<HashMap<String, Object>> schedule) {
        this.workInfo = workInfo;
        this.schedule = schedule;
        Log.d(TAG, "workInfo" + workInfo.size());
        for(int i =0;i<schedule.size();i++){
            if(schedule.get(i).get("type").equals("weekly")){
             Log.d(TAG, "schedule" + schedule.get(i).get("week_day"));
            }
        }

    }

    public String todayDate() {
        LocalDate date = LocalDate.now();
        System.out.println(date);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
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
}
