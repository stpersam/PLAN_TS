package com.example.plan_ts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class testget extends AppCompatActivity {

    private Button btnLoadContent;
    private TextView tvResult;
    String out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testget);

        btnLoadContent = findViewById(R.id.btn_load_content);
        tvResult = findViewById(R.id.tv_result);

        btnLoadContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebResult();
            }
        });
    }

    private void loadWebResult() {
        // Create a trust manager that does not validate certificate chains
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

        WebRunnable webRunnable = new WebRunnable("https://10.0.2.2:5001/api/Plan_ts/GetTestPflanzen");
        new Thread(webRunnable).start();
    }

    class WebRunnable implements Runnable{
        URL url;
        WebRunnable(String url){
            try {
                this.url = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream in = urlConnection.getInputStream();
                Log.d("asdf",in.toString());
                Scanner scanner = new Scanner(in);
                Log.d("asdf",scanner.toString());
                scanner.useDelimiter("\\A");
                Handler mainHandler = new Handler(Looper.getMainLooper());

                if(scanner.hasNext()){

                    out = scanner.next();
                    Log.d("asdf",out);
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(out);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}