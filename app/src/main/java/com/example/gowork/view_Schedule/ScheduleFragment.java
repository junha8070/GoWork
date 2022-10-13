package com.example.gowork.view_Schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gowork.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView rv_calendar;
    private Calendar_Adapter calendar_adapter;
    private Calendar_day_Adapter calendar_day_adapter;
    private ArrayList<HashMap<String, ArrayList>> Calendar_24;
    private HashMap<String, ArrayList> Month_Value;
    private ArrayList day_values;

    private ArrayList arrayList;
    private ArrayList<Calendar_day_Adapter> day_adapterArrayList;

    private String TAG = "ScheduleFragmentTAG";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        init(view); // 요소 초기화

        Calendar_24 = new ArrayList<>();
        Month_Value = new HashMap<>();
        day_values = new ArrayList();

        day_adapterArrayList = new ArrayList<>();
        arrayList = new ArrayList();

        calendar_day_adapter = new Calendar_day_Adapter(getContext(), arrayList);
        day_adapterArrayList.add(calendar_day_adapter);
        calendar_adapter = new Calendar_Adapter(day_adapterArrayList);


        rv_calendar.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_calendar.setAdapter(calendar_adapter);

        Calendar c = Calendar.getInstance();
//        c.set(c.getTime().);

        // 현재 날짜
        Calendar current = Calendar.getInstance();
        Log.d(TAG, String.valueOf(current.get(Calendar.YEAR)));
        Log.d(TAG, String.valueOf(current.get(Calendar.MONTH)));
        Log.d(TAG, String.valueOf(current.get(Calendar.DATE)));


        // 매달 1일
        Calendar first_day = Calendar.getInstance();
        first_day.set(Calendar.YEAR, 2022);
        first_day.set(Calendar.MONTH, 8);
        first_day.set(Calendar.DATE, 1);
        Log.d(TAG, String.valueOf(first_day.get(Calendar.DAY_OF_WEEK))); // 9월 1일 목요일이므로 5

//        for (int i = 0; i < 25; i++) {
////            for (int r = 1; r < 43; r++) {
//            if (i <= 12) {
//                for (int preDay = first_day.get(Calendar.DAY_OF_WEEK) - 1; preDay > 0; preDay--) {
//                    first_day.add(Calendar.DATE, preDay);
//                    day_values.add(first_day.DATE);
//                }
//                for (int q = 1; q < first_day.getActualMaximum(Calendar.DAY_OF_MONTH); q++) {
//                    day_values.add(q);
//                }
//
//                first_day.getActualMaximum(Calendar.DAY_OF_WEEK);
//
//                for(int afterDay = 1; first_day.getActualMaximum(Calendar.DAY_OF_MONTH))
//            } else if (i > 12) {
//
//            }
////            }
//        }


        c.add(Calendar.DATE, -30);
        Log.d(TAG, String.valueOf(c.get(Calendar.YEAR)));
        Log.d(TAG, String.valueOf(c.get(Calendar.MONTH)));
        Log.d(TAG, String.valueOf(c.get(Calendar.DATE)));

//        Log.d(TAG, String.valueOf(c.getActualMaximum(c.DAY_OF_MONTH)));
//        Log.d(TAG, String.valueOf(c.getActualMaximum(c.WEEK_OF_MONTH)));

        return view;
    }

    private void init(View view) {
        rv_calendar = view.findViewById(R.id.rv_calendar);
    }
}