package ru.dasha.spinoglazbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UVHandler {
    private final String API_CALL = "http://api.openweathermap.org/data/2.5/uvi?appid=";
    private final String KEY = "a1f6cbc7e1aca5bf22c000ada99af7c6";

    public String downloadJson(double lat, double lon) throws IOException {
        String urlString = API_CALL + KEY + "&lat=" + lat + "&lon=" + lon;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
