package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.bot.model.BotCommand.BOOK_NAILS;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class BookNailsCommand extends Command<CallbackQuery> {

    @Override
    public BotApiMethod<?> execute(@NotNull final CallbackQuery message) {
        // TODO logic for calendar
        return null;
    }

    @Override
    public BotCommand getCommand() {
        return BOOK_NAILS;
    }
}
