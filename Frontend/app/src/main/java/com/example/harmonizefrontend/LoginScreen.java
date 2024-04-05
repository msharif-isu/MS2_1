package com.example.harmonizefrontend;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.RequestQueue;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;


import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import Connections.VolleyCallBack;
import Connections.VolleySingleton;

/**
 * A login screen that allows users to enter via username/password.
 */
public class LoginScreen extends AppCompatActivity implements OnClickListener {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivityLoginScreenBinding binding;

    private String username, password;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox rememberMe;
    private Button loginButton;
    private Button registerButton;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    private RequestQueue mQueue;

    private String jwtToken = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);



        usernameEditText = findViewById(R.id.UsernameInput);
        passwordEditText = findViewById(R.id.PasswordInput);

        rememberMe = findViewById(R.id.checkBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        loginButton = findViewById(R.id.Login);
        loginButton.setOnClickListener(this);

        registerButton = findViewById(R.id.Register);
        registerButton.setOnClickListener(this);

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();

        // Checks to see if the user has come from the registration screen/logged out
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

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            usernameEditText.setText(loginPreferences.getString("username", ""));
            passwordEditText.setText(loginPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }


    }

    public void onClick(View view) {
        if (view == loginButton) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(usernameEditText.getWindowToken(), 0);

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            if (rememberMe.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            usernameEditText.setBackgroundTintList(null);
            passwordEditText.setBackgroundTintList(null);

            if (username.length() == 0) {
                Toast.makeText(LoginScreen.this, "Username is required", Toast.LENGTH_LONG).show();
                usernameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            }
            else if (password.length() == 0) {
                Toast.makeText(LoginScreen.this, "password is required", Toast.LENGTH_LONG).show();
                passwordEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
            else {
                checkCredentials(username, password, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(LoginScreen.this, navBar.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        intent.putExtra("jwtToken", jwtToken);
                        intent.putExtra("fragment", "profile");

                        startActivity(intent);
                    }
                });


            }

        }
        else if (view == registerButton) {
            Intent intent = new Intent(this, RegistrationScreen.class);
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    /**
     * Used to check if user entered credentials is valid
     * @param username
     * @param password
     * @param callback
     */
    private void checkCredentials(String username, String password, final VolleyCallBack callback) {

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
                checkCredsURL + "/auth/login",
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                                Log.e("JWT", response.getString("tokenType") + response.getString("accessToken"));
                                jwtToken = response.getString("tokenType") + response.getString("accessToken");


                            callback.onSuccess();


                                // Returns Http.status.ok
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginScreen.this, "The username or password is incorrect, please try again!", Toast.LENGTH_LONG).show();
                        jwtToken = null;
//                        Log.e("JWT", error.toString());
                    }
                }
        );
        mQueue.add(request);
    }


}