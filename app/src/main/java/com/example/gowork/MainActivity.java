package com.example.gowork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNav;
    OnBackPressedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    public void setOnBackPressedListener(OnBackPressedListener listener){
        this.listener = listener;
    }

    @Override
    public void onBackPressed() {
        if(listener!=null){
            listener.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

}