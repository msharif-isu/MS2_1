package com.example.harmonizefrontend;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Objects;

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
    private EditText usernameText, bioText, firstNameText, lastNameText;
    private TextView passwordView;
    private boolean hidden = true;
    private boolean allowEdit = false;
    private Button updatePrefsBtn, logoutBtn, delAccBtn;

    private ImageButton changePicBtn, editInfoBtn, unhidePass;

    private String jwtToken;

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

        usernameText = rootView.findViewById(R.id.username);
        passwordView = rootView.findViewById(R.id.password_text_view);

        firstNameText = rootView.findViewById(R.id.firstname);
        lastNameText = rootView.findViewById(R.id.lastname);

        unhidePass = rootView.findViewById(R.id.hideunhide);

        bioText = rootView.findViewById(R.id.bio);

        updatePrefsBtn = rootView.findViewById(R.id.updatePrefs);
        logoutBtn = rootView.findViewById(R.id.logOut);
        delAccBtn = rootView.findViewById(R.id.delAccount);
        changePicBtn = rootView.findViewById(R.id.changePicture);
        editInfoBtn = rootView.findViewById(R.id.editInfo);

        usernameText.setEnabled(allowEdit);
        firstNameText.setEnabled(allowEdit);
        lastNameText.setEnabled(allowEdit);
        bioText.setEnabled(allowEdit);

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra("password") && intent.getStringExtra("password") != null) {
                String password = intent.getStringExtra("password");
                passwordView.setText(password);
            }
            if (intent.hasExtra("jwtToken") && intent.getStringExtra("jwtToken") != null) {
                jwtToken = intent.getStringExtra("jwtToken");
            }
        }

        // Get the rest of the user details from server
        getUserDetails();





        //When changeBtn is clicked, give the user the option to upload their own picture and change the profile picture
//        changePicBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Give user option to upload their own picture
//
//
//            }
//        });

        // When update preferences is clicked, take user to the change preferences screen
//        updatePrefsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Send PUT request to server
//            }
//        });

        // When logout is clicked, change intent to login screen
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
            }
        });

        // When delete account is clicked, send DELETE request to server given username and password
//        delAccBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Send DELETE request to server
//            }
//        });

        unhidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hidden) {
                    passwordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hidden = false;
                }
                else {
                    passwordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidden = true;
                }

            }
        });

        // When this button is clicked, all user information is made into an EditText and the user can edit their information
        editInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowEdit == false) {
                    allowEdit = true;
                    usernameText.setEnabled(allowEdit);
                    firstNameText.setEnabled(allowEdit);
                    lastNameText.setEnabled(allowEdit);
                    bioText.setEnabled(allowEdit);
                }
                else {
                    allowEdit = false;
                    usernameText.setEnabled(allowEdit);
                    firstNameText.setEnabled(allowEdit);
                    lastNameText.setEnabled(allowEdit);
                    bioText.setEnabled(allowEdit);

                    String checkCredsURL = "http://coms-309-032.class.las.iastate.edu:8080";

//                    Send all modified data to server
//                    JSONObject jsonbody = new JSONObject();
//                    try {
//                        jsonbody.put("username", usernameText.getText().toString());
//                        jsonbody.put("firstName", firstNameText.getText().toString());
//                        jsonbody.put("lastName", lastNameText.getText().toString());
//                        jsonbody.put("bio", bioText.getText().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    JsonObjectRequest request = new JsonObjectRequest(
//                            Request.Method.PUT,
//                            checkCredsURL + "/user",
//
//                    )
                }

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getUserDetails() {
        // Send GET request to server to get user details
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("jwtToken", jwtToken);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String checkCredsURL = "http://coms-309-032.class.las.iastate.edu:8080";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                checkCredsURL + "",
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        )

    }

    
}