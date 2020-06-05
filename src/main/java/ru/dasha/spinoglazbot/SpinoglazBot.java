package ru.dasha.spinoglazbot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpinoglazBot extends TelegramLongPollingBot {
    private final static String BOT_USERNAME = "SpinoglazBot";
    private final static String BOT_TOKEN = "1258557799:AAGzaXmXv0P4tPbk2B1EXKmyHsh5PamwNms";
    private ForecastHandler forecastHandler = new ForecastHandler();
    private UVHandler uvHandler = new UVHandler();
    private ObjectMapper objectMapper = new ObjectMapper();
    private SendMessage sendMessage = new SendMessage();
    private InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private InlineKeyboardButton tomsk = new InlineKeyboardButton();

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        if(update.getMessage().getText().equals("/start")) {
            sendMessage("Hello! Please enter the city name.", chatId);
            return;
        }

        String city = update.getMessage().getText();
        System.out.println(update.getMessage().getText());
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
            sendMessage("Failed to execute query", chatId);
        } catch (LocationNotFoundException e) {
            e.printStackTrace();
            sendMessage("Location not found.", chatId);
            return;
        }
        sendMessage(temperature + "\u00B0C", chatId);
        try {
            String UVdata = uvHandler.downloadJson(lat, lon);
            UVValue uvValue = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(UVdata, UVValue.class);
            UVIndex = (int) uvValue.value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendMessage("UV Index:" + " " + UVIndex, chatId);

    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private void sendMessage(String message, Long chatId){
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(sendInlineKeyBoard());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup sendInlineKeyBoard() {
        tomsk.setText("Tomsk");
        tomsk.setCallbackData("Tomsk");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(tomsk);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
