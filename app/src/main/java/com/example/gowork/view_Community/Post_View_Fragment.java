package com.example.gowork.view_Community;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gowork.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Post_View_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Post_View_Fragment extends Fragment {

    private MaterialToolbar toolbar;
    private TextView tv_title, tv_contents;
    private ImageView iv_photo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Post_View_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Post_View_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Post_View_Fragment newInstance(String param1, String param2) {
        Post_View_Fragment fragment = new Post_View_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getParentFragmentManager().setFragmentResultListener("post_data", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String postId = result.getString("postId");
                String name = result.getString("name");
                String title = result.getString("title");
                String contents = result.getString("contents");
                String photo = result.getString("photo");
                String timestamp = result.getString("timestamp");

                toolbar.setSubtitle(name+"님의 글");
                tv_title.setText(title);
                tv_contents.setText(contents);
                Glide.with(getContext()).load(Uri.parse(photo)).into(iv_photo);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post__view_, container, false);

        hideBottomNavigation(true);

        init(view);


        return view;
    }

    private void init(View view){
        toolbar = view.findViewById(R.id.toolbar);
        tv_title = view.findViewById(R.id.tv_title);
        tv_contents = view.findViewById(R.id.tv_contents);
        iv_photo = view.findViewById(R.id.iv_photo);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }
}