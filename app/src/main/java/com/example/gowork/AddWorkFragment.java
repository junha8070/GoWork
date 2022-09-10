package com.example.gowork;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gowork.dto.WorkInfo;
import com.example.gowork.dto.WorkSchedule_Month;
import com.example.gowork.dto.WorkSchedule_Week;
import com.example.gowork.dto.WorkSchedule_day;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddWorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddWorkFragment extends Fragment {

    String TAG = "AddWorkFragmentTAG";

    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    TextInputEditText edt_place_name, edt_work_location, edt_pay;
    Spinner sp_date;
    LinearLayout layout_every_month, layout_every_week, layout_last_day, layout_holiday;
    MaterialButtonToggleGroup group_btn_schedule, group_btn_date, group_btn_last_day, group_btn_holiday;
    MaterialButton btn_finish;

    String[] res_date;
    ArrayAdapter dateAdapter;

    HashMap<String, Object> schedule_data;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddWorkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddWorkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddWorkFragment newInstance(String param1, String param2) {
        AddWorkFragment fragment = new AddWorkFragment();
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        getParentFragmentManager().setFragmentResultListener("find_address_result", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String place_name = result.getString("place_name");
                String address = result.getString("address");

                edt_place_name.setText(place_name);
                edt_work_location.setText(address);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_work, container, false);

        init(view);

        layout_every_month.setVisibility(View.GONE);
        layout_every_week.setVisibility(View.GONE);
//        layout_leap_year.setVisibility(View.GONE);
        layout_last_day.setVisibility(View.GONE);

        res_date = getResources().getStringArray(R.array.date);
        dateAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, res_date);
        sp_date.setAdapter(dateAdapter);

        edt_work_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_addWorkFragment_to_searchAddressFragment);
            }
        });

        group_btn_schedule.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (group.getCheckedButtonId()) {
                    case R.id.button_every_month:
                        layout_every_week.setVisibility(View.GONE);
//                        layout_leap_year.setVisibility(View.GONE);
                        layout_last_day.setVisibility(View.GONE);
                        layout_every_month.setVisibility(View.VISIBLE);
                        break;
                    case R.id.button_every_week:
                        sp_date.setSelection(0);
                        layout_every_week.setVisibility(View.VISIBLE);
//                        layout_leap_year.setVisibility(View.GONE);
                        layout_last_day.setVisibility(View.GONE);
                        layout_every_month.setVisibility(View.GONE);
                        break;
//                    case R.id.button_every_day:
//                        layout_every_week.setVisibility(View.GONE);
//                        layout_leap_year.setVisibility(View.GONE);
//                        layout_last_day.setVisibility(View.GONE);
//                        layout_every_month.setVisibility(View.GONE);
//                        break;
                    default:
                        sp_date.setSelection(0);
                        layout_every_week.setVisibility(View.GONE);
//                        layout_leap_year.setVisibility(View.GONE);
                        layout_last_day.setVisibility(View.GONE);
                        layout_every_month.setVisibility(View.GONE);
                }
            }
        });

        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "i값 : " + position + "l값 : " + l);
                if (position == 28 || position == 29 || position == 30) {
//                    layout_leap_year.setVisibility(View.VISIBLE);
                    layout_last_day.setVisibility(View.VISIBLE);
                }
