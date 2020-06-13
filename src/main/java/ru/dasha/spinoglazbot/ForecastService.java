package ru.dasha.spinoglazbot;

import ru.dasha.openweather.ForecastResponse;
import ru.dasha.openweather.LocationNotFoundException;
import ru.dasha.openweather.OpenWeatherAPI;
import ru.dasha.openweather.UVValue;

import java.io.IOException;

public class ForecastService {
    private OpenWeatherAPI openWeatherAPI = new OpenWeatherAPI("a1f6cbc7e1aca5bf22c000ada99af7c6");

    public Forecast getForecast(String city) throws IOException, LocationNotFoundException {
        ForecastResponse forecastResponse = openWeatherAPI.getForecast(city);
        int temperature = (int) forecastResponse.list.get(1).main.temp - 273;
        float lat = forecastResponse.city.coord.lat;
        float lon = forecastResponse.city.coord.lon;
        UVValue uvValue = openWeatherAPI.getUVIndex(lat, lon);
        int UVIndex = (int) uvValue.value;
        Forecast forecast = new Forecast();
        forecast.temperature = temperature;
        forecast.UVIndex = UVIndex;
        return forecast;
    }
}
