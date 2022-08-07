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

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    String TAG = "LoginFragment";

    ProgressDialog loadingDialog;

    private MaterialButton btn_login;
    private MaterialTextView tv_register;
    private TextInputEditText edt_id, edt_pw;

    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
//        dbViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DBViewModel.class);
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        init(view);

        hideBottomNavigation(true);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingDialog.show();

                String id = edt_id.getText().toString();
                String pw = edt_pw.getText().toString();

                if (id.isEmpty() && pw.isEmpty()) {
                    Toast.makeText(getContext(), "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                } else {
                    Log.d(TAG, "id: " + id + " pw: " + pw);
                    authViewModel.login(id, pw);

                    authViewModel.getLoginSuccess().observe(getActivity(), new Observer<Task>() {
                        @Override
                        public void onChanged(Task task) {
                            if (task.isComplete()) {
                                if (task.isSuccessful()) {
                                    authViewModel.getFirebaseUserLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
                                        @Override
                                        public void onChanged(FirebaseUser firebaseUser) {
                                            if (firebaseUser != null) {
                                                dbViewModel.userInfoLiveData(firebaseUser);
                                                dbViewModel.getUserInfoLiveData().observe(getActivity(), new Observer<UserDTO>() {
                                                    @Override
                                                    public void onChanged(UserDTO userDTO) {
                                                        if (userDTO != null) {
                                                            loadingDialog.cancel();
//                                                            Toast.makeText(getContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                                            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    loadingDialog.cancel();
                                    Toast.makeText(getContext(), "아이디와 비밀번호를 다시 한번 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loadingDialog.cancel();
                                Toast.makeText(getContext(), "로그인 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        return view;
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void init(View view) {
        loadingDialog = new ProgressDialog(getContext());
        edt_id = view.findViewById(R.id.edt_id);
        edt_pw = view.findViewById(R.id.edt_pw);
        btn_login = view.findViewById(R.id.btn_login);
        tv_register = view.findViewById(R.id.tv_register);
    }
}