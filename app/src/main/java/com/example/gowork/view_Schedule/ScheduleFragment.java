package com.example.gowork.view_Schedule;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gowork.R;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private String TAG = "ScheduleFragmentTAG";

    private MaterialButton btn_prev, btn_next;
    private MaterialTextView tv_month;
    private RecyclerView rv_calendar, rv_schedule_info;

    Calendar_Adapter calendar_adapter;
    Schedule_Info_Adapter schedule_info_adapter;

    LocalDateTime current_time;

    int current_year;
    int current_month;
    HashMap<Integer, Integer> days;
    HashMap<String, Boolean> weekly_schedule;
    StringBuilder sb;

    ArrayList<WorkInfo> entire_arr_workInfo;
    ArrayList<HashMap<String, Object>> entire_arr_schedule;

    AuthViewModel authViewModel;
    DBViewModel dbViewModel;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        init(view); // 요소 초기화

        // 상단 Month 설정
        current_time = LocalDateTime.now();
        current_year = current_time.getYear();
        current_month = current_time.getMonthValue();
        String str_current_month = String.valueOf(current_month);
        tv_month.setText(str_current_month + "월");

        days = getDays(current_year, current_month);
        setRv_calendar(current_year, current_month, days, null);

        Set<Integer> key = days.keySet();
        Collection<Integer> value = days.values();
        Iterator<Integer> key_iter = key.iterator();
        Iterator<Integer> value_iter = value.iterator();
        ArrayList arr_date = new ArrayList();
        ArrayList arr_day_of_week = new ArrayList();
        while (key_iter.hasNext()) {
            arr_date.add(key_iter.next());
        }
        while (value_iter.hasNext()) {
            arr_day_of_week.add(value_iter.next());
        }

        dbViewModel.getWorkInfoMutableLiveData().observe(getActivity(), new Observer<ArrayList<WorkInfo>>() {
            @Override
            public void onChanged(ArrayList<WorkInfo> arrayList) {
                if (arrayList != null) {
                    entire_arr_workInfo.addAll(arrayList);

                }
            }
        });

        dbViewModel.getScheduleMutableLiveData().observe(getActivity(), new Observer<ArrayList<HashMap<String, Object>>>() {
                        @Override
                        public void onChanged(ArrayList<HashMap<String, Object>> hashMaps) {
                            if (hashMaps != null) {
                                entire_arr_schedule.addAll(hashMaps);
                                setRv_calendar(current_year, current_month, days, entire_arr_schedule);
//                                for (int i = 0; i < entire_arr_schedule.size(); i++) {
//                                    if (entire_arr_schedule.get(i).get("type").equals("weekly")) {
//                                        weekly_schedule = (HashMap<String, Boolean>) entire_arr_schedule.get(i).get("week_day");
//                                        sb = sortDate(weekly_schedule);
//                                        Log.d(TAG, sb.toString());
//                                    }
//                                }
                            }
                        }
                    });

        Log.d(TAG, String.valueOf(entire_arr_workInfo.get(0).getPlace_name()));
        Log.d(TAG, String.valueOf(entire_arr_schedule.get(0).get("type")));
        Log.d(TAG, String.valueOf(entire_arr_schedule.size()));
        Log.d(TAG, String.valueOf(entire_arr_workInfo.size()));


        calendar_adapter.setOnItemClickListener(new Calendar_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ArrayList<WorkInfo> select_arr_workInfo = new ArrayList<>();
                ArrayList<HashMap<String, Object>> select_arr_schedule = new ArrayList<>();
                select_arr_workInfo.addAll(entire_arr_workInfo);
                select_arr_schedule.addAll(entire_arr_schedule);
                LocalDateTime startDate = LocalDateTime.of(current_year, current_month, (int) arr_date.get(position), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                int day_of_week = startDate.getDayOfWeek().getValue();
                String select_day_of_week = convert_day_of_week_num_to_kor(day_of_week);
                String cal_info = current_month + "월 " + arr_date.get(position) + "일";
                for (int i = 0; i < entire_arr_schedule.size(); i++) {
                    if (entire_arr_schedule.get(i).get("type").equals("weekly")) {
                        weekly_schedule = (HashMap<String, Boolean>) entire_arr_schedule.get(i).get("week_day");
                        sb = sortDate(weekly_schedule);
                        if (!sb.toString().contains(select_day_of_week)) {
                            select_arr_workInfo.remove(i);
                            select_arr_schedule.remove(i);
                        }
                        Log.d(TAG, sb.toString());
                    }
                }

                setRv_schedule_info(cal_info, select_arr_workInfo);

//                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        // 다음달 버튼 클릭시
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (++current_month > 12) {
                    current_month = 1;
                    current_year++;
                }
                tv_month.setText(current_month + "월");
                days = getDays(current_year, current_month);
                setRv_calendar(current_year, current_month, days, entire_arr_schedule);

                Set<Integer> key = days.keySet();
                Collection<Integer> value = days.values();
                Iterator<Integer> key_iter = key.iterator();
                Iterator<Integer> value_iter = value.iterator();
                ArrayList arr_date = new ArrayList();
                ArrayList arr_day_of_week = new ArrayList();
                while (key_iter.hasNext()) {
                    arr_date.add(key_iter.next());
                }
                while (value_iter.hasNext()) {
                    arr_day_of_week.add(value_iter.next());
                }

                calendar_adapter.setOnItemClickListener(new Calendar_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        ArrayList<WorkInfo> select_arr_workInfo = new ArrayList<>();
                        ArrayList<HashMap<String, Object>> select_arr_schedule = new ArrayList<>();
                        select_arr_workInfo.addAll(entire_arr_workInfo);
                        select_arr_schedule.addAll(entire_arr_schedule);
                        LocalDateTime startDate = LocalDateTime.of(current_year, current_month, (int) arr_date.get(position), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                        int day_of_week = startDate.getDayOfWeek().getValue();
                        String select_day_of_week = convert_day_of_week_num_to_kor(day_of_week);
                        String cal_info = current_month + "월 " + arr_date.get(position) + "일";
                        for (int i = 0; i < entire_arr_schedule.size(); i++) {
                            if (entire_arr_schedule.get(i).get("type").equals("weekly")) {
                                weekly_schedule = (HashMap<String, Boolean>) entire_arr_schedule.get(i).get("week_day");
                                sb = sortDate(weekly_schedule);
                                if (!sb.toString().contains(select_day_of_week)) {
                                    select_arr_workInfo.remove(i);
                                    select_arr_schedule.remove(i);
                                }
                                Log.d(TAG, sb.toString());
                            }
                        }

                        setRv_schedule_info(cal_info, select_arr_workInfo);

//                        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 이전달 버튼 클릭시
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (--current_month < 1) {
                    current_month = 12;
                    current_year--;
                }
                tv_month.setText(current_month + "월");
                days = getDays(current_year, current_month);
                setRv_calendar(current_year, current_month, days, entire_arr_schedule);

                Set<Integer> key = days.keySet();
                Collection<Integer> value = days.values();
                Iterator<Integer> key_iter = key.iterator();
                Iterator<Integer> value_iter = value.iterator();
                ArrayList arr_date = new ArrayList();
                ArrayList arr_day_of_week = new ArrayList();
                while (key_iter.hasNext()) {
                    arr_date.add(key_iter.next());
                }
                while (value_iter.hasNext()) {
                    arr_day_of_week.add(value_iter.next());
                }

                calendar_adapter.setOnItemClickListener(new Calendar_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        ArrayList<WorkInfo> select_arr_workInfo = new ArrayList<>();
                        ArrayList<HashMap<String, Object>> select_arr_schedule = new ArrayList<>();
                        select_arr_workInfo.addAll(entire_arr_workInfo);
                        select_arr_schedule.addAll(entire_arr_schedule);
                        LocalDateTime startDate = LocalDateTime.of(current_year, current_month, (int) arr_date.get(position), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                        int day_of_week = startDate.getDayOfWeek().getValue();
                        String select_day_of_week = convert_day_of_week_num_to_kor(day_of_week);
                        String cal_info = current_month + "월 " + arr_date.get(position) + "일";
                        for (int i = 0; i < entire_arr_schedule.size(); i++) {
                            if (entire_arr_schedule.get(i).get("type").equals("weekly")) {
                                weekly_schedule = (HashMap<String, Boolean>) entire_arr_schedule.get(i).get("week_day");
                                sb = sortDate(weekly_schedule);
                                if (!sb.toString().contains(select_day_of_week)) {
                                    select_arr_workInfo.remove(i);
                                    select_arr_schedule.remove(i);
                                }
                                Log.d(TAG, sb.toString());
                            }
                        }

                        setRv_schedule_info(cal_info, select_arr_workInfo);

//                        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }

    private void init(View view) {
        btn_prev = view.findViewById(R.id.btn_prev);
        tv_month = view.findViewById(R.id.tv_month);
        btn_next = view.findViewById(R.id.btn_next);
        rv_calendar = view.findViewById(R.id.rv_calendar);
        rv_schedule_info = view.findViewById(R.id.rv_schedule_info);
        entire_arr_workInfo = new ArrayList<>();
        entire_arr_schedule = new ArrayList<>();
    }

    private void setRv_calendar(int year, int month, HashMap<Integer, Integer> days, ArrayList<HashMap<String, Object>> entire_arr_schedule) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 7);
        rv_calendar.setLayoutManager(gridLayoutManager);
        calendar_adapter = new Calendar_Adapter(year, month, days, entire_arr_schedule);
        rv_calendar.setAdapter(calendar_adapter);
    }

    private void setRv_schedule_info(String cal_info, ArrayList<WorkInfo> select_arr_workInfo) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        rv_schedule_info.setLayoutManager(linearLayoutManager);
        schedule_info_adapter = new Schedule_Info_Adapter(cal_info, select_arr_workInfo);
        rv_schedule_info.setAdapter(schedule_info_adapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        if (rv_schedule_info.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(rv_schedule_info);

        rv_schedule_info.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "newState값 : " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "dx값 : " + dx + "dy값 : " + dy);
                LinearLayoutManager layoutManager =
                        LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
            }
        });

        rv_schedule_info.scrollToPosition(select_arr_workInfo.size()-1);
    }

    private HashMap<Integer, Integer> getDays(int Year, int Month) {

        HashMap<Integer, Integer> days = new LinkedHashMap<>();

        LocalDateTime.now().getYear();
        LocalDateTime startDate = LocalDateTime.of(Year, Month, 1, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
        LocalDateTime endDate = startDate.plusMonths(1);
        Duration between = Duration.between(startDate, endDate);

        int day_of_week = startDate.getDayOfWeek().getValue();

        if (day_of_week == 7) {

        } else {
//            Log.d(TAG, "앞 날짜"+day_of_week);
            int fill_empty_days_init_value = (day_of_week - 1) * -1;
            for (int i = fill_empty_days_init_value; i <= 0; i++) {
                days.put(i, 0);
//                Log.d(TAG, "대입된 날짜 (앞부분) : "+ days);
            }
        }

        for (int i = 1; i <= between.toDays(); i++) {
            if (day_of_week == 8) {
                day_of_week = 1;
            }
            days.put(i, day_of_week++);
//            Log.d(TAG, "대입된 날짜 (중간부분) : "+ days);
        }

        LocalDateTime end = LocalDateTime.of(Year, Month, (int) between.toDays(), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
        int end_day_of_week = end.getDayOfWeek().getValue();
        if (end_day_of_week == 7) {
            for (int i = 1; i <= 6; i++) {
                days.put((int) (between.toDays() + i), 0);
//                Log.d(TAG, "대입된 날짜 (끝부분) : "+ days);
            }
        } else {
            int fill_empty_days_init_value = (end_day_of_week);
            for (int i = 1; i <= fill_empty_days_init_value; i++) {
                days.put((int) (between.toDays() + i), 0);
//                Log.d(TAG, "대입된 날짜 (끝부분) : "+ days);
            }
        }

//        Log.d(TAG, String.valueOf(days));

        return days;
    }

    public StringBuilder sortDate(HashMap<String, Boolean> dateValue) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> sort_date = new ArrayList<>();
        for (String key : dateValue.keySet()) {
            if (dateValue.get(key)) {
                switch (key) {
                    case "sun":
                        sort_date.add(0);
                        break;
                    case "mon":
                        sort_date.add(1);
                        break;
                    case "tue":
                        sort_date.add(2);
                        break;
                    case "wed":
                        sort_date.add(3);
                        break;
                    case "thu":
                        sort_date.add(4);
                        break;
                    case "fri":
                        sort_date.add(5);
                        break;
                    case "sat":
                        sort_date.add(6);
                        break;
                }
            }
        }
        Collections.sort(sort_date);
        for (Integer value : sort_date) {
            switch (value) {
                case 0:
                    sb.append("일");
                    sb.append(", ");
                    break;
                case 1:
                    sb.append("월");
                    sb.append(", ");
                    break;
                case 2:
                    sb.append("화");
                    sb.append(", ");
                    break;
                case 3:
                    sb.append("수");
                    sb.append(", ");
                    break;
                case 4:
                    sb.append("목");
                    sb.append(", ");
                    break;
                case 5:
                    sb.append("금");
                    sb.append(", ");
                    break;
                case 6:
                    sb.append("토");
                    sb.append(", ");
                    break;
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(", "));
        return sb;
    }

    private String convert_day_of_week_num_to_kor(int day_of_week) {
        switch (day_of_week) {
            case 1:
                return "월";
            case 2:
                return "화";
            case 3:
                return "수";
            case 4:
                return "목";
            case 5:
                return "금";
            case 6:
                return "토";
            case 7:
                return "일";
            default:
                return "미정";
        }
    }
}