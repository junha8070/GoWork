package com.example.gowork.view_Setting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.gowork.R;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class AddWork_Schedule_Fragment extends Fragment {

    private String TAG = "AddWork_Schedule_Fragment_TAG";

    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    private String place_name, address, kind, pay, join, resign;

    MaterialButtonToggleGroup group_btn_schedule, group_btn_date, group_btn_last_day;
    LinearLayout layout_every_week, layout_every_month, layout_last_day;
    Spinner sp_date;
    EditText edt_probation;
    TextInputEditText edt_start_time, edt_end;
    SwitchMaterial sw_holiday_allowance, sw_same_time, sw_holiday;
    MaterialButton btn_sun, btn_mon, btn_tue, btn_wed, btn_thu, btn_fri, btn_sat, btn_last_day_do, btn_last_day_not_do, btn_finish;

    WorkInfo workInfo;
    HashMap<String, Object> schedule;
    HashMap<String, Boolean> work_date;

    String[] res_date;
    ArrayAdapter dateAdapter;

    String probation;
    String start_time;
    String end_time;
    Boolean holiday_allowance;
    Boolean sameTime_5upper;
    Boolean work_holiday;
    String str_hour, str_minute;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddWork_Schedule_Fragment() {
        // Required empty public constructor
    }

    public static AddWork_Schedule_Fragment newInstance(String param1, String param2) {
        AddWork_Schedule_Fragment fragment = new AddWork_Schedule_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);


        getParentFragmentManager().setFragmentResultListener("result", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                place_name = result.getString("place_name");
                address = result.getString("address");
                kind = result.getString("kind");
                pay = result.getString("pay");
                join = result.getString("join");
                resign = result.getString("resign");

                Log.d(TAG, " 근무지 : " + place_name + " 주소 : " + address + " 종류 : " + kind + " 급여 : " + pay + " 입사 : " + join + " 퇴사 : " + resign);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_work__schedule_, container, false);

        init(view);
        layout_every_week.setVisibility(View.GONE);
        layout_every_month.setVisibility(View.GONE);
        layout_last_day.setVisibility(View.GONE);

        res_date = getResources().getStringArray(R.array.date);
        dateAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, res_date);
        sp_date.setAdapter(dateAdapter);

        group_btn_schedule.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (!isChecked) {
                    sp_date.setSelection(0);
                    layout_every_week.setVisibility(View.GONE);
                    layout_every_month.setVisibility(View.GONE);
                    layout_last_day.setVisibility(View.GONE);
                } else {
                    switch (checkedId) {
                        case R.id.button_every_month:
                            layout_every_week.setVisibility(View.GONE);
                            layout_every_month.setVisibility(View.VISIBLE);
                            layout_last_day.setVisibility(View.GONE);
//                            sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                                    if(pos == 28||pos==29||pos==30){
//                                        layout_last_day.setVisibility(View.VISIBLE);
//                                    }
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                }
//                            });
                            break;
                        case R.id.button_every_week:
                            sp_date.setSelection(0);
                            layout_every_week.setVisibility(View.VISIBLE);
                            layout_every_month.setVisibility(View.GONE);
                            layout_last_day.setVisibility(View.GONE);
                            break;
                        case R.id.button_every_day:
                            sp_date.setSelection(0);
                            layout_every_week.setVisibility(View.GONE);
                            layout_every_month.setVisibility(View.GONE);
                            layout_last_day.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        });


        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "i값 : " + position + "l값 : " + l);
                if (position == 28 || position == 29 || position == 30) {
                    layout_last_day.setVisibility(View.VISIBLE);
                } else {
                    layout_last_day.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(00)
                        .setTitleText("출근 시간 지정")
                        .build();

                materialTimePicker.show(getActivity().getSupportFragmentManager(), getTag());
                materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
                    int newHour = materialTimePicker.getHour();
                    int newMinute = materialTimePicker.getMinute();

                    if (newMinute == 0) {
                        str_minute = "00";
                    } else {
                        str_minute = String.valueOf(newMinute);
                    }

                    //TODO: 00분일때 0분으로 뜨는 이슈 해결하기
                    if (newHour >= 13) {
                        str_hour = "0" + String.valueOf((newHour) - 12);
                        edt_start_time.setText(str_hour + ":" + str_minute + " PM");
                    } else if (newHour == 12) {
                        edt_start_time.setText(newHour + ":" + str_minute + " PM");
                    } else {
                        str_hour = "0" + String.valueOf(newHour);
                        edt_start_time.setText(str_hour + ":" + str_minute + " AM");
                    }

                });
            }
        });

        edt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(00)
                        .setTitleText("퇴근 시간 지정")
                        .build();

                materialTimePicker.show(getActivity().getSupportFragmentManager(), getTag());
                materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
                    int newHour = materialTimePicker.getHour();
                    int newMinute = materialTimePicker.getMinute();

                    if (newMinute == 0) {
                        str_minute = "00";
                    } else {
                        str_minute = String.valueOf(newMinute);
                    }

                    //TODO: 00분일때 0분으로 뜨는 이슈 해결하기
                    if (newHour >= 13) {
                        str_hour = "0" + String.valueOf((newHour) - 12);
                        edt_end.setText(str_hour + ":" + str_minute + " PM");
                    } else if (newHour == 12) {
                        edt_end.setText(newHour + ":" + str_minute + " PM");
                    } else if (newHour >= 10 && newHour < 12) {
                        edt_end.setText(newHour + ":" + str_minute + " PM");
                    } else {
                        str_hour = "0" + String.valueOf(newHour);
                        edt_end.setText(str_hour + ":" + str_minute + " AM");
                    }

                });
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                probation = edt_probation.getText().toString();
                start_time = edt_start_time.getText().toString();
                end_time = edt_end.getText().toString();
                holiday_allowance = sw_holiday_allowance.isChecked();
                sameTime_5upper = sw_same_time.isChecked();
                work_holiday = sw_holiday.isChecked();

                workInfo = new WorkInfo(place_name, address, kind, pay, join, resign, probation, start_time, end_time, holiday_allowance, sameTime_5upper, work_holiday);

                switch (group_btn_schedule.getCheckedButtonId()) {
                    case R.id.button_every_month:
                        String date = sp_date.getSelectedItem().toString();

                        schedule = new HashMap<>();
                        schedule.put("type", "monthly");
                        schedule.put("date", date);
                        if (btn_last_day_do.isChecked()) {
                            schedule.put("last_day", true);
                        } else {
                            schedule.put("last_day", false);
                        }

                        dbViewModel.setUploadWorkInfo(authViewModel.getFirebaseUserLiveData().getValue(), workInfo, schedule);
                        break;
                    case R.id.button_every_week:
                        schedule = new HashMap<>();
                        schedule.put("type", "weekly");
                        schedule.put("sun", false);
                        schedule.put("mon", false);
                        schedule.put("tue", false);
                        schedule.put("wed", false);
                        schedule.put("thu", false);
                        schedule.put("fri", false);
                        schedule.put("sat", false);

                        if (btn_sun.isChecked()) {
                            schedule.put("sun", true);
                        }
                        if (btn_mon.isChecked()) {
                            schedule.put("mon", true);
                        }
                        if (btn_tue.isChecked()) {
                            schedule.put("tue", true);
                        }
                        if (btn_wed.isChecked()) {
                            schedule.put("wed", true);
                        }
                        if (btn_thu.isChecked()) {
                            schedule.put("thu", true);
                        }
                        if (btn_fri.isChecked()) {
                            schedule.put("fri", true);
                        }
                        if (btn_sat.isChecked()) {
                            schedule.put("sat", true);
                        }

                        dbViewModel.setUploadWorkInfo(authViewModel.getFirebaseUserLiveData().getValue(), workInfo, schedule);
                        break;
                    case R.id.button_every_day:
                        schedule = new HashMap<>();
                        schedule.put("type", "everyday");

                        dbViewModel.setUploadWorkInfo(authViewModel.getFirebaseUserLiveData().getValue(), workInfo, schedule);
                        break;
                }

                dbViewModel.getUploadWorkInfoTask().observe(getActivity(), new Observer<Task>() {
                    @Override
                    public void onChanged(Task task) {
                        if (task.isSuccessful()) {
                            Navigation.findNavController(getView()).navigate(R.id.action_addWork_Schedule_Fragment_to_settingFragment);
                        }
                    }
                });
            }
        });

        return view;
    }

    private void init(View view) {
        group_btn_schedule = view.findViewById(R.id.group_btn_schedule);
        group_btn_date = view.findViewById(R.id.group_btn_date);
        group_btn_last_day = view.findViewById(R.id.group_btn_last_day);
        layout_every_month = view.findViewById(R.id.layout_every_month);
        layout_every_week = view.findViewById(R.id.layout_every_week);
        layout_last_day = view.findViewById(R.id.layout_last_day);
        sp_date = view.findViewById(R.id.sp_date);
        btn_sun = view.findViewById(R.id.btn_sun);
        btn_mon = view.findViewById(R.id.btn_mon);
        btn_tue = view.findViewById(R.id.btn_tue);
        btn_wed = view.findViewById(R.id.btn_wed);
        btn_thu = view.findViewById(R.id.btn_thu);
        btn_fri = view.findViewById(R.id.btn_fri);
        btn_sat = view.findViewById(R.id.btn_sat);
        btn_last_day_do = view.findViewById(R.id.btn_last_day_do);
        btn_last_day_not_do = view.findViewById(R.id.btn_last_day_not_do);
        edt_probation = view.findViewById(R.id.edt_probation);
        edt_start_time = view.findViewById(R.id.edt_start_time);
        edt_end = view.findViewById(R.id.edt_end);
        sw_holiday_allowance = view.findViewById(R.id.sw_holiday_allowance);
        sw_same_time = view.findViewById(R.id.sw_same_time);
        sw_holiday = view.findViewById(R.id.sw_holiday);
        btn_finish = view.findViewById(R.id.btn_finish);
    }
}