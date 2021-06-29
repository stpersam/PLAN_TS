package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    private Button selectPlants;
    private Button Group_back;
    private Button saveGroup;

    private List<Gruppe> gruppen = new ArrayList<>();
    private List<Pflanze> userPflanzen = new ArrayList<>();
    private List<Integer> selectedPlants = new ArrayList<>();

    String out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_add_new_group_view);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);

        selectPlants = findViewById(R.id.selectPlants);
        nameET = findViewById(R.id.nameET);
        beschreibungET = findViewById(R.id.beschreibungET);
        Group_back = findViewById(R.id.Group_back);
        saveGroup = findViewById(R.id.saveGroup);

        GetUserplants();

        Group_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        selectPlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //onButtonShowPopupWindowClick(v);
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

            URL url = new URL("https://10.0.0.152:45455/api/Plan_ts/AddGruppe");
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

    public void GetUserplants(){
          try{
            APIGET apiget = new APIGET(name, session, "GetUserPflanzen");
            Thread thread = new Thread(apiget);
            thread.start();
            thread.join();
            out = apiget.getResult();
              out = out.replace("}{", "};{");
              String[] tmp2 = out.split(";");

            try {
                //Pflanze
                Gson gsonPF = new Gson();
                for (String a : tmp2){
                    Pflanze gsonObjPF = gsonPF.fromJson(a, Pflanze.class);
                    userPflanzen.add(gsonObjPF);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.selectplants_group_popup, null);
        Button selectBtn = popupView.findViewById(R.id.SelectBtn);

        // create the popup window
        int width = ScrollView.LayoutParams.WRAP_CONTENT;
        int height = ScrollView.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TableLayout tab_lay = (TableLayout) popupView.findViewById(R.id.tab_lay);

        for(int i = 0; i < userPflanzen.size();){
            //System.out.println(userPflanzen.get(i));
            TableRow a = new TableRow(this);
            TableRow.LayoutParams param = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    300, 1.0f);
            param.setMargins(10, 10, 10, 10);
            a.setLayoutParams(param);
            a.setGravity(Gravity.CENTER_VERTICAL);

            for(int y = 0; (y < 2) && (i < userPflanzen.size()); y++) {
                Button x = new Button(this);
                x.setText(userPflanzen.get(i).Pflanzenname);
                x.setGravity(Gravity.CENTER);
                TableRow.LayoutParams par = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,300,y);
                x.setLayoutParams(par);


                int ids = userPflanzen.get(i).PflanzenID;
                x.setId(ids);
                x.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        x.setBackgroundColor(Color.rgb(165,216,254));
                        selectedPlants.add(ids);
                    }
                });
                a.addView(x);
                i++;
            }
            tab_lay.addView(a);
        }

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedPlants();
                popupWindow.dismiss();
                return;
            }
        });
    }

    public void SelectedPlants(){
        LinearLayout scrollView = findViewById(R.id.layoutView);
            //LinearLayout
            LinearLayout linearLayout =new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(50, 20, 50, 20);
            linearLayout.setLayoutParams(layoutParams);

            //TableLayout
            TableLayout mTableLayout = new TableLayout(this);
            mTableLayout.setLayoutParams(new TableLayout.LayoutParams(350, TableLayout.LayoutParams.WRAP_CONTENT));
            mTableLayout.setGravity(Gravity.CENTER_HORIZONTAL);


            for (int l = 0; l < selectedPlants.size(); ) {
                TableRow a = new TableRow(this);
                TableRow.LayoutParams param = new TableRow.LayoutParams(350, 350, 0);
                a.setLayoutParams(param);
                a.setGravity(Gravity.CENTER_HORIZONTAL);

                for (int y = 0; (y < 2) && (l < selectedPlants.size()); y++) {
                    Button x = new Button(this);
                    for(int s = 0; s < userPflanzen.size(); s++){
                        if(selectedPlants.get(l) == userPflanzen.get(s).getPflanzenID()){
                            x.setText(userPflanzen.get(s).getPflanzenname());
                            TableRow.LayoutParams par = new TableRow.LayoutParams(350, 350, 0);
                            x.setLayoutParams(par);
                            int ids = userPflanzen.get(s).getPflanzenID();
                            String idsPl = userPflanzen.get(s).getPflanzenID().toString();
                            x.setId(ids);
                        }
                    }
                    a.addView(x);
                    l++;
                }
                mTableLayout.addView(a);
            }
            linearLayout.addView(mTableLayout);
            scrollView.addView(linearLayout);

    }
}