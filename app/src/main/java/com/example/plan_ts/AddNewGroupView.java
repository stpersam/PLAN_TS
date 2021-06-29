package com.example.plan_ts;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AddNewGroupView extends AppCompatActivity {

    public static final String SESSIONID = "Gruppe_Session";
    public static final String USERNAME = "Gruppe_Name";
    public String session;
    public String name;
    private EditText nameET;
    private EditText beschreibungET;
    private Button Group_back;
    private Button saveGroup;



    String out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_add_new_group_view);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);

        nameET = findViewById(R.id.nameET);
        beschreibungET = findViewById(R.id.beschreibungET);
        Group_back = findViewById(R.id.Group_back);
        saveGroup = findViewById(R.id.saveGroup);

        Group_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewGroup();
                finish();
            }
        });
    }

    private void saveNewGroup() {
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

        try {
            String gruppenname = nameET.getText().toString();
            String beschreibung = beschreibungET.getText().toString();
            String jsonLoginData = "\"user\":\"" + name + "\",\"sessionid\":" + session +"";
            String jsonBody = "{\"Gruppenname\":\"" + gruppenname + "\",\"Beschreibung\":\"" + beschreibung + "\",\"Username\":\"" + name +"\"}";
            String json = "{\"gruppe\":" + jsonBody + ",\"usd\":{" + jsonLoginData + "}}";
            System.out.println(json);

            URL url = new URL("https://192.168.179.1:45455/api/Plan_ts/AddGruppe");
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