package com.example.gowork;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gowork.ViewModel.AuthViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private String TAG = "RegisterFragment";

    ProgressDialog loadingDialog;

    private TextInputEditText edt_name, edt_id, edt_pw, edt_repw, edt_phone;
    private MaterialButton btn_finish;

    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DBViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

//        hideBottomNavigation(true);

        init(view);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingDialog.show();

                String name = edt_name.getText().toString();
                String id = edt_id.getText().toString();
                String pw = edt_pw.getText().toString();
                String repw = edt_repw.getText().toString();
                String phone = edt_phone.getText().toString();

                UserDTO userDTO = new UserDTO(name, id, phone);

                if (name.isEmpty() || id.isEmpty() || pw.isEmpty() || repw.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getContext(), "빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                } else if (!pw.equals(repw)) {
                    Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                } else if (phone.length() != 11) {
                    Toast.makeText(getContext(), "전화번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                } else {
                    authViewModel.register(id, pw);
                    authViewModel.getRegisterSuccess().observe(getActivity(), new Observer<Task>() {
                        @Override
                        public void onChanged(Task task) {
                            if (task.isComplete()) {
                                loadingDialog.cancel();
                                if (task.isSuccessful()) {
                                    Log.d(TAG, task.getResult().toString());
                                    authViewModel.getFirebaseUserLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
                                        @Override
                                        public void onChanged(FirebaseUser firebaseUser) {
                                            if (firebaseUser != null) {
                                                Log.d(TAG, firebaseUser.getUid());
                                                Log.d(TAG, userDTO.id);
                                                dbViewModel.uploadUserInfo(firebaseUser, userDTO);
                                                Toast.makeText(getContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "회원가입 오류", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "회원가입 취소", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
        return view;
    }

//    public void hideBottomNavigation(Boolean bool) {
//        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
//        if (bool == true)
//            bottomNavigation.setVisibility(View.GONE);
//        else
//            bottomNavigation.setVisibility(View.VISIBLE);
//    }

    private void init(View view) {
        loadingDialog = new ProgressDialog(getContext());
        edt_name = view.findViewById(R.id.edt_name);
        edt_id = view.findViewById(R.id.edt_id);
        edt_pw = view.findViewById(R.id.edt_pw);
        edt_repw = view.findViewById(R.id.edt_repw);
        edt_phone = view.findViewById(R.id.edt_phone);
        btn_finish = view.findViewById(R.id.btn_finish);
    }
}