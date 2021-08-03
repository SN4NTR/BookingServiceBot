package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.Command;
import com.amiron.booking.bot.command.CommandResolver;
import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.service.BotCommandResolver;
import com.amiron.booking.bot.validator.MessageCommandGenericValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class MessageCommandFacadeImpl implements MessageCommandFacade {

    private final MessageCommandGenericValidator messageCommandGenericValidator;
    private final BotCommandResolver botCommandResolver;
    private final CommandResolver<Message> commandResolver;

    @Override
    public List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final Message commandMessage) {
        messageCommandGenericValidator.validate(commandMessage);

        final String commandText = commandMessage.getText();
        final BotCommand botCommand = botCommandResolver.resolveByCommandText(commandText);
        final Command<Message> command = commandResolver.resolve(botCommand);

        return command.execute(commandMessage);
    }
}
