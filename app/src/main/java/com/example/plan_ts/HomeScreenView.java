package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HomeScreenView extends AppCompatActivity {
    public static final String SESSIONID = "H_session";
    public static final String USERNAME = "H_user";
    public String session;
    public String name;
    private Button plantadd;
    private Button plant1;
    private Button gruppenbtn;
    String out;

    private List<Gruppe> gruppen;
    private List<Pflanzenart> pflanzenarten;
    private List<Pflanze> userPflanzen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_home_screen);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);
        plant1 = findViewById(R.id.home_plant1);
        plantadd = findViewById(R.id.home_plantadd);
        gruppenbtn = findViewById(R.id.gruppen_btn);

        //Initialize Data for HomeScreen of User
        loadWebResult();

        gruppenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);
            }
        });
        plant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenView.this, PflanzeDetailView.class);
                i.putExtra(PflanzeDetailView.PLANT_KEY,("Plant1"));
                i.putExtra(PflanzeDetailView.SESSIONID,SESSIONID);
                i.putExtra(PflanzeDetailView.USERNAME,USERNAME);
                startActivity(i);
            }
        });
        plantadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenView.this, NewPflanzeView.class);
                i.putExtra(NewPflanzeView.SESSIONID,SESSIONID);
                i.putExtra(NewPflanzeView.USERNAME,USERNAME);
                startActivity(i);
            }
        });
    }

    public void onButtonShowPopupWindowClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.home_group_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void loadWebResult() {
        try {
            APIGET apiget = new APIGET(name, session, "Initialize");
            Thread thread = new Thread(apiget);
            thread.start();
            thread.join();
            out = apiget.getResult();

            System.out.println(out);
            String[] tmp = out.split("\\|");
            for (String a: tmp) {
                String[] tmp2 = a.split("\\}");
                for (String x: tmp2) {
                    x = x.replace("{","");
                    String[] tmp3 = x.split(",");
                    System.out.println("----------------------------------------------------------------------------");
                    for (String c: tmp3) {
                        System.out.println(c);
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}