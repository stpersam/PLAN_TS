package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class NewPflanzeView extends AppCompatActivity {
    private Button newPlant_back;
    public static final String SESSIONID = "home";
    public static final String USERNAME = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_new_plant);

        newPlant_back = findViewById(R.id.newPlant_back);
        newPlant_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}