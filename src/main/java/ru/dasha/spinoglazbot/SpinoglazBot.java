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
        try {
            String data = forecastHandler.downloadJson(city);
            ForecastList forecastList = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(data, ForecastList.class);
            temperature = (int) forecastList.list.get(0).main.temp - 273;
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendMessage.setText(temperature + "\u00B0C");
        try{
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        double lat = 56.4977100;
        double lan = 84.9743700;
        try {
            String data = uvHandler.downloadJson(lat, lan);
            UVValue uvValue = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(data, UVValue.class);
        } catch (IOException e) {
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
