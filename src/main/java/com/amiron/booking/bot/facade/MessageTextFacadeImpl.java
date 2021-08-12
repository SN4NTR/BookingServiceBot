package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.BotCommand;
import com.amiron.booking.bot.command.BotCommandPattern;
import com.amiron.booking.bot.exception.BotCommandDoesNotExistException;
import com.amiron.booking.bot.resolver.BotCommandPatternResolver;
import com.amiron.booking.bot.resolver.BotCommandResolver;
import com.amiron.booking.bot.validator.MessageTextGenericValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
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
@Component
public class MessageTextFacadeImpl implements MessageTextFacade {

    private final MessageTextGenericValidator messageTextGenericValidator;
    private final BotCommandPatternResolver botCommandPatternResolver;
    private final BotCommandResolver<Message> botCommandResolver;

    @Override
    public List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final Message message) {
        messageTextGenericValidator.validate(message);

        final String messageText = message.getText();
        final BotCommandPattern botCommandPattern = botCommandPatternResolver.resolveByMessageText(messageText).orElseThrow(BotCommandDoesNotExistException::new);
        final BotCommand<Message> botCommand = botCommandResolver.resolveByPattern(botCommandPattern);

        return botCommand.execute(message);
    }
}
