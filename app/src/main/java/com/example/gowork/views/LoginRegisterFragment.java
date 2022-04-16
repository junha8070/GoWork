package com.example.gowork.views;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gowork.R;
import com.example.gowork.viewmodel.LoginRegisterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRegisterFragment extends Fragment {

    private Button btn_SignInOrUP, btn_login, btn_next, btn_register;
    private EditText edt_id, edt_pw, edt_repw, edt_name, edt_phone;
    private TextInputLayout input_layout_pw, input_layout_repw;
    private LinearLayout layout_email, layout_password, layout_name, layout_phone;

    private Animation animation;

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private LoginRegisterViewModel loginRegisterViewModel;

    private ProgressDialog mDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_homeFragment);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_register, container, false);

        init(view);
        mDialog = new ProgressDialog(getContext());

        hideBottomNavigation(true);

        input_layout_pw.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        input_layout_repw.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

        animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        layout_email.setVisibility(View.VISIBLE);
        layout_email.setAnimation(animation);

        // 핸드폰 번호 자릿수 마다 하이픈(-) 넣어줌
        edt_phone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        edt_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRegisterViewModel.isEmailExist(edt_id.getText().toString());
            }
        });

        btn_SignInOrUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_id.getText().toString();
                String password = edt_pw.getText().toString();

                if (email.length() > 0 && password.length() > 0) {
                    loginRegisterViewModel.login(email, password);
                }
                mDialog.setMessage("확인중입니다");
                mDialog.show();
                loginRegisterViewModel.isEmailExist(edt_id.getText().toString());
                loginRegisterViewModel.getIsEmailExistData().observe(getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean != null) {
                            mDialog.dismiss();
                            if (aBoolean) {
                                input_layout_pw.setVisibility(View.VISIBLE);
                                btn_SignInOrUP.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            } else {
                                input_layout_pw.setVisibility(View.VISIBLE);
                                input_layout_repw.setVisibility(View.VISIBLE);
                                btn_SignInOrUP.setVisibility(View.GONE);
                                btn_next.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_id.getText().toString();
                String password = edt_pw.getText().toString();

                Toast.makeText(getContext(), email + password, Toast.LENGTH_SHORT).show();

                if (email.length() > 0 && password.length() > 0) {
                    loginRegisterViewModel.login(email, password);
                }

            }
        });

        btn_next.setOnClickListener(view1 -> {
            if (!(edt_pw.getText().toString().equals(edt_repw.getText().toString()))) {
                Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            loginRegisterViewModel.login(edt_id.getText().toString(), edt_pw.getText().toString());
            loginRegisterViewModel.getUserData().observe(getActivity(), new Observer<FirebaseUser>() {
                @Override
                public void onChanged(FirebaseUser firebaseUser) {
                    if (firebaseUser != null) {
                        Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_homeFragment);
                        return;
                    }
                }
            });
            layout_name.setVisibility(View.VISIBLE);
            if (edt_name.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "이름을 입렫해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if (layout_name.getVisibility() == View.VISIBLE && !edt_name.getText().toString().isEmpty()) {
                layout_phone.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.GONE);
                btn_register.setVisibility(View.VISIBLE);
            }
        });

        btn_register.setOnClickListener(register -> {
            String email = edt_id.getText().toString();
            String password = edt_pw.getText().toString();
            String name = edt_name.getText().toString();
            String phoneNum = edt_phone.getText().toString();

            Toast.makeText(getContext(), email + password, Toast.LENGTH_SHORT).show();

            if (email.length() > 0 && password.length() > 0) {
                loginRegisterViewModel.register(email, password);
                loginRegisterViewModel.uploadUserInfo(email, name, phoneNum);
//                loginRegisterViewModel.getUserInfoMutableLiveData().observe(getActivity(), new Observer<FirebaseFirestore>() {
//                    @Override
//                    public void onChanged(FirebaseFirestore firebaseFirestore) {
//
//                    }
//                });
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
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottom_nav);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void init(View view) {
        btn_SignInOrUP = view.findViewById(R.id.btn_SignInOrUP);
        btn_login = view.findViewById(R.id.btn_login);
        btn_next = view.findViewById(R.id.btn_next);
        btn_register = view.findViewById(R.id.btn_register);
        edt_id = view.findViewById(R.id.edt_id);
        input_layout_pw = view.findViewById(R.id.input_layout_pw);
        edt_pw = view.findViewById(R.id.edt_pw);
        input_layout_repw = view.findViewById(R.id.input_layout_repw);
        edt_repw = view.findViewById(R.id.edt_pw_check);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        layout_email = view.findViewById(R.id.layout_email);
        layout_password = view.findViewById(R.id.layout_password);
        input_layout_pw = view.findViewById(R.id.input_layout_pw);
        input_layout_repw = view.findViewById(R.id.input_layout_repw);
        layout_name = view.findViewById(R.id.layout_name);
        layout_phone = view.findViewById(R.id.layout_phone);

    }

}