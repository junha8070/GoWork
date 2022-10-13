package com.example.gowork.view_Setting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gowork.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddWork_JoinResign_Fragment extends Fragment {

    private String TAG = "AddWork_JoinResign_FragmentTAG";

    Calendar_BottomSheet_Fragment bottom_Calendar_Fragment;
    Calendar calendar;

    TextInputEditText edt_join, edt_resign;
    MaterialButton btn_notSetResign, btn_next;

    Bundle result;

    String place_name;
    String address;
    String kind;
    String pay;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddWork_JoinResign_Fragment() {
        // Required empty public constructor
    }

    public static AddWork_JoinResign_Fragment newInstance(String param1, String param2) {
        AddWork_JoinResign_Fragment fragment = new AddWork_JoinResign_Fragment();
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

        getParentFragmentManager().setFragmentResultListener("PlaceInfo", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                place_name = result.getString("place_name");
                address = result.getString("address");
                kind = result.getString("kind");
                pay = result.getString("pay");

                Log.d(TAG, " 근무지 : " + place_name + " 주소 : " + address + " 종류 : " + kind + " 급여 : " + pay);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_work__join_resign_, container, false);

        init(view);

        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        edt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("입사일")
                        .setSelection(today)
                        .build();

                datePicker.show(getActivity().getSupportFragmentManager(), getTag());
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                        Date date = new Date();
                        date.setTime(selection);

                        String dateString = simpleDateFormat.format(date);

                        edt_join.setText(dateString);
                    }
                });
            }
        });

        edt_resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("퇴사일")
                        .setSelection(today)
                        .build();

                datePicker.show(getActivity().getSupportFragmentManager(), getTag());
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                        Date date = new Date();
                        date.setTime(selection);

                        String dateString = simpleDateFormat.format(date);

                        edt_resign.setText(dateString);
                    }
                });
            }
        });

        btn_notSetResign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_resign.setText("미지정");
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_join.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "입사일을 지정해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_resign.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "퇴사일을 지정해주세요.\n퇴사일을 모르면 퇴사일 밑 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String join = edt_join.getText().toString();
                String resign = edt_resign.getText().toString();

                result = new Bundle();
                result.putString("place_name", place_name);
                result.putString("address", address);
                result.putString("kind", kind);
                result.putString("pay", pay);
                result.putString("join", join);
                result.putString("resign", resign);

                getParentFragmentManager().setFragmentResult("result", result);
                Navigation.findNavController(getView()).navigate(R.id.action_addWork_JoinResign_Fragment_to_addWork_Schedule_Fragment);
            }
        });

        return view;
    }

    private void init(View view) {
        bottom_Calendar_Fragment = new Calendar_BottomSheet_Fragment();
        edt_join = view.findViewById(R.id.edt_join);
        edt_resign = view.findViewById(R.id.edt_resign);
        btn_notSetResign = view.findViewById(R.id.btn_notSetResign);
        btn_next = view.findViewById(R.id.btn_next);
    }
}