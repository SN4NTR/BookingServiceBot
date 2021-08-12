package com.amiron.booking.bot.resolver;

import com.amiron.booking.bot.command.BotCommand;
import com.amiron.booking.bot.command.BotCommandPattern;
import com.amiron.booking.bot.command.BotCommandsContextHolder;
import com.amiron.booking.bot.exception.BotCommandDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class BotCommandResolver<T extends BotApiObject> {

    private final BotCommandsContextHolder<T> botCommandsContextHolder;

    public BotCommand<T> resolveByPattern(@NotNull final BotCommandPattern botCommandPattern) {
        return botCommandsContextHolder.findByCommand(botCommandPattern)
                .orElseThrow(BotCommandDoesNotExistException::new);
    }
}
