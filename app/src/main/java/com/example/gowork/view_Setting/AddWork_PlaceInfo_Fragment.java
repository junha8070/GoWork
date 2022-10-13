package com.example.gowork.view_Setting;

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

import com.example.gowork.R;
import com.example.gowork.dto.WorkInfo;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class AddWork_PlaceInfo_Fragment extends Fragment {

    private String TAG = "AddWork_PlaceInfo_FragmentTAG";

    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    TextInputEditText edt_place_name, edt_work_location, edt_kind, edt_pay;
    MaterialButton btn_finish;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddWork_PlaceInfo_Fragment() {
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
    public static AddWork_PlaceInfo_Fragment newInstance(String param1, String param2) {
        AddWork_PlaceInfo_Fragment fragment = new AddWork_PlaceInfo_Fragment();
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
                String kind = result.getString("kind");

                edt_place_name.setText(place_name);
                edt_work_location.setText(address);
                edt_kind.setText(kind);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_work__placeinfo, container, false);

        init(view);

        edt_work_location.setOnClickListener(getPlaceInfo -> Navigation.findNavController(getView()).navigate(R.id.action_addWorkFragment_to_searchAddressFragment));

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place_name  = edt_place_name.getText().toString();
                String address = edt_work_location.getText().toString();
                String kind = edt_kind.getText().toString();
                String pay = edt_pay.getText().toString();

                if(place_name.isEmpty()||pay.isEmpty()){
                    Toast.makeText(getContext(), "필수 정보를 채워주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle result = new Bundle();
                result.putString("place_name", place_name);
                result.putString("address", address);
                result.putString("kind", kind);
                result.putString("pay", pay);
                getParentFragmentManager().setFragmentResult("PlaceInfo", result);

                Navigation.findNavController(getView()).navigate(R.id.action_addWorkFragment_to_addWork_JoinResign_Fragment);
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
        edt_kind = view.findViewById(R.id.edt_kind);
        edt_pay = view.findViewById(R.id.edt_pay);
        btn_finish = view.findViewById(R.id.btn_finish);
    }
}