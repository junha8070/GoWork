package com.example.gowork.view_Community;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gowork.R;
import com.example.gowork.dto.PostDTO_Upload;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private String TAG = "PostFragmentTAG";

    private TextInputEditText edt_title, edt_content;
    private RecyclerView rv_photo;
    private MaterialButton btn_gallery, btn_camera, btn_finish;

    private Post_Photo_Adapter postPhotoAdapter;

    private ArrayList<String> data;
    private ArrayList<Uri> uri_photo_data;

    AuthViewModel authViewModel;
    DBViewModel dbViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        hideBottomNavigation(true);

        init(view);

        data = new ArrayList<>();
        uri_photo_data = new ArrayList<>();

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelfPermission();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                image_launcher.launch(intent);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    camera_launcher.launch(takePictureIntent);
                }
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edt_title.getText().toString();
                String content = edt_content.getText().toString();

                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "제목은 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocalDate now = LocalDate.now();         // 포맷 정의
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");         // 포맷 적용
                String formatedNow = now.format(formatter);

//                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                System.out.println(timestamp); // 생성한 timestamp 출력

//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                System.out.println(sdf.format(timestamp)); // format을 사용해 출력

                PostDTO_Upload postData = new PostDTO_Upload(dbViewModel.getUserInfoLiveData().getValue().getName(), title, content, uri_photo_data.get(0), formatedNow);

                dbViewModel.uploadPost(authViewModel.getFirebaseUserLiveData().getValue(), postData);

                Navigation.findNavController(getView()).navigate(R.id.action_postFragment_to_communityFragment);
                hideBottomNavigation(false);
            }
        });

        return view;
    }

    ActivityResultLauncher<Intent> image_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.e(TAG, "result : " + result.getData());
                        Intent intent = result.getData();
                        Log.e(TAG, "intent : " + intent.getData());
                        Uri uri = intent.getData();
                        Log.e(TAG, "uri : " + uri);

                        data.add(String.valueOf(uri));
                        uri_photo_data.add(uri);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rv_photo.setLayoutManager(linearLayoutManager);  // LayoutManager 설정

                        postPhotoAdapter = new Post_Photo_Adapter(uri_photo_data);
                        rv_photo.setAdapter(postPhotoAdapter); // 어댑터 설정
                    }
                }
            });

    ActivityResultLauncher<Intent> camera_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        Uri uri = getImageUri(getContext(), imageBitmap);

                        data.add(String.valueOf(uri));
                        uri_photo_data.add(uri);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rv_photo.setLayoutManager(linearLayoutManager);  // LayoutManager 설정

                        postPhotoAdapter = new Post_Photo_Adapter(uri_photo_data);
                        rv_photo.setAdapter(postPhotoAdapter); // 어댑터 설정
                    }
                }
            });

    public void checkSelfPermission() {

        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }


        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" "), 1);
        } else {
            // 모두 허용 상태
            Toast.makeText(getContext(), "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void init(View view) {
        edt_title = view.findViewById(R.id.edt_title);
        edt_content = view.findViewById(R.id.edt_content);
        rv_photo = view.findViewById(R.id.rv_photo);
        btn_gallery = view.findViewById(R.id.btn_gallery);
        btn_camera = view.findViewById(R.id.btn_camera);
        btn_finish = view.findViewById(R.id.btn_finish);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }


}