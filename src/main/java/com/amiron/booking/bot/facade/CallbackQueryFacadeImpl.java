package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.BotCommand;
import com.amiron.booking.bot.model.UserCommand;
import com.amiron.booking.bot.resolver.BotCommandResolver;
import com.amiron.booking.bot.resolver.UserCommandResolver;
import com.amiron.booking.bot.validator.CallbackQueryGenericValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class CallbackQueryFacadeImpl implements CallbackQueryFacade {

    private final CallbackQueryGenericValidator callbackQueryGenericValidator;
    private final UserCommandResolver userCommandResolver;
    private final BotCommandResolver<CallbackQuery> botCommandResolver;

    @Override
    public List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final CallbackQuery callbackQuery) {
        callbackQueryGenericValidator.validate(callbackQuery);

        final String data = callbackQuery.getData();
        final UserCommand userCommand = userCommandResolver.resolveByCommandText(data);
        final BotCommand<CallbackQuery> botCommand = botCommandResolver.resolve(userCommand);

        return botCommand.execute(callbackQuery);
    }
}
