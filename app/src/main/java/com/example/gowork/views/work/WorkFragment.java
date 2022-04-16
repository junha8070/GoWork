package com.example.gowork.views.work;

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
    private TextView tv_test;

    private ArrayAdapter<Work> adapter;
    private ArrayList workTitle;
    private List<Work> works;
    private StringBuilder entire;
    private int size;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        works = new ArrayList<>();
        workTitle = new ArrayList();
        workTitle.add("전체 일정");
        workViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(WorkViewModel.class);
        workViewModel.getAllWorks().observe(this, new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> dbWorks) {
                works = dbWorks;
                for(int i=0;i<works.size();i++){
                    workTitle.add(works.get(i).getWorkTitle());
                }
                workTitle.add("근무지 추가");

                adapter = new ArrayAdapter<Work>(getContext(), android.R.layout.simple_spinner_item, workTitle);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_work.setAdapter(adapter);
                sp_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if(position==0){
                            entire = new StringBuilder();
                            for(int i=0;i<works.size();i++){
                                entire.append(works.get(i).getTime());
                                entire.append("\n");
                            }
                            tv_test.setText(entire);
                        }
                        else if(position>0 && position<size-1){
                            tv_test.setText(works.get(position-1).getTime());
                        }
                        else if(position == size-1){
                            tv_test.setText("근무지 추가");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workfragment, container,false);

        init(view);

        mcv.setTopbarVisible(false);
        mcv.addDecorator(calendar);
        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                toolbar.setTitle(mcv.getCurrentDate().getYear() +"년 "+ (mcv.getCurrentDate().getMonth() + 1) +"월");
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
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(mcv.getCurrentDate().getYear() +"년 "+ (mcv.getCurrentDate().getMonth() + 1) +"월");
        size = getWorkTitleSize();
    }

    public int getWorkTitleSize(){
        return workTitle.size();
    }


}
