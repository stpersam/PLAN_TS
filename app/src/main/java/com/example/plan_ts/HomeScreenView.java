package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.DynamicLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenView extends AppCompatActivity {
    public static final String SESSIONID = "H_session";
    public static final String USERNAME = "H_user";
    public String session;
    public String name;
    private Button plantadd;
    private Button plant1;
    private Button gruppenbtn;
    String out;

    private List<Integer> selectedGoups = new ArrayList<>();
    private List<Gruppe> gruppen = new ArrayList<>();
    private List<Pflanzenart> pflanzenarten = new ArrayList<>();
    private List<Pflanze> userPflanzen = new ArrayList<>();

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
        Initialize();

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
        Button selectBtn = popupView.findViewById(R.id.SelectBtn);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TableLayout tab_lay = (TableLayout) popupView.findViewById(R.id.tab_lay);
        for(int i = 0; i < gruppen.size(); ){
            TableRow a = new TableRow(this);
            TableRow.LayoutParams param = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            param.setMargins(10, 10, 10, 10);
            a.setLayoutParams(param);
            a.setGravity(Gravity.CENTER_VERTICAL);

            for(int y = 0; (y < 2) && (i < gruppen.size()); y++) {
                Button x = new Button(this);
                x.setText(gruppen.get(i).Gruppenname);
                x.setGravity(Gravity.CENTER);
                TableRow.LayoutParams par = new TableRow.LayoutParams(y);
                x.setLayoutParams(par);
                x.setPadding(40,40,40,40);
                int ids = gruppen.get(i).GruppenID;
                x.setId(ids);
                x.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View view) {
                         x.setBackgroundColor(Color.rgb(165,216,254));
                         selectedGoups.add(ids);
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
                popupWindow.dismiss();
                return;
            }
        });
    }

    private void Initialize() {
        try {
            APIGET apiget = new APIGET(name, session, "Initialize");
            Thread thread = new Thread(apiget);
            thread.start();
            thread.join();
            out = apiget.getResult();

            String[] tmp = out.split("\\|");
            tmp[0] = tmp[0].replace("}{", "};{");
            tmp[1] = tmp[1].replace("}{", "};{");
            tmp[2] = tmp[2].replace("}{", "};{");
            String[] pflanzenA = tmp[0].split(";");
            String[] pfl = tmp[2].split(";");
            String[] grp = tmp[1].split(";");

            try {
                //Pflanzenarten
                Gson gsonPA = new Gson();
                for (String x : pflanzenA){
                    System.out.println(x);
                    Pflanzenart gsonObjPA = gsonPA.fromJson(x, Pflanzenart.class);
                    pflanzenarten.add(gsonObjPA);
                }
                //Gruppen
                for (String g : grp) {
                    Gson gsonG = new Gson();
                    Gruppe gsonObjG = gsonG.fromJson(g, Gruppe.class);
                    gruppen.add(gsonObjG);
                    System.out.println(g);
                }
                //Pflanze
                Gson gsonPF = new Gson();
                for (String a : pfl){
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
}