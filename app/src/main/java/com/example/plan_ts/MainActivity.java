package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView username;
    private TextView error;
    private TextView password;
    private String url = "https://localhost:44312/api/Plan_ts/Login";
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.login_button);
        username = findViewById(R.id.l_username);
        password = findViewById(R.id.l_password);
        error = findViewById(R.id.login_error);
        JSONObject jObjectData = new JSONObject();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = username.getText().toString() + password.getText().toString();
                CallAPI callAPI = new CallAPI(url,content);
                String result = callAPI.doInBackground();

                if(result != "0" && result != null){
                    Intent i  = new Intent(MainActivity.this,HomeScreen.class);
                    i.putExtra(HomeScreen.HOME_KEY,result);
                    startActivity(i);
                }else{
                    error.setText("Username or Password are incorrect");
                    error.setVisibility(View.VISIBLE);
                    error.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            error.setVisibility(View.GONE);
                        }
                    }, 5 * 1000);
                }
            }
        });
    }
}