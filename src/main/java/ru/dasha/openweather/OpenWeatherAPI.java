package ru.dasha.openweather;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherAPI {
    private static final String API_CALL_FOR_FORECAST = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String API_CALL_FOR_UVINDEX = "http://api.openweathermap.org/data/2.5/uvi";
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String appid;

    public OpenWeatherAPI(String appid) {
        this.appid = appid;
    }

    public ForecastResponse getForecast(String city) throws IOException, LocationNotFoundException{
        String urlString = API_CALL_FOR_FORECAST + "?q=" + city + "&appid=" + appid;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();
        if(responseCode == 404) {
            throw new LocationNotFoundException();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();
        return objectMapper.readValue(response.toString(), ForecastResponse.class);
    }

    public UVValue getUVIndex(float lat, float lon) throws IOException {
        String urlString = API_CALL_FOR_UVINDEX + "?appid=" + appid + "&lat=" + lat + "&lon=" + lon;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();
        return objectMapper.readValue(response.toString(), UVValue.class);
    }
}
