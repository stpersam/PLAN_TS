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

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class PflanzeDetailView extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    public static final String PLANT_KEY = "Plant";
    public static final String SESSIONID = "Plant_Session";
    public static final String USERNAME = "Plant_Name";
    public static final String  GRUPPENNAME = "PlantGruppenname";
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
    public String gruppenname;

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
        gruppenname = getIntent().getStringExtra(GRUPPENNAME);

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

        Plant_UPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPost();
            }
        });

    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        return sdf.format(date);
    }

    private void sendPost() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {}
        String pflanzenname = plantname.getText().toString();
        String bild= "plant1";
        String gegossen = now();
        String groesse= topfgroesse.getText().toString();
        String pflA= pflanzenart.getSelectedItem().toString();
        try {
            String jsonLoginData = "\"user\":\"" + name + "\",\"sessionid\":" + session +"";
            String jsonBody = "{\"Pflanzenname\":\"" + pflanzenname + "\",\"Bild\":\"" + bild +"\",\"Gegossen\":\"" + gegossen + "\",\"Groesse\":\"" + groesse + "\",\"Username\":\"" + name + "\",\"Pflanzeartname\":\"" + pflanzenart + "\",\"Gruppenname\":\"" + gruppenname +"\"}";
            String json = "{\"pflanze\":" + jsonBody + ",\"usd\":{" + jsonLoginData +"}}";
            System.out.println(json);

            URL url = new URL("https://10.0.0.152:45455/api/Plan_ts/EditPflanze");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json;");
            httpConnection.setRequestProperty("Accept", "text/plain");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            OutputStream os = httpConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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