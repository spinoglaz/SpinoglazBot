package ru.dasha.spinoglazBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SpinoglazBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        if(update.getMessage().getText().equals("/start")) {
            sendMessage.setText("Hello");
            try{
               execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
