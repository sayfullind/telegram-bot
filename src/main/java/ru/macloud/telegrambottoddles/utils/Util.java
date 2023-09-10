package ru.macloud.telegrambottoddles.utils;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Util {
    BotApiMethodMessage createAnswer(Message lastMessage, Message message);
}
