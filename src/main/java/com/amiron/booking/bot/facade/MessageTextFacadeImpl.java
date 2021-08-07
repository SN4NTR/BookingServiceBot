package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.BotCommand;
import com.amiron.booking.bot.model.UserCommand;
import com.amiron.booking.bot.resolver.BotCommandResolver;
import com.amiron.booking.bot.resolver.UserCommandResolver;
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
    private final UserCommandResolver userCommandResolver;
    private final BotCommandResolver<Message> botCommandResolver;

    @Override
    public List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final Message message) {
        messageTextGenericValidator.validate(message);

        final String messageText = message.getText();
        final UserCommand userCommand = userCommandResolver.resolveByMessageText(messageText);
        final BotCommand<Message> botCommand = botCommandResolver.resolve(userCommand);

        return botCommand.execute(message);
    }
}
