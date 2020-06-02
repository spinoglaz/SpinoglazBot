package ru.dasha.spinoglazbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        System.getProperties().put( "proxySet", "true" );
        System.getProperties().put( "socksProxyHost", "127.0.0.1" );
        System.getProperties().put( "socksProxyPort", "9150" );
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
