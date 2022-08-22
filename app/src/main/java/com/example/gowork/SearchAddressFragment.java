package com.example.gowork;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gowork.DTO.KakaoAddressRequest;
import com.example.gowork.DTO.KakaoAddressResponse;
import com.example.gowork.ViewModel.AddressViewModel;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchAddressFragment extends Fragment {

    private String TAG = "SearchAddressFragment";

    TextInputEditText edt_search_address;

    private AddressViewModel addressViewModel;
    private KakaoAddressRequest kakaoAddressRequest;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchAddressFragment newInstance(String param1, String param2) {
        SearchAddressFragment fragment = new SearchAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addressViewModel = new ViewModelProvider(requireActivity()).get(AddressViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_address, container, false);

        init(view);

        addressViewModel.getAddressInfo().observe(getActivity(), new Observer<KakaoAddressResponse>() {
            @Override
            public void onChanged(KakaoAddressResponse kakaoAddressResponse) {
                if (kakaoAddressResponse.getKakaoAddressDocumentsPojos().get(0) != null) {
                    Log.d(TAG, kakaoAddressResponse.getKakaoAddressDocumentsPojos().get(0).getAddress_name());
                }
            }
        });

        edt_search_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.d(TAG, "검색어 : " + textView.getText().toString() + " 뭐지 : " + i + " 누른 키 : " + keyEvent);
                if (!textView.getText().toString().isEmpty()) {
                    kakaoAddressRequest = new KakaoAddressRequest();
                    kakaoAddressRequest.setQuery(textView.getText().toString());
                    addressViewModel.responseAddressInfo(kakaoAddressRequest);
                }

//                addressViewModel.responseAddressInfo();
                return false;
            }
        });


        return view;
    }

    private void init(View view) {
        edt_search_address = view.findViewById(R.id.edt_search_address);
    }
}