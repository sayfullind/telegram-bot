package ru.macloud.telegrambottoddles.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Log4j2
@Component
public class BotService {
    private final Bot bot;
    private final BotRepository repository;
    private final BotConfig config;

    @Autowired
    public BotService(Bot bot,
                      BotRepository repository,
                      BotConfig config) {
        this.repository = repository;
        this.config = config;
        this.bot = bot;
    }

    public String showConfiguration() {
        return config.toString();
    }

    public String setConfiguration(String token, String name) {
        config.setName(name);
        config.setToken(token);
        return "The configuration has been set";
    }

    public String restoreConfiguration() {
        var result = repository.findById(config.getID());
        if (result.isEmpty()) return "Saved configuration not found";

        config.setToken(result.get().getToken());
        config.setName(result.get().getName());
        return "Saved configuration found";
    }

    public String saveConfiguration() {
        if(BotConfig.isNotValid(config)) return "Invalid token or name";
        log.info("The config has been saved in database");
        return String.format("%s%nThe parameters have been saved", repository.save(config));
    }

    public String resetConfiguration() {
        if(!repository.existsById(config.getID()))
            return "The previous configuration not found";
        log.info("THe config has been deleted from database");
        repository.deleteById(config.getID());
        return "The previous configuration has been removed";
    }

    public String run() {
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
            log.info("The bot was launched");
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage());
            return "Error when starting the bot. See the details in the logs";
        }
        return "The bot is running";
    }
}
