package com.example.plan_ts;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;

interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
public class LoginRepository {
    private final String loginUrl = "https://localhost:5001/api/Plan_ts/";
    private final Executor executor;

    public LoginRepository( Executor executor) {
        this.executor = executor;
    }

    public void makeLoginRequest(
            final String jsonBody,
            final RepositoryCallback<Double> callback
    ) {
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
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpConnection.setRequestProperty("Accept", "text/plain");
            httpConnection.setDoOutput(true);
            httpConnection.getOutputStream().write(jsonBody.getBytes("utf-8"));

            Double loginResponse = Double.parseDouble(httpConnection.getInputStream().toString());

            return new Result.Success<Double>(loginResponse);
        } catch (Exception e) {
            return new Result.Error<Double>(e);
        }
    }
}
