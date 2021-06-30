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

import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
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

public class PflanzeDetailView extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    public static final String PLANT_KEY = "Plant";
    public static final String SESSIONID = "Plant_Session";
    public static final String USERNAME = "Plant_Name";
    public static final String GRUPPENNAME = "PlantGruppenname";
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
    private Spinner group_spinner;
    private Slider bewaesserung;

    private Button Plant_DEL;
    public String gruppenname;

    private List<Pflanze> userPflanzen = new ArrayList<>();
    private List<Pflanzenart> pflanzenartenListe = new ArrayList<>();
    List<Pflanzenart> tmp = new ArrayList();
    List<Gruppe> tmpgruppe = new ArrayList();
    public Pflanze detailplant;

    public String APIURL = "https://192.168.179.1:45455/api/Plan_ts/";

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
        bewaesserung = findViewById(R.id.sliderbewaesserungdetail);
        giessen = findViewById(R.id.giessen);
        topfgroesse = findViewById(R.id.topfgroesse);
        erde = findViewById(R.id.erde);
        licht = findViewById(R.id.licht);
        Plant_UPD = findViewById(R.id.Plant_UPD);
        Plant_DEL = findViewById(R.id.Plant_DEL);
        group_spinner = findViewById(R.id.group_spinner);


        String[] arraySpinner = getPflanzenarten();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pflanzenart.setAdapter(adapter);
        pflanzenart.setOnItemSelectedListener(PflanzeDetailView.this);

        String[] arraySpinnerGruppe = getGruppe();
        ArrayAdapter<String> adapterGruppe = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinnerGruppe);
        adapterGruppe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group_spinner.setAdapter(adapterGruppe);
        group_spinner.setOnItemSelectedListener(PflanzeDetailView.this);

        try {
            APIGET apiget = new APIGET(name, session, "GetUserPflanzen");
            Thread thread = new Thread(apiget);
            thread.start();
            thread.join();
            result = apiget.getResult();
            result = result.replace("}{", "};{");
            String[] tmp = result.split(";");

            Gson gsonPF = new Gson();
            for (String a : tmp) {
                Pflanze gsonObjPF = gsonPF.fromJson(a, Pflanze.class);
                userPflanzen.add(gsonObjPF);
            }
        } catch (Exception e) {
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
            for (String x : tmp2) {
                Pflanzenart gsonObjPA = gsonPA.fromJson(x, Pflanzenart.class);
                pflanzenartenListe.add(gsonObjPA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int h = 0; h < arraySpinnerGruppe.length; h++) {
            if (gruppenname.equals(arraySpinnerGruppe[h])) {
                group_spinner.setSelection(h);
                break;
            }
        }

        for (Pflanze pflanze : userPflanzen) {
            if (pflanze.getPflanzenID() == Integer.parseInt(plant)) {
                detailplant = pflanze;
                plantname.setText(pflanze.getPflanzenname());
                for (Pflanzenart pflanzenarts : pflanzenartenListe) {
                    if (pflanze.getPflanzeartname().equals(pflanzenarts.getBezeichnung())) {
                        String x = (pflanze.Bild);
                        Context context = Plant_Image.getContext();
                        int id = context.getResources().getIdentifier(x, "drawable", context.getPackageName());
                        Plant_Image.setImageResource(id);

                        for (int i = 0; i < arraySpinner.length; i++) {
                            if (pflanze.getPflanzeartname().equals(arraySpinner[i])) {
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
                sendPost(APIURL + "EditPflanze");
                finish();
            }
        });
        Plant_DEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPost(APIURL + "DeletePflanze");
                finish();
            }
        });

    }

    private String[] getGruppe() {
        String resultGP;

        try {
            APIGET apigetGP = new APIGET(name, session, "GetUserGruppen");
            Thread threadGP = new Thread(apigetGP);
            threadGP.start();
            threadGP.join();
            resultGP = apigetGP.getResult();
            resultGP = resultGP.replace("}{", "};{");
            String[] tmpGP = resultGP.split(";");

            Gson gsonGP = new Gson();
            for (String gpx : tmpGP) {
                Gruppe gsonObjGP = gsonGP.fromJson(gpx, Gruppe.class);
                tmpgruppe.add(gsonObjGP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] listGruppen = new String[tmpgruppe.size()];
        for (int i = 0; i < tmpgruppe.size(); i++) {
            listGruppen[i] = tmpgruppe.get(i).getGruppenname();
        }
        return listGruppen;
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.GERMANY);
        return sdf.format(date);
    }

    public static Date nower() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.GERMANY);
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.GERMANY).parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void sendPost(String URL) {
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
        } catch (Exception e) {
        }

        String pflanzenname = plantname.getText().toString();
        String bild = "plant1";
        String gegossen = now();
        String groesse = topfgroesse.getText().toString();
        String pflA = pflanzenart.getSelectedItem().toString();
        String gruppe = group_spinner.getSelectedItem().toString();
        try {
            String jsonLoginData = "\"user\":\"" + name + "\",\"sessionid\":" + session + "";
            String jsonBody = "{\"Pflanzen_ID\":\"" + plant + "\",\"Pflanzenname\":\"" + pflanzenname + "\",\"Bild\":\"" + bild + "\",\"Gegossen\":\"" + gegossen + "\",\"Groesse\":\"" + groesse + "\",\"Username\":\"" + name + "\",\"Pflanzeartname\":\"" + pflA + "\",\"Gruppenname\":\"" + gruppe + "\"}";
            String json = "{\"pflanze\":" + jsonBody + ",\"usd\":{" + jsonLoginData + "},\"Pflanzen_ID\":" + plant + "}";
            System.out.println(json);

            URL url = new URL(URL);
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

            if (scanner.hasNext()) {
                out = scanner.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getPflanzenarten() {
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
            for (String x : tmp2) {
                Pflanzenart gsonObjPA = gsonPA.fromJson(x, Pflanzenart.class);
                tmp.add(gsonObjPA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] pflanzenarten = new String[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            pflanzenarten[i] = tmp.get(i).getBezeichnung();
        }
        return pflanzenarten;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(0xffffffff);
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
            for (String x : tmp2) {
                Pflanzenart gsonObjPA = gsonPA.fromJson(x, Pflanzenart.class);
                tmp.add(gsonObjPA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Pflanzenart x : tmp) {
            if (pflanzA.equals(x.getBezeichnung())) {
                pfl = x;
            }
        }
        if (pfl != null) {
            luftfeuchtigkeit.setText(pfl.getLuftfeuchtigkeit().toString() + "%");
            giessen.setText(pfl.getWasserzyklus().toString() + " Tage");
            topfgroesse.setText(pfl.getTopfgroesse().toString() + "cm");
            erde.setText(pfl.getErde());
            licht.setText(pfl.getLichtbeduerfnisse());


            Calendar cal = Calendar.getInstance();

            Date date = nower();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.GERMANY).parse(detailplant.Gegossen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long wasserzyklus = 0;
            wasserzyklus = pfl.getWasserzyklus().longValue();
            float gießstatus = (((nower().getTime() - (date.getTime())) / 86400000));
            if (gießstatus > wasserzyklus) {
                gießstatus = 0;
            } else {
                gießstatus = 1 - (gießstatus / wasserzyklus);
                //gießstatus = gießstatus / 100;
            }
            bewaesserung.setValue(gießstatus);


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}