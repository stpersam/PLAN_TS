package com.example.plan_ts;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class NewPflanzeView extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    private Button newPlant_back;
    public static final String SESSIONID = "newPlantID";
    public static final String USERNAME = "newPlantName";
    public static final String  GRUPPENNAME = "newPlantGruppenname";
    public String session;
    public String name;
    public String gruppenname;

    private EditText plantName;
    private Spinner addPlant_spinner;
    private TextView newluftfeuchtigkeit;
    private TextView newgiessen;
    private TextView newtopfgroesse;
    private TextView newerde;
    private TextView newlicht;
    private Button newPlant_ADD;

    List<Pflanzenart> tmp = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_new_plant);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);
        gruppenname = getIntent().getStringExtra(GRUPPENNAME);

        newPlant_back = findViewById(R.id.newPlant_back);
        plantName = findViewById(R.id.plantName);
        addPlant_spinner = findViewById(R.id.addPlant_spinner);
        newluftfeuchtigkeit = findViewById(R.id.newluftfeuchtigkeit);
        newgiessen = findViewById(R.id.newgiessen);
        newtopfgroesse = findViewById(R.id.newtopfgroesse);
        newerde = findViewById(R.id.newerde);
        newlicht = findViewById(R.id.newlicht);
        newPlant_ADD = findViewById(R.id.newPlant_ADD);

        String[] arraySpinner = getPflanzenarten();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addPlant_spinner.setAdapter(adapter);
        addPlant_spinner.setOnItemSelectedListener(NewPflanzeView.this);

        newPlant_ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNewPlant();
            }
        });

        newPlant_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String pflanzenart = addPlant_spinner.getSelectedItem().toString();
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
            if(pflanzenart.equals(x.getBezeichnung())){
                pfl = x;
            }
        }
        if(pfl != null) {
            newluftfeuchtigkeit.setText(pfl.getLuftfeuchtigkeit().toString());
            newgiessen.setText(pfl.getWasserzyklus().toString());
            newtopfgroesse.setText(pfl.getTopfgroesse().toString());
            newerde.setText(pfl.getErde());
            newlicht.setText(pfl.getLichtbeduerfnisse());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

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

    public static String now() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.GERMANY);
        return sdf.format(date);
    }

    public void postNewPlant(){
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

        String pflanzenname = plantName.getText().toString();
        String bild= "plant1";
        String gegossen = now();
        String groesse= newtopfgroesse.getText().toString();
        String pflanzenart= addPlant_spinner.getSelectedItem().toString();
        try {
            String jsonLoginData = "\"user\":\"" + name + "\",\"sessionid\":" + session +"";
            String jsonBody = "{\"Pflanzenname\":\"" + pflanzenname + "\",\"Bild\":\"" + bild +"\",\"Gegossen\":\"" + gegossen + "\",\"Groesse\":\"" + groesse + "\",\"Username\":\"" + name + "\",\"Pflanzeartname\":\"" + pflanzenart + "\",\"Gruppenname\":\"" + gruppenname +"\"}";
            String json = "{\"pflanze\":" + jsonBody + ",\"usd\":{" + jsonLoginData +"}}";
            System.out.println(json);

            URL url = new URL("https://10.0.0.152:45455/api/Plan_ts/AddPflanze");
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

            InputStream in = httpConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            String out = "";

            if(scanner.hasNext()){
                out = scanner.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}