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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNewGroupView extends AppCompatActivity {

    public static final String SESSIONID = "Gruppe_Session";
    public static final String USERNAME = "Gruppe_Name";
    public String session;
    public String name;
    private EditText nameET;
    private EditText beschreibungET;
    private Button selectPlants;
    private Button Group_back;

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
                onButtonShowPopupWindowClick(v);
            }
        });
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

        for (int i = 0; i < selectedPlants.size(); i++) {
            //LinearLayout
            LinearLayout linearLayout =new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(50, 20, 50, 20);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setId(i);

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
                        }
                    }

                    TableRow.LayoutParams par = new TableRow.LayoutParams(350, 350, 0);
                    x.setLayoutParams(par);
                    int ids = selectedPlants.get(l);
                    String idsPl = selectedPlants.get(l).toString();
                    x.setId(ids);
                    a.addView(x);
                    l++;
                }
                mTableLayout.addView(a);
            }
            linearLayout.addView(mTableLayout);
            scrollView.addView(linearLayout);
        }
    }
}