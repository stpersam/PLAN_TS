package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
    public static final String HOME_KEY = "home";
    private Button plantadd;
    private Button plant1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_home_screen);

        plant1 = findViewById(R.id.home_plant1);
        plantadd = findViewById(R.id.home_plantadd);

        plant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, PlantDetail.class);
                i.putExtra(PlantDetail.PLANT_KEY,("Plant1"));
                startActivity(i);
            }
        });
        plantadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, NewPlant.class);
                startActivity(i);
            }
        });
    }
}