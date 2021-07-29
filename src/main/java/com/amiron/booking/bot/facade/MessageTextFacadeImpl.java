package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.Command;
import com.amiron.booking.bot.command.CommandResolver;
import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.service.BotCommandResolver;
import com.amiron.booking.bot.validator.MessageTextGenericValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class MessageTextFacadeImpl implements MessageTextFacade {

    private final MessageTextGenericValidator messageTextGenericValidator;
    private final BotCommandResolver botCommandResolver;
    private final CommandResolver commandResolver;

    @Override
    public BotApiMethod<?> onReceive(@NotNull final Message message) {
        messageTextGenericValidator.validate(message);

        final String messageText = message.getText();
        final BotCommand botCommand = botCommandResolver.resolveByMessageText(messageText);
        final Command command = commandResolver.resolve(botCommand);

        return command.execute(message);
    }
}
