package com.example.harmonizefrontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.view.MenuItem;

import Connections.VolleySingleton;

/**
 * Controls the navigation bar at the bottom of the screen. If the user clicks on an icon on the
 * menu, it should load the fragment that they selected.
 */
public class navBar extends AppCompatActivity {

    private String fragment;

    protected String username;
    protected String password;
    public String jwtToken;

    protected RequestQueue mQueue;

    /**
     * Listens to which item the user selects on the nav bar. Then loads the fragment.
     */
    NavigationBarView.OnItemSelectedListener navListener = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            if (item.getItemId() == R.id.navigation_home) {
//                loadFragment(new HomeFragment());
                loadFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.navigation_findUser) {
                loadFragment(new FindFragment());

            } else if (item.getItemId() == R.id.navigation_messages) {
//                loadFragment(new MessageFragment());
                loadFragment(new ConversationsFragment());
            } else if (item.getItemId() == R.id.navigation_profile) {
                loadFragment(new AccountPreferencesFragment());
            }


            return true;
        }
    };


    /**
     * Creates the bottomNavigationView.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();


        Intent intent = getIntent();
        if (intent != null) {

            if (intent.getStringExtra("username") != null) {
                username = intent.getStringExtra("username");
            }
            if (intent.getStringExtra("password") != null) {
                password = intent.getStringExtra("password");
            }
            if (intent.getStringExtra("jwtToken") != null) {
                jwtToken = intent.getStringExtra("jwtToken");
            }

            if (intent.getStringExtra("fragment") != null) {
                fragment = intent.getStringExtra("fragment");


                switch (fragment) {
                    case "home":
//                        loadFragment(new HomeFragment());
                        loadFragment(new SeeReportsFragment());
                        break;
                    case "find":
                        loadFragment(new FindFragment());
                        break;
                    case "messages":
//                        loadFragment(new MessageFragment());
                        loadFragment(new ConversationsFragment());
                        break;
                    case "profile":
                        loadFragment(new AccountPreferencesFragment());
                        break;
                }

            }


        }


    }

    /**
     * loads the fragment passed into the method.
     * @param fragment
     */
    void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // This is for the popout fragment, currently protected, is that a good idea?
    protected void loadFragmentPopout(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.popout_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null); // Important for adding multiple fragments to the same container
        // The order which we add fragments to the backstack is the order in which they are popped off
        fragmentTransaction.commit();
    }

    public RequestQueue getQueue() {
        return mQueue;
    }
}
