package com.example.harmonizefrontend;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;


import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.harmonizefrontend.databinding.ActivityLoginScreenBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.*;

public class RegistrationScreen extends AppCompatActivity implements OnClickListener {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivityLoginScreenBinding binding;

    private String username, password, first, last, checkPass;
    private EditText firstNameEditText, lastNameEditText, usernameEditText, passwordEditText, reenterpasswordEditText;
    private Button loginButton;
    private Button registerButton;

    private String jwtToken;

    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);



        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        usernameEditText = findViewById(R.id.UsernameInput);
        passwordEditText = findViewById(R.id.PasswordInput);
        reenterpasswordEditText = findViewById(R.id.reenterPassword);

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("username") && intent.getStringExtra("username") != null) {
                username = intent.getStringExtra("username");
                usernameEditText.setText(username);
            }
            if (intent.hasExtra("password") && intent.getStringExtra("password") != null) {
                password = intent.getStringExtra("password");
                passwordEditText.setText(password);
            }
        }


        loginButton = findViewById(R.id.Login);
        loginButton.setOnClickListener(this);

        registerButton = findViewById(R.id.Register);
        registerButton.setOnClickListener(this);




    }



    public void onClick(View view) {
        if (view == loginButton) {
            Intent intent = new Intent(this, LoginScreen.class);
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);


        }
        else if (view == registerButton) {
            first = firstNameEditText.getText().toString();
            last = lastNameEditText.getText().toString();
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            checkPass = reenterpasswordEditText.getText().toString();

            firstNameEditText.setBackgroundTintList(null);
            lastNameEditText.setBackgroundTintList(null);
            usernameEditText.setBackgroundTintList(null);
            passwordEditText.setBackgroundTintList(null);
            reenterpasswordEditText.setBackgroundTintList(null);


            if (first.length() == 0 || last.length() == 0) {
                Toast.makeText(RegistrationScreen.this, "Name is required", Toast.LENGTH_LONG).show();

                firstNameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                lastNameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            }
            else if (username.length() == 0) {
                Toast.makeText(RegistrationScreen.this, "Username is required", Toast.LENGTH_LONG).show();
                usernameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            }
            else if ((password.length() < 8) || (!password.matches(".*[A-Z].*")) || (!password.matches(".*[!@#$%^&*()-+=?].*"))) {
                Toast.makeText(RegistrationScreen.this, "Please ensure password contains at least 8 characters, at least 1 upper case, and 1 special character", Toast.LENGTH_LONG).show();
                passwordEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            }
            else if (!password.equals(checkPass)) {
                Toast.makeText(RegistrationScreen.this, "Please ensure that the passwords match", Toast.LENGTH_LONG).show();
                reenterpasswordEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            }
            else {
//                Toast.makeText(RegistrationScreen.this, "Account has been successfully created!", Toast.LENGTH_LONG).show();
                register(username, password);

                if (jwtToken != null) {

                    Intent intent = new Intent(this, navbarPlaceholder.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("jwtToken", jwtToken);
                    startActivity(intent);
                }


            }
        }
    }

    private void
    register(String username, String password) {

        // Connect to backend in order to check if credentials are valid
        JSONObject jsonBody = new JSONObject();
        String checkCredsURL = "http://coms-309-032.class.las.iastate.edu:8080";

        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                checkCredsURL + "/auth/register",
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(RegistrationScreen.this, "Account Creation Successful", Toast.LENGTH_LONG).show();
                            jwtToken = response.getString("tokenType") + response.getString("accessToken");


                        }
                        catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrationScreen.this, "The username is already in use, please try again!", Toast.LENGTH_LONG).show();
                        jwtToken = null;
                        error.printStackTrace();
                    }
                }
        );
        mQueue.add(request);






    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }




}