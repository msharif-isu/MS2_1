package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private Button increaseBtn; // define increase button variable
    private Button decreaseBtn; // define decrease button variable
    private Button backBtn;     // define back button variable

    private Button resetBtn; // define reset button variable
    private int counter = 0;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        increaseBtn = findViewById(R.id.counter_increase_btn);
        decreaseBtn = findViewById(R.id.counter_decrease_btn);
        backBtn = findViewById(R.id.counter_back_btn);
        resetBtn = findViewById(R.id.counter_reset_btn);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);


        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < 100) {
                    numberTxt.setText(String.valueOf(++counter));
                    progressBar.setProgress(counter);
                }
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    numberTxt.setText(String.valueOf(--counter));
                    progressBar.setProgress(counter);
                }
            }
        });

        /* when reset btn is pressed, counter = 0, reset number textview */
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override

//            counter = 0;
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter = 0));
                progressBar.setProgress(counter);
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });

    }
}