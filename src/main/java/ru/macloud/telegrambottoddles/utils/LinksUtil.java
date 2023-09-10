package ru.macloud.telegrambottoddles.utils;

import lombok.extern.log4j.Log4j2;

import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public final class LinksUtil implements Util {
    @Override
    public BotApiMethodMessage createAnswer(Message lastMessage, Message message) {
        log.info(String.format("LinksUtil gets a messages: %nLast: %s%nCurrent: %s",
                lastMessage.getText(), message.getText()));
        if (lastMessage.isCommand()) return requestText(message.getChatId());
        if (lastMessage.getEntities() == null) return invalidMessage(message.getChatId());
        else return correctMessage(lastMessage);
    }

    private SendMessage requestText(Long chatId) {
        return SendMessage.builder().text("To start, send a message").chatId(chatId).build();
    }

    private SendMessage invalidMessage(Long chatId) {
        return SendMessage.builder()
                .text("Incorrect message format - the text must contain at least one link")
                .chatId(chatId).build();
    }

    private SendMessage correctMessage(Message lastMessage) {
        var entities = lastMessage.getEntities();
        var text = new StringBuilder(lastMessage.getText()).append("\n\nИнтересные материалы от: ");
        var chatId = lastMessage.getChatId();
        return preparing(text, entities, chatId);
    }


    private SendMessage preparing(StringBuilder text,
                                  List<MessageEntity> entities,
                                  Long chatId) {

        var urls = entities.stream().filter(entity -> entity.getUrl() != null)
                .collect(Collectors.toList()).stream().map(MessageEntity::getUrl)
                .collect(Collectors.toList()).stream().map(string -> string.substring(0, string.lastIndexOf("/")))
                .collect(Collectors.toList()).stream().filter(s -> !s.equals("https://t.me"))
                .collect(Collectors.toList());

        var names = urls.stream().map(this::getName).collect(Collectors.toList());
        names.forEach(name -> text.append(name).append(", "));
        text.deleteCharAt(text.lastIndexOf(", "));

        for(int i = 0; i < names.size(); i++) {
            entities.add(MessageEntity.builder()
                    .text(names.get(i)).url(urls.get(i))
                    .offset(text.indexOf(names.get(i)))
                    .length(names.get(i).length())
                    .type("text_link").build());
        }
        return SendMessage.builder().text(text.toString()).entities(entities).chatId(chatId).build();
    }

    private String getName(String url) {
        try {
            return  Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("https://www.google.com")
                    .get().getElementsByTag("meta")
                    .get(2).attr("content");
        }

        catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }
}
