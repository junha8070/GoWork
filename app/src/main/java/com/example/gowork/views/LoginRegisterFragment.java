package com.example.gowork.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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

public class LoginRegisterFragment extends Fragment {

    private Button btn_login, btn_register, btn_next;
    private EditText edt_id, edt_pw, edt_repw, edt_name, edt_phone;
    private TextInputLayout input_layout_pw, input_layout_repw;
    private LinearLayout layout_email, layout_password, layout_name, layout_phone;

    private Animation animation;

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private LoginRegisterViewModel loginRegisterViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_loggedinFragment);
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

        hideBottomNavigation(true);

        input_layout_pw.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        input_layout_repw.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

        animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        layout_email.setVisibility(View.VISIBLE);
        layout_email.setAnimation(animation);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRegisterViewModel.isEmailExist(edt_id.getText().toString());
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_id.getText().toString();
                String password = edt_pw.getText().toString();

                Toast.makeText(getContext(), email+password, Toast.LENGTH_SHORT).show();

                if(email.length() > 0 && password.length() > 0){
                    loginRegisterViewModel.register(email, password);
                }

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_id.getText().toString();
                String password = edt_pw.getText().toString();

                if(email.length() > 0 && password.length() > 0){
                    loginRegisterViewModel.login(email, password);
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
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottom_nav);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void init(View view){
        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);
        btn_next = view.findViewById(R.id.btn_next);
        edt_id = view.findViewById(R.id.edt_id);
        input_layout_pw = view.findViewById(R.id.input_layout_pw);
        edt_pw = view.findViewById(R.id.edt_pw);
        input_layout_repw = view.findViewById(R.id.input_layout_repw);
        edt_repw = view.findViewById(R.id.edt_pw_check);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        layout_email = view.findViewById(R.id.layout_email);
    }

}