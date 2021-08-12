package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.BotCommand;
import com.amiron.booking.bot.command.BotCommandPattern;
import com.amiron.booking.bot.resolver.BotCommandPatternResolver;
import com.amiron.booking.bot.resolver.BotCommandResolver;
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
    private final BotCommandPatternResolver botCommandPatternResolver;
    private final BotCommandResolver<Message> botCommandResolver;

    @Override
    public List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final Message message) {
        messageCommandGenericValidator.validate(message);

        final BotCommand<Message> botCommand = getBotCommand(message);

        return botCommand.execute(message);
    }

    private BotCommand<Message> getBotCommand(final Message message) {
        final String commandText = message.getText();
        final BotCommandPattern botCommandPattern = botCommandPatternResolver.resolveByCommandText(commandText);
        return botCommandResolver.resolveByPattern(botCommandPattern);
    }
}
