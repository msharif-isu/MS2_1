package com.example.harmonizefrontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountPreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountPreferencesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView profilePicture;
    private String username, password, firstName, lastName, bio;
    private EditText usernameText, bioText, firstNameText, lastNameText;
    private TextView passwordView;
    private boolean hidden = true;
    private boolean allowEdit = false;
    private Button updatePrefsBtn, logoutBtn, delAccBtn;

    private ImageButton changePicBtn, editInfoBtn, unhidePass;

    private String jwtToken;

    private RequestQueue mQueue;

    private String URL = "http://coms-309-032.class.las.iastate.edu:8080";

    public AccountPreferencesFragment() {
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
    public static AccountPreferencesFragment newInstance(String param1, String param2) {
        AccountPreferencesFragment fragment = new AccountPreferencesFragment();
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

        profilePicture = rootView.findViewById(R.id.profile_Picture);

        usernameText = rootView.findViewById(R.id.usernameHolder);
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


        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            username = navBar.username;
            password = navBar.password;
            jwtToken = navBar.jwtToken;
            mQueue = navBar.mQueue;
        }

        Log.e("JWT", "username: " + username);
        Log.e("JWT", "password: " + password);
        Log.e("JWT", "jwtToken: " + jwtToken);



        // Get the rest of the user details from server
        Log.e("JWT", "Running getUserDetails");


        getUserDetails(new VolleyCallBack() {

            @Override
            public void onSuccess() {
                usernameText.setText(username);

                firstNameText.setText(firstName);

                lastNameText.setText(lastName);

                bioText.setText(bio);
                passwordView.setText(password);
            }

        });







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
        delAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send DELETE request to server
                deleteUser(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("JWT", "Inside Success");
                        Intent intent = new Intent(getActivity(), LoginScreen.class);
                        startActivity(intent);
                    }
                });

            }
        });

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

                    updateUserDetails(new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            Log.e("JWT", "Updated user details!");
                            usernameText.setText(username);
                            firstNameText.setText(firstName);
                            lastNameText.setText(lastName);
                            bioText.setText(bio);
                        }
                    });

                }

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void updateUserDetails(final VolleyCallBack callBackDetails) {

        JSONObject jsonBody = new JSONObject();
        try {
            if (!username.equals(usernameText.getText().toString())) {
                jsonBody.put("username", usernameText.getText().toString());
            }
            jsonBody.put("firstName", firstNameText.getText().toString());
            jsonBody.put("lastName", lastNameText.getText().toString());
            jsonBody.put("bio", bioText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL + "/users",
                jsonBody, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("JWT", "Updating user details!");
                            username = response.getString("username");
                            firstName = response.getString("firstName");
                            lastName = response.getString("lastName");
                            bio = response.getString("bio");
                            callBackDetails.onSuccess();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JWT", error.toString());
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", jwtToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        mQueue.add(jsonObjReq);

    };

    private void deleteUser(final VolleyCallBack delUserCallBack) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                URL + "/users",
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("JWT", "Deleting user");
                            Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_LONG).show();

                            delUserCallBack.onSuccess();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JWT", error.toString());
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", jwtToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        mQueue.add(jsonObjReq);



    }

    private void getUserDetails(final VolleyCallBack callBack) {

        Log.e("JWT", "inside the method");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL + "/users",
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("JWT", "Accessed user details");

                            username = response.getString("username");

                            firstName = response.getString("firstName");

                            lastName = response.getString("lastName");

                            bio = response.getString("bio");

                            callBack.onSuccess();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JWT", error.toString());
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", jwtToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        mQueue.add(jsonObjReq);
    }


}