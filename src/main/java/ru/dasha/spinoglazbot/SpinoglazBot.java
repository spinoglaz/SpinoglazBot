package ru.dasha.spinoglazbot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class SpinoglazBot extends TelegramLongPollingBot {
    private ForecastHandler forecastHandler = new ForecastHandler();
    private UVHandler uvHandler = new UVHandler();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        if(update.getMessage().getText().equals("/start")) {
            sendMessage.setText("Hello! Please enter the city name.");
            try{
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        String city = update.getMessage().getText();
        int temperature = 0;
        float lat = 0;
        float lon = 0;
        int UVIndex = 0;
        try {
            String data = forecastHandler.downloadJson(city);
            ForecastResponse forecastResponse = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(data, ForecastResponse.class);
            System.out.println(data);
            temperature = (int) forecastResponse.list.get(1).main.temp - 273;
            lat = forecastResponse.city.coord.lat;
            lon = forecastResponse.city.coord.lon;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            execute(sendMessage.setText(temperature + "\u00B0C"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            String UVdata = uvHandler.downloadJson(lat, lon);
            UVValue uvValue = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(UVdata, UVValue.class);
            UVIndex = (int) uvValue.value;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try{
            execute(sendMessage.setText("UV Index:" + " " + UVIndex));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "SpinoglazBot";
    }

    @Override
    public String getBotToken() {
        return "1258557799:AAGzaXmXv0P4tPbk2B1EXKmyHsh5PamwNms";
    }

}
