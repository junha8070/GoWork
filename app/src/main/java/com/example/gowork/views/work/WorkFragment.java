package com.example.gowork.views.work;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gowork.R;
import com.example.gowork.db.Work;
import com.example.gowork.viewmodel.WorkViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WorkFragment extends Fragment {

    private WorkViewModel workViewModel;

    private MaterialToolbar toolbar;
    private MaterialCalendarView mcv;
    private Calendar calendar;
    private Spinner sp_work;
    private TextView tv_test, tb_title;

    private ArrayAdapter<Work> adapter;
    private List workTitle;
    private List<Work> works;
    private StringBuilder entire;
    private int size;
    StringBuilder sb = new StringBuilder();

    private ProgressDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        workTitle = new ArrayList();
        works = new ArrayList<>();

        workViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(WorkViewModel.class);
        workViewModel.getAllWorks().observe(this, new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> dbWorks) {
                if(dbWorks == null){
                    mDialog.setMessage("확인중입니다");
                    mDialog.show();
                }else{
                    works = dbWorks;
                    mDialog.dismiss();
                    int listSize = dbWorks.size();
                    workTitle.add("전체보기");
                    for(int i =0; i<listSize;i++){
                        workTitle.add(dbWorks.get(i).getWorkTitle().toString());
                        sb.append(dbWorks.get(i).getHourMoney());
                        Toast.makeText(getActivity(), workTitle.get(0).toString(), Toast.LENGTH_SHORT).show();
                    }
                    adapter = new ArrayAdapter<Work>(getContext(), android.R.layout.simple_spinner_item, workTitle);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_work.setAdapter(adapter);


                    mDialog.dismiss();
                }
            }
        });

//        workViewModel.getAllTitle().observe(this, new Observer<List>() {
//            @Override
//            public void onChanged(List list) {
//                if(list == null){
//                    mDialog.setMessage("확인중입니다");
//                    mDialog.show();
//                }else{
//
//
//                }
//
//            }
//        });




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workfragment, container,false);

        init(view);
        mDialog = new ProgressDialog(getContext());
        mcv.setTopbarVisible(false);
        mcv.addDecorator(calendar);
        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                tb_title.setText(mcv.getCurrentDate().getYear() +"년 "+ (mcv.getCurrentDate().getMonth()) +"월 근무일정");
            }
        });




        return view;
    }

    private void init(View view){
        calendar = new Calendar();
        toolbar = view.findViewById(R.id.toolbar);
        mcv = view.findViewById(R.id.calendar);
        sp_work = view.findViewById(R.id.sp_work);
        tv_test = view.findViewById(R.id.tv_test);
        tb_title = view.findViewById(R.id.tb_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        tb_title.setText(mcv.getCurrentDate().getYear() +"년 "+ (mcv.getCurrentDate().getMonth()) +"월 근무일정");
        Log.d("WorkFragment", String.valueOf(tb_title.getTextSize()));
        size = getWorkTitleSize();
        StringBuilder sb = new StringBuilder();
        sp_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0){
                    tv_test.setText(sb);
                    for(int i = 1; i<size;i++){
//                        sb.append(String.valueOf(works.get(i-1).getHourMoney()));
                    }
//                    tv_test.setText(sb);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        sp_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(position == 0){
                    tv_test.setText(sb);
                    for(int i = 1; i<size;i++){
//                        sb.append(String.valueOf(works.get(i-1).getHourMoney()));\

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public int getWorkTitleSize(){
        return workTitle.size();
    }


}
