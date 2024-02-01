package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = (TextView)findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        messageText.setText("CLICK ME");
        messageText.setTextColor(Color.BLUE);
        messageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageText.setText("This is my first...");
                messageText.setTextColor(Color.RED);

                Toast.makeText(MainActivity.this, "Com S 309 Experiment!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}