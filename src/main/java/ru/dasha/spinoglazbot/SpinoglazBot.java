package ru.dasha.spinoglazbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dasha.openweather.LocationNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpinoglazBot extends TelegramLongPollingBot {
    private final static String BOT_USERNAME = "SpinoglazBot";
    private SendMessage sendMessage = new SendMessage();
    private InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
    private ForecastService forecastService = new ForecastService();

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            if(update.getMessage().getText().equals("/start")) {
                sendMessage("Hello! Please enter the city name or select from the list.", chatId, createInlineKeyBoard());
                return;
            }
            String city = update.getMessage().getText();
            System.out.println(update.getMessage().getText());
            try {
                Forecast forecast = forecastService.getForecast(city);
                sendMessage(forecast.temperature + "\u00B0C" + " " + "UV Index:" + forecast.UVIndex, chatId, null);
            } catch (IOException e) {
                e.printStackTrace();
                sendMessage("Failed to execute query", chatId, null);
            } catch (LocationNotFoundException e) {
                e.printStackTrace();
                sendMessage("Location not found.", chatId, null);
                return;
            }

        } else if(update.hasCallbackQuery()) {
            String queryId = update.getCallbackQuery().getId();
            String city = update.getCallbackQuery().getData();
            try {
                Forecast forecast = forecastService.getForecast(city);
                answerCallbackQuery( forecast.temperature+ "\u00B0C" + " " + "UV Index:" + forecast.UVIndex, queryId);
            } catch (IOException e) {
                e.printStackTrace();
                answerCallbackQuery("Failed to execute query", queryId);
            } catch (LocationNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    private void sendMessage(String message, Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup){
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup createInlineKeyBoard() {
        InlineKeyboardButton tomsk = new InlineKeyboardButton();
        InlineKeyboardButton novosibirsk = new InlineKeyboardButton();
        tomsk.setText("Tomsk");
        tomsk.setCallbackData("Tomsk");
        novosibirsk.setText("Novosibirsk");
        novosibirsk.setCallbackData("Novosibirsk");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(tomsk);
        keyboardButtonsRow1.add(novosibirsk);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private void answerCallbackQuery(String message, String id) {
        answerCallbackQuery.setCallbackQueryId(id);
        answerCallbackQuery.setText(message);
        answerCallbackQuery.setShowAlert(true);
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
