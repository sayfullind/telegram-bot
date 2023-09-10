package ru.macloud.telegrambottoddles.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j2
@Component
public class Bot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final MessageService service;

    @Autowired
    public Bot (BotConfig config, MessageService service) {
        this.config = config;
        this.service = service;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage())
            reply(service.preparingAnswer(update.getMessage()));
        if(update.hasChannelPost()) {

        }
    }

    private void reply(BotApiMethodMessage message) {
        try { execute(message); }
        catch (TelegramApiException e) { log.error(e.getLocalizedMessage()); }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
