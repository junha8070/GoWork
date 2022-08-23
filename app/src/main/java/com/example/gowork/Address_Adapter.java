package com.example.gowork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gowork.DTO.KakaoAddressResponse;

import java.util.ArrayList;

public class Address_Adapter extends RecyclerView.Adapter<Holder>{

    KakaoAddressResponse address_list;

    Address_Adapter(KakaoAddressResponse address_list) {
        this.address_list = address_list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_search_address, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tv_place_name.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getPlace_name());
        holder.tv_address.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getAddress_name());
        holder.tv_road_address.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getRoad_address_name());
        holder.tv_category.setText(address_list.getKakaoAddressDocumentsPojos().get(position).getCategory_name());
    }

    @Override
    public int getItemCount() {
        return address_list.getKakaoAddressDocumentsPojos().size();
    }
}

class Holder extends RecyclerView.ViewHolder {

    TextView tv_place_name, tv_address, tv_road_address, tv_category;

    public Holder(@NonNull View itemView) {
        super(itemView);

        tv_place_name = itemView.findViewById(R.id.tv_place_name);
        tv_address = itemView.findViewById(R.id.tv_address);
        tv_road_address = itemView.findViewById(R.id.tv_road_address);
        tv_category = itemView.findViewById(R.id.tv_category);
    }
}
