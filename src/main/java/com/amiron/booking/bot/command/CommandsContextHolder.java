package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
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
public class CommandsContextHolder<T extends BotApiObject> {

    private final List<Command<T>> commands;

    public Optional<Command<T>> findByCommand(final BotCommand command) {
        return commands.stream()
                .filter(c -> c.getCommand().equals(command))
                .findFirst();
    }
}
