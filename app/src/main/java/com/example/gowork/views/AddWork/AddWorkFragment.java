package com.example.gowork.views.AddWork;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gowork.R;
import com.example.gowork.db.Work;
import com.example.gowork.viewmodel.WorkViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class AddWorkFragment extends Fragment {

    TextInputEditText edt_workTitle, edt_money;
    MaterialButtonToggleGroup btn_group, daySelectionGroup;
    MaterialButton btn_weekly, btn_everyday, btn_finish, btn_sun, btn_mon, btn_tue, btn_wed, btn_thu, btn_fri, btn_sat;
    DecimalFormat myFormatter;

    WorkViewModel workViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(WorkViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_work, container, false);
        init(view);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DB 추가
            }
        });

        // 요일 버튼 셋업
        btn_group.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.btn_weekly) {
                    daySelectionGroup.setVisibility(View.VISIBLE);
                    daySelectionGroup.clearChecked();
                } else if (checkedId == R.id.btn_everyday) {
                    daySelectionGroup.setVisibility(View.VISIBLE);
                    daySelectionGroup.check(R.id.btn_sun);
                    daySelectionGroup.check(R.id.btn_mon);
                    daySelectionGroup.check(R.id.btn_tue);
                    daySelectionGroup.check(R.id.btn_wed);
                    daySelectionGroup.check(R.id.btn_thu);
                    daySelectionGroup.check(R.id.btn_fri);
                    daySelectionGroup.check(R.id.btn_sat);
                }
            }
        });

        // 시급적는란 세자리 콤마
        edt_money.addTextChangedListener(new NumberTextWatcher(edt_money));

        // 추가하기 버튼을 눌렀을 때
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edt_workTitle.getText().toString();
                Boolean Sun = btn_sun.isChecked();
                Boolean Mon = btn_mon.isChecked();
                Boolean Tue = btn_tue.isChecked();
                Boolean Wed = btn_wed.isChecked();
                Boolean Thu = btn_thu.isChecked();
                Boolean Fri = btn_fri.isChecked();
                Boolean Sat = btn_sat.isChecked();
                int hourMoney = Integer.parseInt(edt_money.getText().toString().replace(",", ""));
                Work work = new Work(title, Sun, Mon, Tue, Wed, Thu, Fri, Sat, hourMoney);
                workViewModel.insert(work);
                Navigation.findNavController(view).navigate(R.id.moreFragment);
            }
        });
        return view;
    }

    public void init(View view) {
        edt_workTitle = view.findViewById(R.id.edt_workTitle);
        edt_money = view.findViewById(R.id.edt_money);
        btn_group = view.findViewById(R.id.btn_group);
        daySelectionGroup = view.findViewById(R.id.daySelectionGroup);
        btn_weekly = view.findViewById(R.id.btn_weekly);
        btn_everyday = view.findViewById(R.id.btn_everyday);
        btn_sun = view.findViewById(R.id.btn_sun);
        btn_mon = view.findViewById(R.id.btn_mon);
        btn_tue = view.findViewById(R.id.btn_tue);
        btn_wed = view.findViewById(R.id.btn_wed);
        btn_thu = view.findViewById(R.id.btn_thu);
        btn_fri = view.findViewById(R.id.btn_fri);
        btn_sat = view.findViewById(R.id.btn_sat);
        btn_finish = view.findViewById(R.id.btn_finish);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideBottomNavigation(true);
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
}