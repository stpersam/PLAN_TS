package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PflanzeDetailView extends AppCompatActivity {
    public static final String PLANT_KEY = "Plant";
    public static final String SESSIONID = "Plant_Session";
    public static final String USERNAME = "Plant_Name";
    public String session;
    public String name;
    public String plant;
    private Button Plant_back;
    private TextView plantname;
    private Spinner pflanzenart;
    private TextView luftfeuchtigkeit;
    private TextView giessen;
    private TextView topfgroesse;
    private TextView erde;
    private TextView licht;
    private String result;
    private String result2;
    private List<Pflanze> userPflanzen = new ArrayList<>();
    private List<Pflanzenart> pflanzenartenListe = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_plant_detail);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);
        plant = getIntent().getStringExtra(PLANT_KEY);

        Plant_back = findViewById(R.id.Plant_back);
        plantname = findViewById(R.id.plantName);
        pflanzenart = findViewById(R.id.addPlant_spinner);
        luftfeuchtigkeit = findViewById(R.id.luftfeuchtigkeit);
        giessen = findViewById(R.id.giessen);
        topfgroesse = findViewById(R.id.topfgroesse);
        erde = findViewById(R.id.erde);
        licht = findViewById(R.id.licht);

        try {
            APIGET apiget = new APIGET(name, session, "GetUserPflanzen");
            Thread thread = new Thread(apiget);
            thread.start();
            thread.join();
            result = apiget.getResult();
            result = result.replace("}{", "};{");
            String[] tmp = result.split(";");

            Gson gsonPF = new Gson();
            for (String a : tmp){
                Pflanze gsonObjPF = gsonPF.fromJson(a, Pflanze.class);
                userPflanzen.add(gsonObjPF);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            APIGET apigetPA = new APIGET(name, session, "GetPflanzenArten");
            Thread threadPA = new Thread(apigetPA);
            threadPA.start();
            threadPA.join();
            result2 = apigetPA.getResult();
            result2 = result2.replace("}{", "};{");
            String[] tmp2 = result2.split(";");

            Gson gsonPA = new Gson();
            for (String x : tmp2){
                Pflanzenart gsonObjPA = gsonPA.fromJson(x, Pflanzenart.class);
                pflanzenartenListe.add(gsonObjPA);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for(Pflanze pflanze : userPflanzen){
            if(pflanze.getPflanzenID() == Integer.parseInt(plant)) {
                plantname.setText(pflanze.getPflanzenname());
                for(Pflanzenart pflanzenart : pflanzenartenListe){
                    if(pflanze.getPflanzeartname().equals(pflanzenart.getBezeichnung())){
                        luftfeuchtigkeit.setText(pflanzenart.getLuftfeuchtigkeit().toString());
                        giessen.setText(pflanzenart.getWasserzyklus().toString());
                        topfgroesse.setText(pflanzenart.getTopfgroesse().toString());;
                        erde.setText(pflanzenart.getErde());;
                        licht.setText(pflanzenart.getLichtbeduerfnisse());
                        break;
                    }
                }
                break;
            }
        }

        Plant_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}