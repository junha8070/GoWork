package com.example.gowork.view_Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gowork.MainActivity;
import com.example.gowork.R;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.dto.WorkingTimeDTO;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.example.gowork.viewModel.WorkingViewModel;
import com.example.gowork.view_Community.Community_Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
//        implements OnBackPressedListener
{

    private String TAG = "HomeFragmentTAG";

    Context context;

    MaterialButton btn_working_place, btn_goWork;
    RecyclerView rv;
//    TextInputLayout sp_working_place;
//    AutoCompleteTextView autoCompleteTextView;

    DBViewModel dbViewModel;
    AuthViewModel authViewModel;
    MainActivity mainActivity;

    Boolean workingState;

    WorkingViewModel stateViewModel;

    Home_Rv_Adapter home_rv_adapter;

    long backKeyPressedTime;

    ArrayList<WorkInfo> workInfo_receive;
    ArrayList<HashMap<String, Object>> hashMaps_receive;
    List<String> spinnerArray;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        stateViewModel = new ViewModelProvider(requireActivity()).get(WorkingViewModel.class);

        workInfo_receive = new ArrayList<>();
        hashMaps_receive = new ArrayList<>();

        dbViewModel.getWorkInfoData(authViewModel.getFirebaseUserLiveData().getValue());
        dbViewModel.getScheduleData();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();

        init(view);

        // TODO: 리사이클러뷰 중복으로 뜨는거 해결

        dbViewModel.getWorkInfoMutableLiveData().observe(getActivity(), new Observer<ArrayList<WorkInfo>>() {
            @Override
            public void onChanged(ArrayList<WorkInfo> workInfo) {
                if (workInfo != null) {
                    workInfo_receive.addAll(workInfo);
                    spinnerArray = new ArrayList<String>();
                    for (int q = 0; q < workInfo.size(); q++) {
                        spinnerArray.add(workInfo.get(q).getPlace_name());
                        Log.d(TAG, "스피너 갯수" + String.valueOf(workInfo.size()));
                    }
                }
            }
        });

        dbViewModel.getScheduleMutableLiveData().observe(getActivity(), new Observer<ArrayList<HashMap<String, Object>>>() {
            @Override
            public void onChanged(ArrayList<HashMap<String, Object>> hashMaps) {
                if (hashMaps != null) {
                    hashMaps_receive.addAll(hashMaps);
                    Log.d(TAG, "hashMaps_receive" + String.valueOf(hashMaps_receive));
                    for (int i = 0; i < hashMaps.size(); i++) {
                        if (hashMaps.get(i).get("week_day") != null) {
                            HashMap dayDetect = (HashMap) hashMaps.get(i).get("week_day");
//                                        if (!(Boolean) dayDetect.get(todayDate())) {
//                                            workInfo_receive.remove(i);
//                                            hashMaps_receive.remove(i);
//                                            Log.d(TAG, String.valueOf(workInfo_receive));
//                                        }
                        }
                    }
                    home_rv_adapter = new Home_Rv_Adapter(workInfo_receive, hashMaps_receive);
                    rv.setAdapter(home_rv_adapter); // 어댑터 설정
                }
            }
        });

        this.context = getContext();

        if (stateViewModel.getWorkingState() == null || !stateViewModel.getWorkingState()) {
            btn_goWork.setText("출근\n하기");
            btn_goWork.setBackgroundColor(getResources().getColor(R.color.point_color, getActivity().getTheme()));
        } else {
            btn_goWork.setText("퇴근\n하기");
            btn_working_place.setText(stateViewModel.getWorkingPlace());
            btn_goWork.setBackgroundColor(Color.GRAY);
        }

        btn_working_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("근무지를 선택해주세요.");


                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                adapter.addAll(spinnerArray);

                builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!btn_working_place.getText().toString().equals("근무지를 선택해주세요")) {
                            btn_goWork.setEnabled(true);
                        }
                        btn_working_place.setText(adapter.getItem(i));
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        btn_goWork.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (btn_working_place.getText().toString().equals("근무지를 선택해주세요")) {
                    Toast.makeText(getContext(), "근무지를 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (stateViewModel.getWorkingState() == null || stateViewModel.getWorkingState() == false) {
                        Toast.makeText(getContext(), "출근", Toast.LENGTH_SHORT).show();
                        btn_goWork.setText("퇴근\n하기");
                        btn_goWork.setBackgroundColor(Color.GRAY);
                        stateViewModel.setWorkingState(true);
                        stateViewModel.setWorkingPlace(btn_working_place.getText().toString());
                        stateViewModel.setStartWorkingTime(getCurrentTime());
                        Toast.makeText(getContext(), getCurrentTime(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "퇴근", Toast.LENGTH_SHORT).show();
                        btn_goWork.setText("출근\n하기");
                        btn_goWork.setBackgroundColor(getResources().getColor(R.color.point_color, getActivity().getTheme()));
                        stateViewModel.setWorkingState(false);
                        stateViewModel.setFinishWorkingTime(getCurrentTime());
                        HashMap<String, String> Working_Time = new LinkedHashMap<>();
                        Working_Time.put("Start_Time", stateViewModel.getStartWorkingTime());
                        Working_Time.put("Finish_Time", stateViewModel.getFinishWorkingTime());
                        Log.d(TAG, "출근지 : " + stateViewModel.getWorkingPlace() + " 시작시간: " + stateViewModel.getStartWorkingTime() + "퇴근 시간: " + stateViewModel.getFinishWorkingTime());
                        WorkingTimeDTO workingTimeDTO = new WorkingTimeDTO(stateViewModel.getWorkingPlace(), Working_Time);
                        stateViewModel.setWorkingTime(authViewModel.getFirebaseUserLiveData().getValue(), workingTimeDTO);
                        Toast.makeText(getContext(), getCurrentTime(), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelper = new PagerSnapHelper();
        if (rv.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(rv);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager =
                        LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
            }
        });

        return view;
    }

    public void init(View view) {
        rv = view.findViewById(R.id.rv);
        btn_working_place = view.findViewById(R.id.btn_working_place);
        btn_goWork = view.findViewById(R.id.btn_goWork);
    }

//    @Override
//    public void onBackPressed() {
//
//        //터치간 시간을 줄이거나 늘리고 싶다면 2000을 원하는 시간으로 변경해서 사용하시면 됩니다.
//        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
//            backKeyPressedTime = System.currentTimeMillis();
//            Toast.makeText(getContext(),"한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
//            getActivity().finish();
//            Toast.makeText(getContext(),"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).cancel();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
//        mainActivity.setOnBackPressedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        hideBottomNavigation(false);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();       // 포맷 정의
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");        // 포맷 적용
        String currentTime = formatter.format(calendar.getTime());
        return currentTime;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();       // 포맷 정의
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");        // 포맷 적용
        String currentTime = formatter.format(calendar.getTime());
        return currentTime;
    }

    public String todayDate() {
        LocalDate date = LocalDate.now();
        System.out.println(date);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US).toLowerCase(Locale.ROOT);
    }
}