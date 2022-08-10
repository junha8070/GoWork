package com.example.gowork;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_UserInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_UserInfo extends Fragment {

    private MaterialToolbar materialToolbar;
    private TextInputEditText edt_id, edt_pw, edt_newpw, edt_repw, edt_name;
    private MaterialButton btn_finish;

    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    private String id, pw, newPw, rePw, name;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Edit_UserInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_UserInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_UserInfo newInstance(String param1, String param2) {
        Edit_UserInfo fragment = new Edit_UserInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit__user_info, container, false);

        init(view);

        id = dbViewModel.getUserInfoLiveData().getValue().id;
        name = dbViewModel.getUserInfoLiveData().getValue().name;

        edt_id.setText(id);
        edt_name.setText(name);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void init(View view){
        materialToolbar = view.findViewById(R.id.materialToolbar);
        edt_id = view.findViewById(R.id.edt_id);
        edt_pw = view.findViewById(R.id.edt_pw);
        edt_newpw = view.findViewById(R.id.edt_newpw);
        edt_repw = view.findViewById(R.id.edt_repw);
        edt_name = view.findViewById(R.id.edt_name);
        btn_finish = view.findViewById(R.id.btn_finish);
    }
}