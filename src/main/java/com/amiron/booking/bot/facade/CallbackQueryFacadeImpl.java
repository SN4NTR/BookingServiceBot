package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.Command;
import com.amiron.booking.bot.command.CommandResolver;
import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.service.BotCommandResolver;
import com.amiron.booking.bot.validator.CallbackQueryGenericValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class CallbackQueryFacadeImpl implements CallbackQueryFacade {

    private final CallbackQueryGenericValidator callbackQueryGenericValidator;
    private final BotCommandResolver botCommandResolver;
    private final CommandResolver commandResolver;

    @Override
    public BotApiMethod<?> onReceive(@NotNull final CallbackQuery callbackQuery) {
        callbackQueryGenericValidator.validate(callbackQuery);

        final String data = callbackQuery.getData();
        final BotCommand botCommand = botCommandResolver.resolveByCommandText(data);
        final Command command = commandResolver.resolve(botCommand);

        return command.execute(callbackQuery);
    }
}
