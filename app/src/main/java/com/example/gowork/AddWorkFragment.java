package com.example.gowork;

import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddWorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddWorkFragment extends Fragment {

    TextInputEditText edt_place_name, edt_work_location;
    Spinner sp_date;
    MaterialButtonToggleGroup group_btn_every;

    String[] res_date;
    ArrayAdapter dateAdapter;

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

        res_date = getResources().getStringArray(R.array.date);
        dateAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, res_date);
        sp_date.setAdapter(dateAdapter);

        edt_work_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_addWorkFragment_to_searchAddressFragment);
            }
        });

//        group_btn_every.add

        return view;
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void init(View view){
        group_btn_every = view.findViewById(R.id.group_btn_every);
        edt_place_name = view.findViewById(R.id.edt_place_name);
        edt_work_location = view.findViewById(R.id.edt_work_location);
        sp_date = view.findViewById(R.id.sp_date);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        hideBottomNavigation(false);
//    }
}