//                else if (position == 29 || position == 30) {
//                    layout_leap_year.setVisibility(View.GONE);
//                    layout_last_day.setVisibility(View.VISIBLE);
//                }
                else {
//                    layout_leap_year.setVisibility(View.GONE);
                    layout_last_day.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schedule_data = new HashMap<>();
//                Log.d(TAG, String.valueOf(group_btn_day.getCheckedButtonIds().));
                Boolean state = false;
                String place_name = edt_place_name.getText().toString();
                String place_address = edt_work_location.getText().toString();
                String pay = edt_pay.getText().toString();

                Boolean[] work_schedule = {false, false, false};
                String str_work_day;
                int work_day;

                HashMap<String, Boolean> work_date;
                Boolean last_day = null;
                Boolean holiday = null;
                WorkInfo workInfo;
//
                switch (group_btn_holiday.getCheckedButtonId()) {
                    case R.id.btn_holiday_do:
                        schedule_data.put("holiday", true);
//                        holiday = true;
                        break;
                    case R.id.btn_holiday_not_do:
                        schedule_data.put("holiday", false);
//                        holiday = false;
                        break;
                    default:
                        Toast.makeText(getContext(), "공휴일 근무 여부를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                }
//
                switch (group_btn_schedule.getCheckedButtonId()) {
                    case R.id.button_every_month:
                        schedule_data.put("schedule", "month");
                        work_schedule[0] = true;
                        str_work_day = sp_date.getSelectedItem().toString();

                        if (str_work_day.equals("29일") || str_work_day.equals("30일") || str_work_day.equals("31일")) {
                            switch (group_btn_last_day.getCheckedButtonId()) {
                                case R.id.btn_last_day_do:
                                    schedule_data.put("last_day", true);
                                    last_day = true;
                                    break;
                                case R.id.btn_last_day_not_do:
                                    schedule_data.put("last_day", false);
                                    last_day = false;
                                    break;
                            }
                        }

                        work_day = Integer.parseInt(str_work_day.replace("일", ""));
                        schedule_data.put("work_day", work_day);
//                        WorkSchedule_Month workSchedule_month = new WorkSchedule_Month(work_day, last_day, holiday);
                        workInfo = new WorkInfo(place_name, place_address, pay);
                        dbViewModel.setUploadWorkInfo(authViewModel.getFirebaseUserLiveData().getValue(), workInfo, schedule_data);
                        Navigation.findNavController(getView()).navigate(R.id.action_addWorkFragment_to_settingFragment);

                        break;

                    case R.id.button_every_week:
                        schedule_data.put("schedule", "week");
                        work_schedule[1] = true;

                        work_date = new HashMap<>();
                        switch (group_btn_date.getCheckedButtonId()) {
                            case R.id.btn_sun:
                                work_date.put("sun", true);
                                work_date.put("mon", false);
                                work_date.put("tue", false);
                                work_date.put("wed", false);
                                work_date.put("thu", false);
                                work_date.put("fri", false);
                                work_date.put("sat", false);
                                break;
                            case R.id.btn_mon:
                                work_date.put("sun", false);
                                work_date.put("mon", true);
                                work_date.put("tue", false);
                                work_date.put("wed", false);
                                work_date.put("thu", false);
                                work_date.put("fri", false);
                                work_date.put("sat", false);
                                break;
                            case R.id.btn_tue:
                                work_date.put("sun", false);
                                work_date.put("mon", false);
                                work_date.put("tue", true);
                                work_date.put("wed", false);
                                work_date.put("thu", false);
                                work_date.put("fri", false);
                                work_date.put("sat", false);
                                break;
                            case R.id.btn_wed:
                                work_date.put("sun", false);
                                work_date.put("mon", false);
                                work_date.put("tue", false);
                                work_date.put("wed", true);
                                work_date.put("thu", false);
                                work_date.put("fri", false);
                                work_date.put("sat", false);
                                break;
                            case R.id.btn_thu:
                                work_date.put("sun", false);
                                work_date.put("mon", false);
                                work_date.put("tue", false);
                                work_date.put("wed", false);
                                work_date.put("thu", true);
                                work_date.put("fri", false);
                                work_date.put("sat", false);
                                break;
                            case R.id.btn_fri:
                                work_date.put("sun", false);
                                work_date.put("mon", false);
                                work_date.put("tue", false);
                                work_date.put("wed", false);
                                work_date.put("thu", false);
                                work_date.put("fri", true);
                                work_date.put("sat", false);
                                break;
                            case R.id.btn_sat:
                                work_date.put("sun", false);
                                work_date.put("mon", false);
                                work_date.put("tue", false);
                                work_date.put("wed", false);
                                work_date.put("thu", false);
                                work_date.put("fri", false);
                                work_date.put("sat", true);
                                break;
                        }

                        schedule_data.put("work_date", work_date);
//                        WorkSchedule_Week workSchedule_week = new WorkSchedule_Week(work_date, holiday);
                        workInfo = new WorkInfo(place_name, place_address, pay);
                        dbViewModel.setUploadWorkInfo(authViewModel.getFirebaseUserLiveData().getValue(), workInfo, schedule_data);
                        Navigation.findNavController(getView()).navigate(R.id.action_addWorkFragment_to_settingFragment);

                        break;

                    case R.id.button_every_day:
                        schedule_data.put("schedule", "Everyday");
                        work_schedule[2] = true;

//                        WorkSchedule_day workSchedule_day = new WorkSchedule_day(holiday);
                        workInfo = new WorkInfo(place_name, place_address, pay);
                        dbViewModel.setUploadWorkInfo(authViewModel.getFirebaseUserLiveData().getValue(), workInfo, schedule_data);
                        Navigation.findNavController(getView()).navigate(R.id.action_addWorkFragment_to_settingFragment);

                        break;

                    default:
                        Toast.makeText(getContext(), "근무일 설정을 해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                }


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
        edt_place_name = view.findViewById(R.id.edt_place_name);
        edt_work_location = view.findViewById(R.id.edt_work_location);
        edt_pay = view.findViewById(R.id.edt_pay);
        group_btn_schedule = view.findViewById(R.id.group_btn_schedule);
        sp_date = view.findViewById(R.id.sp_date);
        layout_every_month = view.findViewById(R.id.layout_every_month);
        layout_every_week = view.findViewById(R.id.layout_every_week);
//        layout_leap_year = view.findViewById(R.id.layout_leap_year);
        layout_last_day = view.findViewById(R.id.layout_last_day);
        layout_holiday = view.findViewById(R.id.layout_holiday);
        group_btn_date = view.findViewById(R.id.group_btn_date);
//        group_btn_leap_year = view.findViewById(R.id.group_btn_leap_year);
        group_btn_last_day = view.findViewById(R.id.group_btn_last_day);
        group_btn_holiday = view.findViewById(R.id.group_btn_holiday);
        btn_finish = view.findViewById(R.id.btn_finish);
    }
}