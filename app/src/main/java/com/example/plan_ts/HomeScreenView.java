package com.example.plan_ts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeScreenView extends AppCompatActivity {
    public static final String SESSIONID = "H_session";
    public static final String USERNAME = "H_user";
    public String session;
    public String name;
    private Button gruppenbtn;

    private MaterialToolbar toolbar;
    private TextView bewaesserung;
    public Slider slider;
    String out;

    private List<Integer> selectedGoups = new ArrayList<>();
    private List<Gruppe> gruppen = new ArrayList<>();
    private List<Pflanzenart> pflanzenarten = new ArrayList<>();
    private List<Pflanze> userPflanzen = new ArrayList<>();
    private List<Pflanze> selectedPflanzen = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_home_screen);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);
        gruppenbtn = findViewById(R.id.gruppen_btn);
        toolbar = findViewById(R.id.topAppBar);
        bewaesserung = findViewById(R.id.bewaesserung);
        slider = findViewById(R.id.Slider);


        //Initialize Data for HomeScreen of User
        Initialize();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowMenuClick(v);
            }
        });

        gruppenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Initialize();
    }

    public void onButtonShowMenuClick(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.menu_popup, null);

        ImageButton addplantbtn = popupView.findViewById(R.id.AddPlant);
        ImageButton AddGroup = popupView.findViewById(R.id.AddGroup);
        Button logoutbtn = popupView.findViewById(R.id.logoutbtn);


        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addplantbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenView.this, NewPflanzeView.class);
                i.putExtra(NewPflanzeView.SESSIONID, session);
                i.putExtra(NewPflanzeView.USERNAME, name);
                i.putExtra(NewPflanzeView.GRUPPENNAME, "");
                startActivity(i);
            }
        });

        AddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenView.this, AddNewGroupView.class);
                i.putExtra(AddNewGroupView.SESSIONID, session);
                i.putExtra(AddNewGroupView.USERNAME, name);
                startActivity(i);
            }
        });
    }

    public void onButtonShowPopupWindowClick(View view) {

        selectedGoups = new ArrayList<>();

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
        for (int i = 0; i < gruppen.size(); ) {
            TableRow a = new TableRow(this);
            TableRow.LayoutParams param = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    300, 1.0f);
            param.setMargins(10, 10, 10, 10);
            a.setLayoutParams(param);
            a.setGravity(Gravity.CENTER_VERTICAL);

            for (int y = 0; (y < 2) && (i < gruppen.size()); y++) {
                Button x = new Button(this);
                x.setText(gruppen.get(i).Gruppenname);
                x.setGravity(Gravity.CENTER);
                TableRow.LayoutParams par = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 300, y);
                x.setLayoutParams(par);


                int ids = gruppen.get(i).GruppenID;
                x.setId(ids);
                x.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        x.setBackgroundColor(Color.rgb(165, 216, 254));
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
                SelectedGroups();
                popupWindow.dismiss();
                return;
            }
        });
    }

    public static Date now() {
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

    private void SelectedGroups() {

        LinearLayout scrollView = findViewById(R.id.homeScrollView);
        scrollView.removeAllViews();
        selectedPflanzen = new ArrayList<>();

        for (int i = 0; i < selectedGoups.size(); i++) {
            //LinearLayout
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(50, 20, 50, 20);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setId(i);

            //TextView
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.setMargins(200, 30, 30, 30);
            params1.gravity = Gravity.CENTER;
            textView.setLayoutParams(params1);
            textView.setTextSize(18);
            String gruppenname = "";
            for (Gruppe g : gruppen) {
                if (g.getGruppenID() == selectedGoups.get(i)) {
                    gruppenname = g.getGruppenname();
                    break;
                } else {
                    gruppenname = "No Group found";
                }
            }
            final String nameDerGruppe = gruppenname;
            textView.setText(gruppenname);
            linearLayout.addView(textView);

            List<Pflanze> tmpPfl = new ArrayList();
            for (int z = 0; z < userPflanzen.size(); z++) {
                if (userPflanzen.get(z).Gruppenname.equals(gruppenname)) {
                    selectedPflanzen.add(userPflanzen.get(z));
                    tmpPfl.add(userPflanzen.get(z));
                }
            }

            double bewaesserungcount = 0;
            float bewaesserungpercentfloat = 0;
            String bewaesserungpercent = "N/A";

            if (selectedPflanzen.size() > 0) {
                Calendar cal = Calendar.getInstance();
                for (Pflanze p : selectedPflanzen) {
                    Date date = now();
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.GERMANY).parse(p.Gegossen);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long wasserzyklus = 0;
                    for (Pflanzenart pa:pflanzenarten
                         ) {
                        if(pa.Bezeichnung.equals(p.Pflanzeartname)){
                            wasserzyklus = pa.Wasserzyklus.longValue();
                        }
                    }
                    if (((now().getTime() - (date.getTime()))/86400000) < wasserzyklus) {
                        bewaesserungcount++;
                    }
                }
                bewaesserungpercentfloat = (float)bewaesserungcount / (float)selectedPflanzen.size();
                bewaesserungpercent = String.format("%.0f",bewaesserungcount / selectedPflanzen.size() * 100);
            }

            slider.setValue(bewaesserungpercentfloat);
            bewaesserung.setText(bewaesserungpercent + "% Pflanzen sind bew??ssert");

            //TableLayout
            TableLayout mTableLayout = new TableLayout(this);
            mTableLayout.setLayoutParams(new TableLayout.LayoutParams(350, TableLayout.LayoutParams.WRAP_CONTENT));
            mTableLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            for (int l = 0; l <= tmpPfl.size(); ) {
                TableRow a = new TableRow(this);
                TableRow.LayoutParams param = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 0.1f);
                a.setLayoutParams(param);
                a.setGravity(Gravity.CENTER_HORIZONTAL);

                for (int y = 0; (y < 2) && (l <= tmpPfl.size()); y++) {
                    if (l < (tmpPfl.size())) {
                        Button x = new Button(this);
                        x.setText(tmpPfl.get(l).Pflanzenname);
                        TableRow.LayoutParams par = new TableRow.LayoutParams(350, 350, 0);
                        x.setLayoutParams(par);
                        int ids = tmpPfl.get(l).getPflanzenID();
                        String idsPl = tmpPfl.get(l).getPflanzenID().toString();
                        x.setId(ids);
                        x.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(HomeScreenView.this, PflanzeDetailView.class);
                                i.putExtra(PflanzeDetailView.SESSIONID, session);
                                i.putExtra(PflanzeDetailView.USERNAME, name);
                                i.putExtra(PflanzeDetailView.PLANT_KEY, idsPl);
                                i.putExtra(PflanzeDetailView.GRUPPENNAME, nameDerGruppe);
                                startActivity(i);
                            }
                        });
                        a.addView(x);
                    } else if (l == (tmpPfl.size())) {
                        Button end = new Button(this);
                        end.setBackgroundResource(R.drawable.add2);
                        end.setGravity(Gravity.CENTER);
                        TableRow.LayoutParams endpar = new TableRow.LayoutParams(350, 350, 0);
                        end.setLayoutParams(endpar);
                        end.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(HomeScreenView.this, NewPflanzeView.class);
                                i.putExtra(NewPflanzeView.SESSIONID, session);
                                i.putExtra(NewPflanzeView.USERNAME, name);
                                i.putExtra(NewPflanzeView.GRUPPENNAME, nameDerGruppe);
                                startActivity(i);
                            }
                        });
                        a.addView(end);
                    }
                    l++;
                }
                mTableLayout.addView(a);
            }
            linearLayout.addView(mTableLayout);
            scrollView.addView(linearLayout);
        }
    }

    private void Initialize() {
        try {
            pflanzenarten = new ArrayList<>();
            gruppen = new ArrayList<>();
            userPflanzen = new ArrayList<>();


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
                for (String x : pflanzenA) {
                    Pflanzenart gsonObjPA = gsonPA.fromJson(x, Pflanzenart.class);
                    pflanzenarten.add(gsonObjPA);
                }
                //Gruppen
                for (String g : grp) {
                    Gson gsonG = new Gson();
                    Gruppe gsonObjG = gsonG.fromJson(g, Gruppe.class);
                    gruppen.add(gsonObjG);
                }
                //Pflanze
                Gson gsonPF = new Gson();
                for (String a : pfl) {
                    Pflanze gsonObjPF = gsonPF.fromJson(a, Pflanze.class);
                    userPflanzen.add(gsonObjPF);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            SelectedGroups();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}