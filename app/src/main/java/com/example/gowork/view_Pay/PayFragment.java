package com.example.gowork.view_Pay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gowork.R;
import com.example.gowork.dto.PayRvDTO;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.example.gowork.viewModel.WorkingViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {

    private String TAG = "PayFragment";

    private MaterialButton btn_set_month;
    private TextView tv_total_pay;
    private RecyclerView rv_pay;
    private String[] month = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};

    private WorkingViewModel workingViewModel;
    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    private Pay_Adapter payAdapter;

    HashMap<String, Integer> work_per_pay;
    HashMap<String, ArrayList> entire_Start_Time_HashMap;
    HashMap<String, ArrayList> entire_End_Time_HashMap;

    ArrayList<PayRvDTO> arr_payRvDTO;

    ArrayList<Integer> arr_totalPay;
    int sumTotalPay = 0;

    ArrayList<String> workTitle;
    ArrayList startTime, endTime;

    LocalDate now;
    String yearMonth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PayFragment newInstance(String param1, String param2) {
        PayFragment fragment = new PayFragment();
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
        workingViewModel = new ViewModelProvider(requireActivity()).get(WorkingViewModel.class);

        now = LocalDate.now();
        yearMonth = String.valueOf(now.getYear()) + "-" + String.valueOf(now.getMonth().getValue());
        workingViewModel.getWorkingTime(authViewModel.getFirebaseUserLiveData().getValue(), yearMonth);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay, container, false);

        init(view);

        yearMonth = String.valueOf(now.getYear()) + "-" + String.valueOf(now.getMonth().getValue());

        btn_set_month.setText(String.valueOf(now.getYear()) + "년 " + String.valueOf(now.getMonth().getValue()) + "월달");

        startTime = new ArrayList();
        endTime = new ArrayList();
        work_per_pay = new LinkedHashMap<>();
        entire_Start_Time_HashMap = new LinkedHashMap<>();
        entire_End_Time_HashMap = new LinkedHashMap<>();
        arr_totalPay = new ArrayList();

        workingViewModel.getWorkingTimeMutableLiveData().observe(getActivity(), new Observer<HashMap<String, HashMap>>() {
            @Override
            public void onChanged(HashMap<String, HashMap> timeValues) {

                int sumTotalPay = 0;
                arr_payRvDTO = new ArrayList<>();
                arr_totalPay = new ArrayList<>();

                workTitle = new ArrayList(timeValues.get("startTime").keySet());

                for (int i = 0; i < dbViewModel.getWorkInfoMutableLiveData().getValue().size(); i++) {
                    // 직장 이름
                    String title = dbViewModel.getWorkInfoMutableLiveData().getValue().get(i).getPlace_name();
                    // 시급
                    int pay = Integer.parseInt(dbViewModel.getWorkInfoMutableLiveData().getValue().get(i).getPay());
                    // 직장 이름 : 시급
                    work_per_pay.put(title, pay);
                }

                HashMap<String, ArrayList> entire_Start_Time_HashMap = new LinkedHashMap<>();
                entire_Start_Time_HashMap.putAll(timeValues.get("startTime"));
                HashMap<String, ArrayList> entire_End_Time_HashMap = new LinkedHashMap<>();
                entire_End_Time_HashMap.putAll(timeValues.get("finishTime"));

                for (String work_title : workTitle) {
                    for (int i = 0; i < entire_Start_Time_HashMap.get(work_title).size(); i++) {
                        PayRvDTO payRvDTO = new PayRvDTO();
                        payRvDTO.setWorkTitle(work_title);
                        payRvDTO.setStart_time(strTimeToDate((String) entire_Start_Time_HashMap.get(work_title).get(i)));
                        payRvDTO.setFinish_time(strTimeToDate((String) entire_End_Time_HashMap.get(work_title).get(i)));
                        payRvDTO.setDate(strDateToDate((String) entire_Start_Time_HashMap.get(work_title).get(i)));

                        long sec = timeDuration((String) entire_Start_Time_HashMap.get(work_title).get(i), (String) entire_End_Time_HashMap.get(work_title).get(i));
                        int calcPay = (int) Math.floor(((double) work_per_pay.get(work_title) * (sec / 60.0 / 60.0)));
                        arr_totalPay.add(calcPay);

                        int duration_hours = ((int) sec / 3600);
                        int duration_minutes = (((int) sec - (duration_hours * 3600)) / 60);
                        if(duration_minutes < 10){
                            payRvDTO.setTime_duration(duration_hours + "시간 0" + duration_minutes + "분");
                        }else{
                            payRvDTO.setTime_duration(duration_hours + "시간" + duration_minutes + "분");
                        }
                        payRvDTO.setPay(String.valueOf(calcPay) + "원");
                        arr_payRvDTO.add(payRvDTO);
                    }
                    Log.d(TAG, "시작시간 : " + work_title + entire_Start_Time_HashMap.get(work_title));
                    Log.d(TAG, "퇴근시간 : " + work_title + entire_End_Time_HashMap.get(work_title));
                }

                for (int r = 0; r < arr_totalPay.size(); r++) {
                    sumTotalPay += arr_totalPay.get(r);
                    tv_total_pay.setText(String.valueOf(sumTotalPay));
                }

                Pay_Adapter payAdapter = new Pay_Adapter(arr_payRvDTO);

                rv_pay.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_pay.setAdapter(payAdapter);
            }
        });


        ArrayList month = new ArrayList();
        for (int i = -6; i < 6; i++) {
            if (now.getMonth().getValue() + i <= 0) {
                int year = now.getYear() - 1;
                String year_month = year + "년 " + now.getMonth().plus(i).getValue() + "월달";
                month.add(year_month);
            } else if (now.getMonth().getValue() + i >= 13) {
                int year = now.getYear() + 1;
                String year_month = year + "년 " + now.getMonth().plus(i).getValue() + "월달";
                month.add(year_month);
            } else {
                int year = now.getYear();
                String year_month = year + "년 " + now.getMonth().plus(i).getValue() + "월달";
                month.add(year_month);
            }
        }

        btn_set_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("월을 골라주세요");


                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                adapter.addAll(month);

                builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        btn_set_month.setText(adapter.getItem(i));
                        String yearMonth = adapter.getItem(i);
                        yearMonth = yearMonth.replace("년 ", "-");
                        yearMonth = yearMonth.replace("월달", "");
                        workingViewModel.getWorkingTime(authViewModel.getFirebaseUserLiveData().getValue(), yearMonth);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    private void init(View view) {
        now = LocalDate.now();
        btn_set_month = view.findViewById(R.id.btn_set_month);
        tv_total_pay = view.findViewById(R.id.tv_total_pay);
        rv_pay = view.findViewById(R.id.rv_pay);
    }

    private Long timeDuration(String start, String end) {
        DateTimeFormatter dateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalTime startLocalTime = LocalTime.parse(start, dateParser);
        LocalTime endLocalTime = LocalTime.parse(end, dateParser);

        Log.d(TAG, "duration startLocalTime: " + startLocalTime);
        Log.d(TAG, "duration endLocalTime: " + endLocalTime);

        Duration duration = Duration.between(startLocalTime, endLocalTime);
        Log.d(TAG, "duration : " + duration.getSeconds());
        return duration.getSeconds();
    }

    private String strTimeToDate(String time) {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        {
            try {
                Date date = dateParser.parse(time);
//                System.out.println(date);

                SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                return dateFormatter.format(date);
//                System.out.println();

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private String strDateToDate(String Date) {
        Log.d(TAG, Date.toString());
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        {
            try {
                Date date = dateParser.parse(Date);
                Log.d(TAG, date.toString());

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM월 dd일");
                Log.d(TAG, dateFormatter.format(date));
                return dateFormatter.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}