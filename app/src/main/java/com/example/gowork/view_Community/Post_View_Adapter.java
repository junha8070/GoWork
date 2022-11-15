package com.example.gowork.view_Community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gowork.R;
import com.example.gowork.dto.CommentDTO;
import com.example.gowork.dto.PostDTO;
import com.example.gowork.dto.Post_View_DTO;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Post_View_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "Post_View_Adapter";

    private ArrayList<Post_View_DTO> myDataList = null;

    Post_View_Adapter(ArrayList<Post_View_DTO> dataList) {
        myDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == PostViewType.POST_VIEW_TYPE) {
            view = inflater.inflate(R.layout.item_post_view_top, parent, false);
            return new PostViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_post_view_comment, parent, false);
            return new CommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PostViewHolder) {
            Log.d(TAG, String.valueOf(position));
            if (myDataList.get(position).getPostViewType() == PostViewType.POST_VIEW_TYPE) {
                ((PostViewHolder) viewHolder).tv_title.setText(myDataList.get(position).getPostDTO().getTitle());
                ((PostViewHolder) viewHolder).tv_contents.setText(myDataList.get(position).getPostDTO().getContents());
                Glide.with(viewHolder.itemView).load(myDataList.get(position).getPostDTO().getPhoto()).into(((PostViewHolder) viewHolder).iv_photo);
            }
        } else if (viewHolder instanceof CommentViewHolder) {
            Log.d(TAG, "아답터 댓글: " + myDataList.get(position).getCommentDTO().getContents());
            ((CommentViewHolder) viewHolder).tv_name.setText(myDataList.get(position).getCommentDTO().getName());
            ((CommentViewHolder) viewHolder).tv_comment.setText(myDataList.get(position).getCommentDTO().getContents());
            ((CommentViewHolder) viewHolder).tv_time.setText(myDataList.get(position).getCommentDTO().getTime());
        }
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getPostViewType();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_contents;
        ImageView iv_photo;
        MaterialButton btn_up;
        MaterialButton btn_scrap;

        PostViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_contents = itemView.findViewById(R.id.tv_contents);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            btn_up = itemView.findViewById(R.id.btn_up);
            btn_scrap = itemView.findViewById(R.id.btn_scrap);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_name;
        TextView tv_comment;
        TextView tv_time;

        CommentViewHolder(View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }

}
