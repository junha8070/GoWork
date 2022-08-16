package com.example.gowork;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gowork.ViewModel.AuthViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_UserInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_UserInfo extends Fragment {
    private String TAG = "Edit_UserInfo";

    private MaterialToolbar materialToolbar;
    private TextInputEditText edt_id, edt_pw, edt_newpw, edt_repw, edt_name, edt_phone;
    private MaterialButton btn_finish;

    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    private String id, pw, newPw, rePw, name, phone;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Edit_UserInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_UserInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_UserInfo newInstance(String param1, String param2) {
        Edit_UserInfo fragment = new Edit_UserInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideBottomNavigation(true);

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
        View view = inflater.inflate(R.layout.fragment_edit__user_info, container, false);

        init(view);

        id = dbViewModel.getUserInfoLiveData().getValue().id;
        name = dbViewModel.getUserInfoLiveData().getValue().name;
        phone = dbViewModel.getUserInfoLiveData().getValue().phone;


        edt_id.setText(id);
        edt_name.setText(name);
        edt_phone.setText(phone);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_edit_UserInfo_to_settingFragment);
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String id = edt_id.getText().toString();
                String pw = edt_pw.getText().toString();
                String newPw = edt_newpw.getText().toString();
                String newRePw = edt_repw.getText().toString();
                String newName = edt_name.getText().toString();
                String newPhone = edt_phone.getText().toString();

                if (pw.isEmpty()) {
                    Toast.makeText(getContext(), "정보 변경을 위해서는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!newPw.isEmpty() || !newRePw.isEmpty()) {
                    if (newPw.equals(newRePw)) {
                        Toast.makeText(getContext(), "새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    UserDTO new_userDTO = new UserDTO();
                    FirebaseUser firebaseUser = authViewModel.getFirebaseUserLiveData().getValue();
//                    Log.d(TAG, authViewModel.getFirebaseUserLiveData().getValue().getUid());
                    new_userDTO.setId(id);
                    new_userDTO.setName(newName);
                    new_userDTO.setPhone(newPhone);
                    dbViewModel.updateUserInfo(firebaseUser, new_userDTO);
                    Navigation.findNavController(getView()).navigate(R.id.action_edit_UserInfo_to_settingFragment);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomNavigation(false);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void init(View view) {
        materialToolbar = view.findViewById(R.id.materialToolbar);
        edt_id = view.findViewById(R.id.edt_id);
        edt_pw = view.findViewById(R.id.edt_pw);
        edt_newpw = view.findViewById(R.id.edt_newpw);
        edt_repw = view.findViewById(R.id.edt_repw);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        btn_finish = view.findViewById(R.id.btn_finish);
    }
}