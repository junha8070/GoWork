package com.example.gowork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gowork.DTO.KakaoAddressResponse;

import java.util.ArrayList;

public class Address_Adapter extends RecyclerView.Adapter<Address_Adapter.ViewHolder> {

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

    KakaoAddressResponse address_list;

    Address_Adapter(KakaoAddressResponse address_list) {
        this.address_list = address_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_search_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tv_place_name.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getPlace_name());
        viewHolder.tv_address.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getAddress_name());
        viewHolder.tv_road_address.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getRoad_address_name());
        viewHolder.tv_category.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getCategory_name());
    }

    @Override
    public int getItemCount() {
        return address_list.getKakaoAddressDocumentsPojos().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_place_name, tv_address, tv_road_address, tv_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_place_name = itemView.findViewById(R.id.tv_place_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_road_address = itemView.findViewById(R.id.tv_road_address);
            tv_category = itemView.findViewById(R.id.tv_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    Toast.makeText(itemView.getContext(), position, Toast.LENGTH_SHORT).show();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, position);
                        }
                    }
                }
            });
        }
    }
}
