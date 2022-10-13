package com.example.gowork.view_Community;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gowork.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;

public class Post_Photo_Adapter extends RecyclerView.Adapter<Post_Photo_Adapter.ViewHolder> {

    private ArrayList<Uri> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialButton btn_del;
        private ImageView iv_photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_del = itemView.findViewById(R.id.btn_del);
            iv_photo = itemView.findViewById(R.id.iv_photo);
        }
    }

    public Post_Photo_Adapter(ArrayList<Uri> dataSet) {
        this.dataSet = dataSet;
        Log.d("Post_Photo_Adapter", String.valueOf(dataSet.get(0)));
    }

    @NonNull
    @Override
    public Post_Photo_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_photo, parent, false);
        Post_Photo_Adapter.ViewHolder viewHolder = new Post_Photo_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Post_Photo_Adapter.ViewHolder holder, int position) {
        Log.d("Post_Photo_Adapter", String.valueOf(dataSet.get(position)));

        Glide.with(holder.itemView).load(dataSet.get(position)).into(holder.iv_photo);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
