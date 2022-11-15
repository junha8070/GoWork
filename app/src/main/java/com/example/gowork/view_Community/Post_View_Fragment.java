package com.example.gowork.view_Community;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gowork.R;
import com.example.gowork.dto.CommentDTO;
import com.example.gowork.dto.PostDTO;
import com.example.gowork.dto.Post_View_DTO;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Post_View_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Post_View_Fragment extends Fragment {

    private String TAG = "Post_View_Fragment_TAG";

    // Layout
    private MaterialToolbar toolbar;
    private RecyclerView rv;
    private TextInputEditText edt_comment;
    private MaterialButton btn_send;

    // Toolbar
//    private AppBarConfiguration mAppBarConfiguration;

    private Post_View_Adapter post_view_adapter;
    private String postId;
    private String name;
    private String title;
    private String contents;
    private String photo;
    private String timestamp;
    private ArrayList arr_data;

    AuthViewModel authViewModel;
    DBViewModel dbViewModel;

    Fragment frg = null;
    FragmentTransaction ft;
//    private TextView tv_title, tv_contents;
//    private ImageView iv_photo;

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

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);


        dbViewModel.getCommentLiveData().observe(getActivity(), new Observer<ArrayList<CommentDTO>>() {
            @Override
            public void onChanged(ArrayList<CommentDTO> commentDTOS) {
                for (int i = 0; i < commentDTOS.size(); i++) {
                    Log.d(TAG, "댓글 가져오는 중 " + commentDTOS.get(i).getContents());
                    Post_View_DTO comment_data = new Post_View_DTO(null, commentDTOS.get(i), PostViewType.COMMENT_VIEW_TYPE);
                    arr_data.add(comment_data);
                    post_view_adapter = new Post_View_Adapter(arr_data);
                }
                rv.setAdapter(post_view_adapter);
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
        ft.commit();

        LinearLayoutManager manager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(manager);


        getParentFragmentManager().setFragmentResultListener("post_data", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                postId = result.getString("postId");
                name = result.getString("name");
                title = result.getString("title");
                contents = result.getString("contents");
                photo = result.getString("photo");
                timestamp = result.getString("timestamp");

                Log.d(TAG, "postId : " + postId + "\nname : " + name + "\ntitle : " + title + "\ncontents : " + contents + "\nphoto : " + photo);

                PostDTO real_postDTO = new PostDTO(postId, name, title, contents, Uri.parse(photo), timestamp);
                Post_View_DTO post_data = new Post_View_DTO(real_postDTO, null, PostViewType.POST_VIEW_TYPE);
                arr_data.add(post_data);

                dbViewModel.getComment(real_postDTO);

                post_view_adapter = new Post_View_Adapter(arr_data);

                rv.setAdapter(post_view_adapter);
                toolbar.setSubtitle(name + "님의 글");

                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = edt_comment.getText().toString();
                        if (comment.isEmpty()) {
                            Toast.makeText(getContext(), "댓글을 적어주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CommentDTO commentDTO =
                                new CommentDTO(postId, authViewModel.getFirebaseUserLiveData().getValue().getUid(), null, dbViewModel.getUserInfoLiveData().getValue().getName(), comment, getTime());
                        dbViewModel.uploadComment(authViewModel.getFirebaseUserLiveData().getValue(), commentDTO);
                    }
                });

                NavController navController = Navigation.findNavController(view);
                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
                NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
            }
        });


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "종료", Toast.LENGTH_SHORT).show();
        ft.detach(frg);
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "재시작", Toast.LENGTH_SHORT).show();
        ft.attach(frg);
    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        post_view_adapter = null;
//        arr_data = new ArrayList<>();
//    }

    private void init(View view) {
        frg = getActivity().getSupportFragmentManager().findFragmentById(getId());
        ft = getActivity().getSupportFragmentManager().beginTransaction();

        toolbar = view.findViewById(R.id.toolbar);
        rv = view.findViewById(R.id.rv);
        edt_comment = view.findViewById(R.id.edt_comment);
        btn_send = view.findViewById(R.id.btn_send);

        post_view_adapter = null;
        arr_data = new ArrayList<>();
//        tv_title = view.findViewById(R.id.tv_title);
//        tv_contents = view.findViewById(R.id.tv_contents);
//        iv_photo = view.findViewById(R.id.iv_photo);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    public void saveValue(String postId) {
        this.postId = postId;
    }

    public String getTime() {
        LocalDate now = LocalDate.now();         // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");         // 포맷 적용
        String formatedNow = now.format(formatter);

        return formatedNow;
    }

}