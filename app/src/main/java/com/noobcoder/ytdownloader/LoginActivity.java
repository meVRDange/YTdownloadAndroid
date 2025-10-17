package com.noobcoder.ytdownloader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // JSON body
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            // Create request body
            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            // Build request manually
            Request request = new Request.Builder()
                    .url("http://192.168.29.87:5062/login")  // FULL control here ✅
                    .post(body) // Can change to .get(), .put(), etc.
                    .build();

            // Make the request on background thread
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();

                        try {
                            JSONObject resp = new JSONObject(responseBody);
                            String token = resp.optString("token", "");

                            if (!token.isEmpty()) {
                                getSharedPreferences("YTAppPrefs", MODE_PRIVATE)
                                        .edit()
                                        .putString("jwt_token", token)
                                        .apply();

                                runOnUiThread(() -> {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                });
                            } else {
                                runOnUiThread(() ->
                                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show()
                                );
                            }
                        } catch (Exception ex) {
                            runOnUiThread(() ->
                                    Toast.makeText(LoginActivity.this, "Parse error: " + ex.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                        }
                    } else {
                        runOnUiThread(() ->
                                Toast.makeText(LoginActivity.this, "Invalid credentials or server error", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
