package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
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

    private final List<BotCommand<T>> botCommands;

    public Optional<BotCommand<T>> findByCommand(final UserCommand command) {
        return botCommands.stream()
                .filter(c -> c.getCommand().equals(command))
                .findFirst();
    }
}
