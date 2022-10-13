package com.example.gowork.view_Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gowork.R;

import java.util.ArrayList;

public class Calendar_Adapter extends RecyclerView.Adapter<Calendar_Adapter.ViewHolder>{

    private ArrayList<Calendar_day_Adapter> mData = null ;


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        private GridView gv_day;

        ViewHolder(View itemView) {
            super(itemView) ;
            gv_day = itemView.findViewById(R.id.gv_day);
            gv_day.setAdapter(mData.get(0));

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    Calendar_Adapter(ArrayList<Calendar_day_Adapter> list) {
        mData = list ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_calendar, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
