package com.example.gowork.view_Community;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.gowork.R;
import com.example.gowork.dto.PostDTO;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {
    private String TAG = "CommunityFragmentTAG";

    private FloatingActionButton btn_post;
    private RecyclerView rv_post;
    private MaterialButton btn_refresh;
//    private SwipeRefreshLayout sr_layout;

    AuthViewModel authViewModel;
    DBViewModel dbViewModel;

    Community_Adapter community_adapter;

    public CommunityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        hideBottomNavigation(false);

        init(view);

        btn_post.setOnClickListener(task -> {
            Navigation.findNavController(getView()).navigate(R.id.action_communityFragment_to_postFragment);
        });


//        Log.d("CommunityFragment", dbViewModel.postMutableLiveData().getValue().get(0).getTitle());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_post.setLayoutManager(linearLayoutManager);  // LayoutManager 설정

        community_adapter = new Community_Adapter(dbViewModel.postMutableLiveData().getValue());
        rv_post.setAdapter(community_adapter); // 어댑터 설정

        rv_post.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

//        sr_layout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                if(rv_post.getScrollY() == 0){
//                    sr_layout.isEnabled();
//                }
//            }
//        });


//        sr_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                dbViewModel.getPost();
//                dbViewModel.getGetPostTask().observe(getActivity(), new Observer<Task>() {
//                    @Override
//                    public void onChanged(Task task) {
//                        if(task.isSuccessful()){
//                            sr_layout.setRefreshing(false);
//                        }
//                    }
//                });
//            }
//        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbViewModel.getPost();
            }
        });

        dbViewModel.postMutableLiveData().observe(getActivity(), new Observer<ArrayList<PostDTO>>() {
            @Override
            public void onChanged(ArrayList<PostDTO> postDTOS) {
                Community_Adapter community_adapter = new Community_Adapter(dbViewModel.postMutableLiveData().getValue());
                rv_post.setAdapter(community_adapter);
                community_adapter.notifyDataSetChanged();

                community_adapter.setOnItemClickListener(new Community_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Log.d(TAG, String.valueOf(position));
                        String postId = dbViewModel.postMutableLiveData().getValue().get(position).getPostId();
                        String name = dbViewModel.postMutableLiveData().getValue().get(position).getName();
                        String title = dbViewModel.postMutableLiveData().getValue().get(position).getTitle();
                        String contents = dbViewModel.postMutableLiveData().getValue().get(position).getContents();
                        String photo = String.valueOf(dbViewModel.postMutableLiveData().getValue().get(position).getPhoto());
                        String timestamp = dbViewModel.postMutableLiveData().getValue().get(position).getTimestamp();

                        Bundle result = new Bundle();
                        result.putString("postId", postId);
                        result.putString("name", name);
                        result.putString("title", title);
                        result.putString("contents", contents);
                        result.putString("photo", photo);
                        result.putString("timestamp", timestamp);
                        getParentFragmentManager().setFragmentResult("post_data", result);

                        Navigation.findNavController(getView()).navigate(R.id.action_communityFragment_to_post_View_Fragment);
                    }
                });
            }
        });


        return view;
    }

    private void init(View view) {
//        sr_layout = view.findViewById(R.id.sr_layout);
        btn_refresh = view.findViewById(R.id.btn_refresh);
        rv_post = view.findViewById(R.id.rv_post);
        btn_post = view.findViewById(R.id.btn_post);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }
}