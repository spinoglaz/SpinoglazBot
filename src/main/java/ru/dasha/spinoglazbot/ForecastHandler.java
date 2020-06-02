package ru.dasha.spinoglazbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastHandler {
    private final String API_CALL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private final String KEY = "&appid=a1f6cbc7e1aca5bf22c000ada99af7c6";

    public String downloadJson(String city) throws IOException {
        String urlString = API_CALL + city + KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();
        if(responseCode == 404) {
            throw new IllegalArgumentException();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();
        return response.toString();
    }
}
