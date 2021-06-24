package com.example.plan_ts;

import android.os.StrictMode;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;

interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
public class LoginRepository {
    private final String loginUrl = "https://10.0.2.2:5001/api/Plan_ts/";
    private final Executor executor;

    public LoginRepository( Executor executor) {
        this.executor = executor;
    }

    public void makeLoginRequest(
            final String jsonBody,
            final RepositoryCallback<Double> callback
    ) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<Double> result = makeSynchronousLoginRequest(jsonBody);
                    callback.onComplete(result);
                } catch (Exception e) {
                    Result<Double> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
    }

    public Result<Double> makeSynchronousLoginRequest(String jsonBody) {
        try {
            URL url = new URL(loginUrl + "Login");
            Log.d("connect",url.toString());
            HttpsURLConnection httpConnection = (HttpsURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpConnection.setRequestProperty("Accept", "text/plain");
            httpConnection.setDoOutput(true);
            httpConnection.getOutputStream().write(jsonBody.getBytes("utf-8"));

            Double loginResponse = Double.parseDouble(httpConnection.getInputStream().toString());
            Log.d("connectendet",loginResponse.toString());
            return new Result.Success<Double>(loginResponse);
        } catch (Exception e) {
            //Log.d("connect",);
            return new Result.Error<Double>(e);
        }
    }
}
