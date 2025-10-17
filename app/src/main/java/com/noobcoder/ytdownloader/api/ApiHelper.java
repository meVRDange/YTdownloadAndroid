package com.noobcoder.ytdownloader.api;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    private static final String BASE_URL = "http://192.168.29.87:5062";

    // ✅ Generic HTTP call
    public static String makeRequest(Context context, String endpoint, String method, String jsonBody) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            // Add headers
            conn.setRequestProperty("Content-Type", "application/json");

            // 🔹 Attach JWT if available
            String token = context
                    .getSharedPreferences("YTAppPrefs", Context.MODE_PRIVATE)
                    .getString("jwt_token", null);

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            // 🔹 If there’s a JSON body (e.g. POST/PUT)
            if (jsonBody != null && !jsonBody.isEmpty()) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes());
                    os.flush();
                }
            }

            // 🔹 Read response
            int responseCode = conn.getResponseCode();
            BufferedReader reader;

            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "API Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
