package com.example.harmonizefrontend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import PictureData.SeePictureFragment;
import PictureData.SharedViewModel;
import UserInfo.Member;
import UserInfo.User;
import UserInfo.UserSession;
import messaging.chat.ReportMessageFragment;

/**
 * A simple {@link Fragment} subclass that allows users to see their info.
 * Use the {@link AccountPreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// Fragment for the Account Preferences screen which allows users to see their info
public class AccountPreferencesFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView profilePicture;
    private int id;
    private String username, password, firstName, lastName, bio;
    private EditText usernameText, bioText, firstNameText, lastNameText;
    private TextView passwordView;
    private boolean hidden = true;
    private boolean allowEdit = false;
    private Button updatePrefsBtn, logoutBtn, delAccBtn, seeReportsBtn, addSongsBtn;

    private ImageButton changePicBtn, editInfoBtn, unhidePass;

    private String jwtToken;

    private RequestQueue mQueue;

    private static Member currentUser;

    private String URL = "http://coms-309-032.class.las.iastate.edu:8080";
//    private String URL = "http://10.48.110.126";
    private SharedViewModel viewModel;


    /**
     * Creates a new instance of the Account Preferences fragment
     */
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

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe(this, this::updateData);
    }

    private void updateData(Bitmap data) {
        if (data == null) {
            //TODO: Set default image
            profilePicture.setImageResource(R.drawable.ic_launcher_foreground);
        }
        profilePicture.setImageBitmap(
                data);
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
        addSongsBtn = rootView.findViewById(R.id.addSongs);
        logoutBtn = rootView.findViewById(R.id.logOut);
        delAccBtn = rootView.findViewById(R.id.delAccount);
        changePicBtn = rootView.findViewById(R.id.changePicture);
        editInfoBtn = rootView.findViewById(R.id.editInfo);
        seeReportsBtn = rootView.findViewById(R.id.seeReports);
        seeReportsBtn.setVisibility(View.GONE);

        usernameText.setEnabled(allowEdit);
        firstNameText.setEnabled(allowEdit);
        lastNameText.setEnabled(allowEdit);
        bioText.setEnabled(allowEdit);


        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            username = navBar.username;
            password = navBar.password;
            jwtToken = navBar.jwtToken;
            mQueue = navBar.getQueue();
        }

        Log.e("JWT", "username: " + username);
        Log.e("JWT", "password: " + password);
        Log.e("JWT", "jwtToken: " + jwtToken);



        // Get the rest of the user details from server
        Log.e("JWT", "Running getUserDetails");
        makeImageRequest();

        getUserDetails(new VolleyCallBack() {

            @Override
            public void onSuccess() {
                usernameText.setText(username);

                firstNameText.setText(firstName);

                lastNameText.setText(lastName);

                bioText.setText(bio);
                passwordView.setText(password);

                Member currentUser = new Member(id, firstName, lastName, username, bio);
                UserSession.getInstance().setCurrentUser(currentUser);
                UserSession.getInstance().setPassword(password);
                UserSession.getInstance().setJwtToken(jwtToken);
                Log.d("jwt", "OG jwt: " + jwtToken);
                UserSession.getInstance().setmQueue(mQueue);
                Log.e("msg", currentUser.getUsername() + " " + currentUser.getFirstName() + " " + currentUser.getLastName() + " " + currentUser.getBio());
                checkRoles(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("Mod", "roles: " + String.valueOf(UserSession.getInstance().getRoles()));
                        if (UserSession.getInstance().getRoles().contains("MODERATOR")) {
                            seeReportsBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }

        });

        seeReportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((navBar) getActivity()).loadFragment(new SeeReportsFragment());
            }
        });


        //When changeBtn is clicked, give the user the option to upload their own picture and change the profile picture
        changePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageChooser();
                // TODO: Open picture fragment
                ((navBar) getActivity()).loadFragmentPopout(new SeePictureFragment());

            }
        });

        // When update preferences is clicked, take user to the change preferences screen
        updatePrefsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((navBar) getActivity()).loadFragment(new LikedSongsFragment());
            }
        });

        addSongsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((navBar) getActivity()).loadFragment(new SearchFragment());
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
                    editInfoBtn.setImageResource(R.drawable.edit_mode);
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
//                            usernameText.setText(username);
//                            firstNameText.setText(firstName);
//                            lastNameText.setText(lastName);
//                            bioText.setText(bio);

                            currentUser = new Member(id, firstName, lastName, username, bio);
                            UserSession.getInstance().setCurrentUser(currentUser);
                            UserSession.getInstance().setPassword(password);
                            UserSession.getInstance().setJwtToken(jwtToken);
                            Log.e("msg", currentUser.getUsername() + " " + currentUser.getFirstName() + " " + currentUser.getLastName() + " " + currentUser.getBio());
                            // SET ID TO MAX BECAUSE CURRENTLY DO NOT HAVE A REQUEST TO GET ID
                            editInfoBtn.setImageResource(R.drawable.finish_mode);
                        }
                    });



                }

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getUserDetails(VolleyCallBack callBack) {
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
                            id = response.getInt("id");

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

    private void makeImageRequest() {
        ImageRequest imageRequest = new ImageRequest(
                URL + "/users/icons", // Do change
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView

                        profilePicture.setImageBitmap(response);
                        Log.d("Image", response.toString());
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = "";
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                        Log.e("Image", body);
                        Log.e("Image", statusCode);
                        if (statusCode.equals("404")) {
                            profilePicture.setImageResource(R.drawable.ic_launcher_foreground);
                        }
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

        // Adding request to request queue
        mQueue.add(imageRequest);
    }

    private void deleteUser(VolleyCallBack volleyCallBack) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                URL + "/users",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("JWT", "Deleting user");
                            Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_LONG).show();

                            volleyCallBack.onSuccess();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            volleyCallBack.onSuccess(); // GOTTA FIGURE OUT WHY IM GETTING THIS ERROR, IT STILL ACTUALLY DELETES THE ACCOUNT

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
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
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

    private void updateUserDetails(VolleyCallBack callBackDetails) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstName", firstNameText.getText().toString());
            jsonBody.put("lastName", lastNameText.getText().toString());
            jsonBody.put("username", usernameText.getText().toString());
            jsonBody.put("bio", bioText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                UserSession.getInstance().getURL() + "/users",
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("JWT", "Updating user details!");
                            callBackDetails.onSuccess();
                            username = usernameText.getText().toString();
                            firstName = firstNameText.getText().toString();
                            lastName = lastNameText.getText().toString();
                            bio = bioText.getText().toString();

                        }
                        catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the exception
                        error.printStackTrace();

                        // Log detailed error information
                        if (error.networkResponse != null) {
                            // Get the status code
                            int statusCode = error.networkResponse.statusCode;
                            Log.e("JWT", "Status Code: " + statusCode);

                            // Try to convert byte[] data to a string
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e("JWT", "Response Body: " + responseBody);
                        } else {
                            Log.e("JWT", "No response from server");
                        }
                    }

                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Log.e("JWT header", UserSession.getInstance().getJwtToken());
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }

        };
        mQueue.add(jsonObjReq);
    }

    private void checkRoles(VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL + "/users/roles",
                null, // Pass null as the request body since it's a GET request
                response -> {
                    try {
                        List<String> roles = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject roleObject = response.getJSONObject(i);
                            String role = roleObject.getString("name");
                            Log.e("roles", "User has role: " + role);
                            roles.add(role);
                        }
                        UserSession.getInstance().setRoles(roles);
                        volleyCallBack.onSuccess();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("report", error.toString())
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
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
        mQueue.add(jsonArrayRequest);
    }
}





