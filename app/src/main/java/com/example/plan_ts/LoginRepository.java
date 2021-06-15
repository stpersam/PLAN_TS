package com.example.plan_ts;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
public class LoginRepository {
    private final String loginUrl = "https://localhost:44343/api/Plan_ts/";
    private final Executor executor;

    public LoginRepository( Executor executor) {
        this.executor = executor;
    }

    public void makeLoginRequest(
            final String jsonBody,
            final RepositoryCallback<String> callback
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<String> result = makeSynchronousLoginRequest(jsonBody);
                    callback.onComplete(result);
                } catch (Exception e) {
                    Result<String> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
    }

    public Result<String> makeSynchronousLoginRequest(String jsonBody) {
        try {
            URL url = new URL(loginUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setDoOutput(true);
            httpConnection.getOutputStream().write(jsonBody.getBytes("utf-8"));

            String loginResponse = httpConnection.getInputStream().toString();
            return new Result.Success<String>(loginResponse);
        } catch (Exception e) {
            return new Result.Error<String>(e);
        }
    }
}
