package com.example.harmonizefrontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountPreferences#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountPreferences extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView profilePicture;
    private TextView username, password, bio;
    private TextView firstName, lastName;
    private boolean hidden = true;
    private Button updatePrefsBtn, logoutBtn, delAccBtn;

    private ImageButton changePicBtn, editInfoBtn, unhidePass;

    public AccountPreferences() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountPreferences.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountPreferences newInstance(String param1, String param2) {
        AccountPreferences fragment = new AccountPreferences();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_preferences, container, false);

        profilePicture = rootView.findViewById(R.id.profilepicture);

        username = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password_text_view);

        firstName = rootView.findViewById(R.id.firstname);
        lastName = rootView.findViewById(R.id.lastname);

        unhidePass = rootView.findViewById(R.id.hideunhide);

        bio = rootView.findViewById(R.id.bio);

        updatePrefsBtn = rootView.findViewById(R.id.updatePrefs);
        logoutBtn = rootView.findViewById(R.id.logOut);
        delAccBtn = rootView.findViewById(R.id.delAccount);
        changePicBtn = rootView.findViewById(R.id.changePicture);





        //When changeBtn is clicked, give the user the option to upload their own picture and change the profile picture
        changePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Give user option to upload their own picture


            }
        });

        // When update preferences is clicked, take user to the change preferences screen
        updatePrefsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send PUT request to server
            }
        });

        // When logout is clicked, change intent to login screen
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
            }
        });

        // When delete account is clicked, send DELETE request to server given username and password
        delAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send DELETE request to server
            }
        });

        unhidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hidden) {
                    password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hidden = false;
                }
                else {
                    password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hidden = true;
                }

            }
        });

        // When this button is clicked, all user information is made into an EditText and the user can edit their information
        editInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getUserDetails() {
        // Send GET request to server to get user details

    }

    
}