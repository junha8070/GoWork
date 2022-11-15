package com.example.gowork.view_Pay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gowork.R;
import com.example.gowork.dto.PayRvDTO;

import java.util.ArrayList;
import java.util.HashMap;

public class Pay_Adapter extends RecyclerView.Adapter<Pay_Adapter.ViewHolder> {

    private ArrayList<PayRvDTO> timeValues = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day, tv_workTitle, tv_money, tv_startTime, tv_endTime, tv_hours;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_workTitle = itemView.findViewById(R.id.tv_workTitle);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_startTime = itemView.findViewById(R.id.tv_startTime);
            tv_endTime = itemView.findViewById(R.id.tv_endTime);
            tv_hours = itemView.findViewById(R.id.tv_hours);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    Pay_Adapter(ArrayList<PayRvDTO> timeValues) {
        this.timeValues = timeValues;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public Pay_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_pay, parent, false);
        Pay_Adapter.ViewHolder vh = new Pay_Adapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(Pay_Adapter.ViewHolder holder, int position) {
        String title = timeValues.get(position).getWorkTitle();
        String date = timeValues.get(position).getDate();
        String pay = timeValues.get(position).getPay();
        String startTime = timeValues.get(position).getStart_time();
        String finishTime = timeValues.get(position).getFinish_time();
        String duration = timeValues.get(position).getTime_duration();

        holder.tv_workTitle.setText(title);
        holder.tv_day.setText(date);
        holder.tv_money.setText(pay);
        holder.tv_startTime.setText(startTime);
        holder.tv_endTime.setText(finishTime);
        holder.tv_hours.setText(duration);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return timeValues.size();
    }

}
