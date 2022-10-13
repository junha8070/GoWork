//package com.example.gowork.view_Community;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.gowork.R;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.card.MaterialCardView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Post_Photo_Adapter_Old extends RecyclerView.Adapter<Post_Photo_Adapter_Old.ViewHolder> {
//
//    @NonNull
//    @Override
//    public Post_Photo_Adapter_Old.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater
//                .from(parent.getContext())
//                .inflate(getViewSrc(viewType), parent, false);
//        return new ViewHolder(view, viewType);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Post_Photo_Adapter_Old.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return value.size();
//    }
//
//    // 아이템 뷰를 저장하는 뷰홀더 클래스.
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private int viewType;
//
//        public ViewHolder(@NonNull View itemView, int viewType) {
//            super(itemView);
//            this.viewType = viewType;
//        }
//
//        public void bind(HashMap<String, Object> value) {
//            if (viewType == TYPE_FIX) {
//                AddPhotoButton();
//            } else if (viewType == TYPE_NOT_FIX) {
//                ShowPhoto(value);
//            }
//        }
//
//        private void AddPhotoButton() {
//            MaterialCardView add_photo = itemView.findViewById(R.id.card_add_photo);
//        }
//
//        private void ShowPhoto(HashMap<String, Object> value) {
//            MaterialButton btn_del = itemView.findViewById(R.id.btn_del);
//            ImageView iv_photo = itemView.findViewById(R.id.iv_photo);
//            Glide.with(itemView).load(value.get("uri")).into(iv_photo);
//        }
//    }
//
//
//    // data
//    ArrayList<> value = new ArrayList<>();
//
//    public void submit(HashMap<String, Object> value) {
////        this.value = value;
//        notifyDataSetChanged();
//    }
//
//    private int TYPE_FIX = 101;
//    private int TYPE_NOT_FIX = 102;
//
//    // view type
//    private int getViewSrc(int viewType) {
//        // todo : connect xml
//        if (viewType == TYPE_FIX) {
//            return R.layout.item_post_photo_add;
//        } else {
//            return R.layout.item_rv_photo;
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if(value.get())
//    }
//}
