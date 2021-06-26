package com.example.plan_ts;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class APIGET implements Runnable {
    private String returnvalue;
    private String name;
    private String session;
    private String urlEnding;

    public APIGET(String name, String session, String urlEnding){
        this.name = name;
        this.session = session;
        this.urlEnding = urlEnding;
    }

    @Override
    public void run() {
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
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {}

        try {
            URL url = new URL("https://10.0.0.152:45455/api/Plan_ts/" + urlEnding);
            String jsonBody = "{\"user\":\"" + name + "\",\"sessionid\":\"" + session + "\"}";
            System.out.println(jsonBody);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpConnection.setRequestProperty("Accept", "text/plain");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            OutputStream os = httpConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonBody);
            writer.flush();
            writer.close();
            os.close();

            InputStream in = httpConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if(scanner.hasNext()){
                returnvalue = scanner.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResult(){
        return returnvalue;
    }
}
