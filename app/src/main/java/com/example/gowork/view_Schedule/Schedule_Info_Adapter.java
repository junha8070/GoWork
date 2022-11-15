package com.example.gowork.view_Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gowork.R;
import com.example.gowork.dto.WorkInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class Schedule_Info_Adapter extends RecyclerView.Adapter<Schedule_Info_Adapter.ViewHolder> {

    String cal_info;
    ArrayList<WorkInfo> arr_workInfo;

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day, tv_place, tv_time, tv_address, tv_money, tv_none_working;

        ViewHolder(View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_place = itemView.findViewById(R.id.tv_place);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_none_working = itemView.findViewById(R.id.tv_none_working);

        }
    }

    @NonNull
    @Override
    public Schedule_Info_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_schedule_info, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Schedule_Info_Adapter.ViewHolder holder, int position) {
        holder.tv_day.setText(cal_info);
//        if (arr_workInfo.size() == 0) {
//            holder.itemView.setVisibility(View.GONE);
//            holder.tv_none_working.setVisibility(View.VISIBLE);
//        } else {
            holder.tv_place.setText(arr_workInfo.get(position).getPlace_name());
            holder.tv_time.setText(arr_workInfo.get(position).getStart_time() + " ~ " + arr_workInfo.get(position).getEnd_time());
            holder.tv_address.setText(arr_workInfo.get(position).getPlace_address());
            holder.tv_money.setText(arr_workInfo.get(position).getPay());
//        }


    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    Schedule_Info_Adapter(String cal_info, ArrayList<WorkInfo> arr_workInfo) {
        this.cal_info = cal_info;
        this.arr_workInfo = arr_workInfo;
    }

    @Override
    public int getItemCount() {
//        if (arr_workInfo.size() == 0) {
//            return 1;
//        }
        return arr_workInfo.size();
    }
}
