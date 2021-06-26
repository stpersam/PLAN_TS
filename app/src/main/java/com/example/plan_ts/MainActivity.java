package com.example.plan_ts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView username;
    private TextView error;
    private TextView password;
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final LoginRepository loginRepository = new LoginRepository(executorService);


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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeLoginRequest(username.getText().toString(),password.getText().toString());
            }
        });
    }
    public void makeLoginRequest(String username, String password) {
        String jsonBody = "{\"user\":\"" + username + "\",\"password\":\"" + password + "\"}";
        loginRepository.makeLoginRequest(jsonBody, new RepositoryCallback<Integer>() {
            @Override
            public void onComplete(Result<Integer> result) {
                if (result instanceof Result.Success && result != null && ((Result.Success<Integer>) result).data != 0) {
                    Intent i  = new Intent(MainActivity.this, HomeScreenView.class);
                    i.putExtra(HomeScreenView.SESSIONID,((Result.Success<Integer>) result).data.toString());
                    i.putExtra(HomeScreenView.USERNAME,username);
                    startActivity(i);
                } else {
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