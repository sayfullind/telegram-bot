package ru.macloud.telegrambottoddles.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.macloud.telegrambottoddles.utils.LinksUtil;
import ru.macloud.telegrambottoddles.utils.StartUtil;
import ru.macloud.telegrambottoddles.utils.Util;

import java.util.Optional;

@Log4j2
@Component
public class MessageService {
    private Message lastMessage;
    private static final String START = "/start";
    private static final String LINKS = "/links";

    public BotApiMethodMessage preparingAnswer(Message message) {
        var text = message.getText();
        var chatId = message.getChatId();

        if(message.isCommand()) {
            var result = Optional.ofNullable(selectUtil(text));
            if (result.isEmpty()) return unknown(chatId);
            var answer =  result.get().createAnswer(lastMessage, message);
            lastMessage = message;
            return answer;
        }

        lastMessage = message;
        return unknown(message.getChatId());
    }

    private Util selectUtil(String command) {
        return switch (command) {
            case START -> new StartUtil();
            case LINKS -> new LinksUtil();
            default -> null;
        };
    }

    public SendMessage unknown(Long chatId) {
        return SendMessage.builder().chatId(chatId).text("Enter the command to process the message").build();
    }
}
