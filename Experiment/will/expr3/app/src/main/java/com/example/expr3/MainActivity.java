package com.example.expr3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize TextView and EditText, find views in layout
        textView = (TextView) findViewById(R.id.textView);
        inputText = (EditText) findViewById(R.id.inputText);
    }

    //method is called when button is clicked
    public void updateText(View view) {

        //update text
        textView.setText("You are " + inputText.getText() + " years old.");

        System.out.println("Button Clicked.");

    }
}