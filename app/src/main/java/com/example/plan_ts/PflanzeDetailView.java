package com.example.plan_ts;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PflanzeDetailView extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    public static final String PLANT_KEY = "Plant";
    public static final String SESSIONID = "Plant_Session";
    public static final String USERNAME = "Plant_Name";
    public String session;
    public String name;
    public String plant;
    private ImageView Plant_Image;
    private Button Plant_back;
    private EditText plantname;
    private Spinner pflanzenart;
    private TextView luftfeuchtigkeit;
    private TextView giessen;
    private TextView topfgroesse;
    private TextView erde;
    private TextView licht;
    private String result;
    private String result2;
    private Button Plant_UPD;


    private List<Pflanze> userPflanzen = new ArrayList<>();
    private List<Pflanzenart> pflanzenartenListe = new ArrayList<>();
    List<Pflanzenart> tmp = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_plant_detail);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);
        plant = getIntent().getStringExtra(PLANT_KEY);

        Plant_Image = findViewById(R.id.Plant_Image);
        Plant_back = findViewById(R.id.Plant_back);
        plantname = findViewById(R.id.plantName);
        pflanzenart = findViewById(R.id.addPlant_spinner);
        luftfeuchtigkeit = findViewById(R.id.luftfeuchtigkeit);
        giessen = findViewById(R.id.giessen);
        topfgroesse = findViewById(R.id.topfgroesse);
        erde = findViewById(R.id.erde);
        licht = findViewById(R.id.licht);
        Plant_UPD = findViewById(R.id.Plant_UPD);

        String[] arraySpinner = getPflanzenarten();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pflanzenart.setAdapter(adapter);
        pflanzenart.setOnItemSelectedListener(PflanzeDetailView.this);

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
                for(Pflanzenart pflanzenarts : pflanzenartenListe){
                    if(pflanze.getPflanzeartname().equals(pflanzenarts.getBezeichnung())){
                        String x = (pflanze.Bild);
                        Context context = Plant_Image.getContext();
                        int id = context.getResources().getIdentifier(x, "drawable", context.getPackageName());
                        Plant_Image.setImageResource(id);

                        for(int i = 0; i < arraySpinner.length; i++){
                            if(pflanze.getPflanzeartname().equals(arraySpinner[i])){
                                pflanzenart.setSelection(i);
                                break;
                            }
                        }

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

    public String[] getPflanzenarten(){
        List<Pflanzenart> tmp = new ArrayList();
        String result2;

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
                tmp.add(gsonObjPA);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String[] pflanzenarten = new String[tmp.size()];
        for(int i = 0; i < tmp.size(); i++){
            pflanzenarten[i] = tmp.get(i).getBezeichnung();
        }
        return pflanzenarten;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String pflanzA = pflanzenart.getSelectedItem().toString();
        Pflanzenart pfl = null;
        String result2;

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
                tmp.add(gsonObjPA);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for(Pflanzenart x : tmp){
            if(pflanzA.equals(x.getBezeichnung())){
                pfl = x;
            }
        }
        if(pfl != null) {
            luftfeuchtigkeit.setText(pfl.getLuftfeuchtigkeit().toString());
            giessen.setText(pfl.getWasserzyklus().toString());
            topfgroesse.setText(pfl.getTopfgroesse().toString());
            erde.setText(pfl.getErde());
            licht.setText(pfl.getLichtbeduerfnisse());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}