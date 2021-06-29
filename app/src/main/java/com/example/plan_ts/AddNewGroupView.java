package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddNewGroupView extends AppCompatActivity {

    public static final String SESSIONID = "Gruppe_Session";
    public static final String USERNAME = "Gruppe_Name";
    public String session;
    public String name;
    private EditText nameET;
    private EditText beschreibungET;
    private Button selectPlants;
    private LinearLayout GroupScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group_view);

        session = getIntent().getStringExtra(SESSIONID);
        name = getIntent().getStringExtra(USERNAME);

    }
}