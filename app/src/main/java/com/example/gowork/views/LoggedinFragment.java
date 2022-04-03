package com.example.gowork.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.gowork.R;
import com.example.gowork.viewmodel.LoggedInViewModel;
import com.example.gowork.viewmodel.LoginRegisterViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoggedinFragment extends Fragment {

    private TextView tv_status;
    private Button btn_logout;

    private LoggedInViewModel loggedInViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        loggedInViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(LoggedInViewModel.class);
        loggedInViewModel.getUserMutableLiveData().observe(this, firebaseUser -> {
            if(firebaseUser != null){
                tv_status.setText(firebaseUser.getEmail());
            }
        });

        loggedInViewModel.getLogOutMutableLiveData().observe(this, loggedOut -> {
            if(loggedOut){
                Navigation.findNavController(requireView()).navigate(R.id.action_loggedinFragment_to_loginRegisterFragment);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loggedinfragment, container,false);

        tv_status = view.findViewById(R.id.tv_status);
        btn_logout = view.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(view1 -> loggedInViewModel.logOut());

        return view;
    }
}
