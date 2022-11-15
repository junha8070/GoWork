package com.example.gowork.view_Community;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gowork.R;
import com.example.gowork.dto.PostDTO;
import com.example.gowork.view_Setting.Address_Adapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Community_Adapter extends RecyclerView.Adapter<Community_Adapter.ViewHolder> {

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

    private ArrayList<PostDTO> dataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title, tv_contents, tv_day;
        private ImageView iv_photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_contents = itemView.findViewById(R.id.tv_contents);
            tv_day = itemView.findViewById(R.id.tv_day);
            iv_photo = itemView.findViewById(R.id.iv_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String postId = dataSet.get(getAdapterPosition()).getPostId();
//                    String name = dataSet.get(getAdapterPosition()).getName();
//                    String title = dataSet.get(getAdapterPosition()).getTitle();
//                    String contents = dataSet.get(getAdapterPosition()).getContents();
//                    String photo = String.valueOf(dataSet.get(getAdapterPosition()).getPhoto());
//                    String timestamp = dataSet.get(getAdapterPosition()).getTimestamp();
//
//                    Bundle result = new Bundle();
//                    result.putString("postId", postId);
//                    result.putString("name", name);
//                    result.putString("title", title);
//                    result.putString("contents", contents);
//                    result.putString("photo", photo);
//                    result.putString("timestamp", timestamp);
//
//                    getParentFragmentManager().setFragmentResult("post_data", result);
//
//                Navigation.findNavController(getView()).navigate(R.id.action_communityFragment_to_post_View_Fragment);
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

    public Community_Adapter(ArrayList<PostDTO> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public Community_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community_photo, parent, false);
        Community_Adapter.ViewHolder viewHolder = new Community_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Community_Adapter.ViewHolder holder, int position) {
        holder.tv_title.setText(dataSet.get(position).getTitle());
        holder.tv_contents.setText(dataSet.get(position).getContents());
        holder.tv_day.setText(dataSet.get(position).getTimestamp());
        Glide.with(holder.itemView).load(dataSet.get(position).getPhoto()).into(holder.iv_photo);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
