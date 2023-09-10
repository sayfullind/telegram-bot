package ru.macloud.telegrambottoddles.bot;

import lombok.Data;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Data
@Entity @Component
public class BotConfig {
    @Id private final int ID = 1;
    private String token;
    private String name;

    @Override
    public String toString() {
        return
                String.format("Config:%nCurrent token: %s%nCurrent name: %s", token, name);
    }

    public static boolean isNotValid(BotConfig config) {
        return
                config.getToken() == null || config.getName() == null;
    }
}
