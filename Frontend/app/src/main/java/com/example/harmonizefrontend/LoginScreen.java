package com.example.harmonizefrontend;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

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

            checkCredentials(username, password);

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

    private void checkCredentials(String username, String password) {
        if (username.length() == 0) {
            Toast.makeText(LoginScreen.this, "Username is required", Toast.LENGTH_LONG).show();
            usernameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

        }
        else if (password.length() == 0) {
            Toast.makeText(LoginScreen.this, "password is required", Toast.LENGTH_LONG).show();
            passwordEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        }
        // Connect to backend in order to check if credentials are valid

    }


}