package com.example.gowork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.example.gowork.dto.WorkInfo;
import com.example.gowork.viewModel.AuthViewModel;
import com.example.gowork.viewModel.DBViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivityTAG";

    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNav;
    OnBackPressedListener listener;
    private AuthViewModel authViewModel;
    private DBViewModel dbViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);

        authViewModel.getFirebaseUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    dbViewModel.getWorkInfoMutableLiveData().observe(MainActivity.this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            Log.d(TAG, "근무지 정보 관찰중");
//                if(workInfo != null)
//                Log.d(TAG, workInfo.getPlace_name());
                        }
                    });
                }
            }
        });

    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBackPressed() {
        if (listener != null) {
            listener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

}