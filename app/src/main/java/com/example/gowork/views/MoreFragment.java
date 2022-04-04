package com.example.gowork.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gowork.R;
import com.example.gowork.viewmodel.MoreViewModel;
import com.example.gowork.viewmodel.WorkViewModel;

public class MoreFragment extends Fragment {

    private Button btn_logout;
    private MoreViewModel moreViewModel;
    private TextView tv_name, tv_email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moreViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(MoreViewModel.class);

        moreViewModel.getUserMutableLiveData().observe(this, firebaseUser -> {
            tv_name.setText(firebaseUser.getDisplayName());
            tv_email.setText(firebaseUser.getEmail());
        });

        moreViewModel.getLogOutMutableLiveData().observe(this, loggedOut -> {
            if(loggedOut){
                Navigation.findNavController(requireView()).navigate(R.id.action_loggedinFragment_to_loginRegisterFragment);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_more, container, false);

        btn_logout = view.findViewById(R.id.btn_logout);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_name);

        btn_logout.setOnClickListener(view1 -> moreViewModel.logOut());

        return view;
    }
}