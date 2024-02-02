package com.example.expr2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Array of items to be displayed
    String[] item = {"One", "Two", "Three", "Four", "Five"};

    //Declaring variables
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize AutoCompleteTextView and ArrayAdapter
        autoCompleteTextView = findViewById(R.id.yuh);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        //autoCompleteTextView provides a dropdown menu based on input text. As they type it will autofill.
        autoCompleteTextView.setAdapter(adapterItems);

        //Set an item click listener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {

                //retrieve the selected item
                String item = adapterView.getItemAtPosition(i).toString();

                //Display a message with the selected item
                Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();

            }
        });
    }
}