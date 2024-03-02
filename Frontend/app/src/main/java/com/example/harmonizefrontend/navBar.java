package com.example.harmonizefrontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;


public class navBar extends AppCompatActivity {

    private String fragment;
    private String password;
    private String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("fragment") != null) {
                fragment = intent.getStringExtra("fragment");
                if (fragment.equals("home")) {
                    loadFragment(new HomeFragment());
                } else if (fragment.equals("find")) {
                    loadFragment(new FindFragment());
                } else if (fragment.equals("messages")) {
                    loadFragment(new MessagesFragment());
                } else if (fragment.equals("profile")) {
                    loadFragment(new AccountPreferences());
                }
            }
            if (intent.getStringExtra("password") != null) {
                password = intent.getStringExtra("password");
            }
            if (intent.getStringExtra("jwtToken") != null) {
                jwtToken = intent.getStringExtra("jwtToken");
            }
        }

        //loadFragment(new FindFragment());


    }

    private NavigationBarView.OnItemSelectedListener navListener = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            if (item.getItemId() == R.id.navigation_home) {
                loadFragment(new HomeFragment());

            } else if (item.getItemId() == R.id.navigation_findUser) {
                loadFragment(new FindFragment());

            } else if (item.getItemId() == R.id.navigation_messages) {
                loadFragment(new MessagesFragment());
            } else if (item.getItemId() == R.id.navigation_profile) {
                loadFragment(new AccountPreferences());
            }


            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
};