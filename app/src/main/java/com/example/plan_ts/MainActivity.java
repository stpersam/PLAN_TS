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
    private TextView text;
    private String url = "https://localhost:44312/api/Plan_ts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        text = findViewById(R.id.textView);
        JSONObject jObjectData = new JSONObject();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Create Json Object using text
                    jObjectData.put("text",text.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }

                CallAPI callAPI = new CallAPI(url,jObjectData);
                callAPI.doInBackground();
            }
        });
    }
}