package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView username;
    private TextView password;
    private String url = "https://localhost:44312/api/Plan_ts";
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.login_button);
        username = findViewById(R.id.l_username);
        password = findViewById(R.id.l_password);
        JSONObject jObjectData = new JSONObject();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = username.getText().toString() + password.getText().toString();
                CallAPI callAPI = new CallAPI(url,content);
                callAPI.doInBackground();
            }
        });
    }
}