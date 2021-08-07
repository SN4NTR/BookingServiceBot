package com.amiron.booking.bot.resolver;

import com.amiron.booking.bot.model.MessageType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import static com.amiron.booking.bot.model.MessageType.PHOTO;
import static com.amiron.booking.bot.model.MessageType.TEXT;
import static com.amiron.booking.bot.model.MessageType.UNKNOWN;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class BotMessageTypeResolver {

    public MessageType resolve(final PartialBotApiMethod<?> message) {
        if (message instanceof BotApiMethod) {
            return TEXT;
        } else if (message instanceof SendPhoto) {
            return PHOTO;
        }
        return UNKNOWN;
    }
}
