package com.example.harmonizefrontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            /*

            I can't get the switch case method to work. Please help lol. -Will

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_findUser:
                    selectedFragment = new FindFragment();
                    break;
                case R.id.navigation_messages:
                    selectedFragment = new MessagesFragment();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
             */

            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new HomeFragment();

            } else if (item.getItemId() == R.id.navigation_findUser) {
                selectedFragment = new FindFragment();

            } else if (item.getItemId() == R.id.navigation_messages) {
                selectedFragment = new MessagesFragment();

            } else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();

            return true;

        }
    };
}