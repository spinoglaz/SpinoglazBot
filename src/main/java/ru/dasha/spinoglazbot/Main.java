package ru.dasha.spinoglazbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        SpinoglazBot spinoglazBot = new SpinoglazBot();
        try {
             botsApi.registerBot(spinoglazBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
