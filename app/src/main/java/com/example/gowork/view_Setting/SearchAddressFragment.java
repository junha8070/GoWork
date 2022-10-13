package com.example.gowork.view_Setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gowork.R;
import com.example.gowork.RecyclerViewDecoration;
import com.example.gowork.dto.KakaoAddressRequest;
import com.example.gowork.dto.KakaoAddressResponse;
import com.example.gowork.viewModel.AddressViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchAddressFragment extends Fragment {

    private String TAG = "SearchAddressFragment";

    private TextInputEditText edt_search_address;
    private RecyclerView rv_address;
    private MaterialToolbar materialToolbar;
    private TextView tv_none_result;

    private Address_Adapter address_adapter;

    private AddressViewModel addressViewModel;
    private KakaoAddressRequest kakaoAddressRequest;

    private KakaoAddressResponse kakaoAddressResponse;

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

        hideBottomNavigation(true);

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

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_searchAddressFragment_to_addWorkFragment);
            }
        });

        addressViewModel.getAddressInfo().observe(getActivity(), new Observer<KakaoAddressResponse>() {
            @Override
            public void onChanged(KakaoAddressResponse kakaoAddressResponse) {
                if (kakaoAddressResponse != null) {
                    tv_none_result.setVisibility(View.GONE);
                    rv_address.setVisibility(View.VISIBLE);
                    address_adapter = new Address_Adapter(kakaoAddressResponse);

                    rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_address.setAdapter(address_adapter);

                    address_adapter.setOnItemClickListener(new Address_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Toast.makeText(getContext(), "position", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, String.valueOf(position));
                            String place_name = addressViewModel.getAddressInfo().getValue().getKakaoAddressDocumentsPojos().get(position).getPlace_name();
                            String address = addressViewModel.getAddressInfo().getValue().getKakaoAddressDocumentsPojos().get(position).getAddress_name();
                            String kind = addressViewModel.getAddressInfo().getValue().getKakaoAddressDocumentsPojos().get(position).getCategory_name();
                            int kindSpaceIndex = kind.indexOf(" ");
                            kind = kind.substring(0, kindSpaceIndex);

                            Bundle result = new Bundle();
                            result.putString("place_name", place_name);
                            result.putString("address", address);
                            result.putString("kind", kind);
                            getParentFragmentManager().setFragmentResult("find_address_result", result);

                            Navigation.findNavController(getView()).navigate(R.id.action_searchAddressFragment_to_addWorkFragment);
                        }
                    });
//                    Log.d(TAG, "주소 반환" + kakaoAddressResponse.getKakaoAddressDocumentsPojos().get(0).getAddress_name());
                } else if (kakaoAddressResponse.getKakaoAddressDocumentsPojos().size() == 0 || kakaoAddressResponse.getKakaoAddressDocumentsPojos() == null || kakaoAddressResponse.getKakaoAddressDocumentsPojos().isEmpty()) {
                    rv_address.setVisibility(View.GONE);
                    tv_none_result.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_search_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.d(TAG, "검색어 : " + textView.getText().toString() + " 뭐지 : " + i + " 누른 키 : " + keyEvent);
                if (!textView.getText().toString().isEmpty()) {
                    Log.d(TAG, "주소 끌고오는 중");
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
        address_adapter = new Address_Adapter(kakaoAddressResponse);
        materialToolbar = view.findViewById(R.id.materialToolbar);
        tv_none_result = view.findViewById(R.id.tv_none_result);
        edt_search_address = view.findViewById(R.id.edt_search_address);
        rv_address = view.findViewById(R.id.rv_address);
        rv_address.addItemDecoration(new RecyclerViewDecoration(30));
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        hideBottomNavigation(false);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        hideBottomNavigation(true);
//    }
}