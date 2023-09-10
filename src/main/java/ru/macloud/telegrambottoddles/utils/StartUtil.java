package ru.macloud.telegrambottoddles.utils;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Log4j2
public class StartUtil implements Util {
    @Override
    public BotApiMethodMessage createAnswer(Message lastMessage, Message message) {
        if (lastMessage == null) return defaultAnswer(message.getChatId(), message.getChat().getFirstName());
        else return invalidAnswer(message.getChatId());
    }

    private SendMessage defaultAnswer(Long chatId, String name) {
        log.info("The user launched the bot");
        return SendMessage.builder().text(String.format("Hello, %s!", name)).chatId(chatId).build();
    }

    private SendMessage invalidAnswer(Long chatId) {
        return SendMessage.builder().text("You have already launched the bot").chatId(chatId).build();
    }
}
