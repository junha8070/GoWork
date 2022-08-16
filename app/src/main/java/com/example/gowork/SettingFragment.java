package com.example.gowork;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gowork.ViewModel.AuthViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    private String TAG = "SettingFragment";

    private TextView tv_name, tv_email;
    private MaterialButton btn_add_work, btn_edit_work, btn_edit_profile, btn_logout;

    private HashMap<String, Object> loginInfo;

    //    FragmentSettingBinding settingBinding;
    AuthViewModel authViewModel;
    DBViewModel dbViewModel;

    String name = "1";
    String email = "2";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        dbViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DBViewModel.class);
        // new ViewModelProvider(requireActivity()).get(DBViewModel.class);로 해야 viewmodel 공유 가능
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
//        settingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        init(view);
//        UserDTO userDTO = dbViewModel.getUserInfoLiveData().getValue();
        dbViewModel.getUserInfoLiveData().observe(getActivity(), new Observer<UserDTO>() {
            @Override
            public void onChanged(UserDTO userDTO) {
                name = userDTO.name;
                email = userDTO.id;
                updateUI(name, email);
            }
        });
//        updateUI();

//        return settingBinding.getRoot();

        btn_add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_settingFragment_to_addWorkFragment);
            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_settingFragment_to_edit_UserInfo);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("알림")
                        .setMessage("로그아웃 할까요?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /*TODO : 로그아웃시 정보 삭제 다시 점검*/
                                loginInfo = new HashMap<>();
                                loginInfo.put("autologin", false);
                                loginInfo.put("id", null);
                                loginInfo.put("password", null);
                                authViewModel.setLoginInfo(loginInfo);
                                authViewModel.logout();
                                getActivity().finish();
//                                Navigation.findNavController(getView()).navigate(R.id.action_settingFragment_to_loginFragment);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.dismiss();
            }
        });
        return view;
    }

    public void updateUI(String name, String email) {
//        settingBinding.name.setText(name);
//        settingBinding.email.setText(email);
        tv_name.setText(name);
        tv_email.setText(email);
    }

    public void init(View view) {
        tv_name = view.findViewById(R.id.name);
        tv_email = view.findViewById(R.id.email);
        btn_add_work = view.findViewById(R.id.btn_add_work);
        btn_edit_work = view.findViewById(R.id.btn_edit_work);
        btn_edit_profile = view.findViewById(R.id.btn_edit_profile);
        btn_logout = view.findViewById(R.id.btn_logout);
    }
}