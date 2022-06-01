package com.example.gowork.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gowork.MyService;
import com.example.gowork.R;
import com.example.gowork.viewmodel.HomeViewModel;
import com.example.gowork.viewmodel.WorkViewModel;
import com.google.android.material.button.MaterialButton;


public class HomeFragment extends Fragment {

    private TextView tv_money, tv_workplace, tv_startTime, tv_endTime;
    private MaterialButton btn_start;
    private HomeViewModel homeViewModel;
    private String CurrentTime = null;
    private Boolean Work_State = false;

    public Intent serviceIntent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(HomeViewModel.class);
        homeViewModel.getTime().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("HomeFragment", s);
                CurrentTime = s;
                if (Work_State) {
                    tv_startTime.setText(CurrentTime.substring(11) + "에 출근하셨습니다");
                } else {
                    tv_endTime.setText(CurrentTime.substring(11) + "에 퇴근하셨습니다");
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Work_State) {
                    onStopForegroundService(view);
                    serviceIntent = new Intent(getContext(), MyService.class);
                    Work_State = false;
                    homeViewModel.currentTime();
                    btn_start.setText("출퇴근하기");
                    tv_endTime.setVisibility(View.VISIBLE);
                } else {
                    onStartForegroundService(view);
                    Work_State = true;
                    homeViewModel.currentTime();
                    btn_start.setText("근무중");
                    tv_startTime.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    private void init(View view) {
        tv_money = view.findViewById(R.id.tv_money);
        tv_startTime = view.findViewById(R.id.tv_startTime);
        tv_endTime = view.findViewById(R.id.tv_endTime);
        tv_workplace = view.findViewById(R.id.tv_workplace);
        btn_start = view.findViewById(R.id.btn_start);
    }

    public void onStartService(View view) {
        Intent intent = new Intent(getContext(), MyService.class);
        getActivity().startService(intent);
    }

    public void onStopService(View view) {
        Intent intent = new Intent(getContext(), MyService.class);
        getActivity().startService(intent);
    }

    public void onStartForegroundService(View view) {
        Intent intent = new Intent(getContext(), MyService.class);
        intent.setAction("startForeground");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent);
        } else {
            getActivity().startService(intent);
        }
    }

    public void onStopForegroundService(View view) {
        Intent intent = new Intent(getContext(), MyService.class);
        intent.setAction("stopForeground");
        getActivity().stopService(intent);
    }
}