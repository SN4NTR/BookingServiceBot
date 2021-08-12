package com.amiron.booking.bot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

import java.util.List;
import java.util.Optional;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Component
@Getter
public class BotCommandsContextHolder<T extends BotApiObject> {

    private final List<BotCommand<T>> botCommands;

    public Optional<BotCommand<T>> findByCommand(final BotCommandPattern command) {
        return botCommands.stream()
                .filter(c -> c.getCommandPattern().equals(command))
                .findFirst();
    }
}
