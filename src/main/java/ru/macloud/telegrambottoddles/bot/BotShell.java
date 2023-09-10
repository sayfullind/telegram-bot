package ru.macloud.telegrambottoddles.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@Log4j2
@ShellComponent
public class BotShell {
    private final BotService service;

    @Autowired
    public BotShell(BotService service) {
        this.service = service;
    }

    @ShellMethod(key = "config", value = "check the bot properties")
    public String config() {
        return service.showConfiguration();
    }

    @ShellMethod(key = "restore", value = "restore the previous configuration")
    public String restore() {
        return service.restoreConfiguration();
    }

    @ShellMethod(key = "set", value = "set the bot token and name in new session")
    public String set(@ShellOption({"token", "-t"}) String token,
                      @ShellOption({"name", "-n"}) String name) {
        return service.setConfiguration(token, name);
    }

    @ShellMethod(key = "save", value = "save the config")
    public String save() {
        return service.saveConfiguration();
    }

    @ShellMethod(key = "reset", value = "reset the config")
    public String reset() {
        return service.resetConfiguration();
    }

    @ShellMethod(key = "run", value = "run the bot")
    public String run() {
        return service.run();
    }
}
