package com.amiron.booking.bot.service;

import com.amiron.booking.bot.model.MessageType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import static com.amiron.booking.bot.model.MessageType.PHOTO;
import static com.amiron.booking.bot.model.MessageType.SIMPLE_MESSAGE;
import static com.amiron.booking.bot.model.MessageType.UNKNOWN;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class MessageTypeResolver {

    public MessageType resolve(final PartialBotApiMethod<?> message) {
        if (message instanceof BotApiMethod) {
            return SIMPLE_MESSAGE;
        } else if (message instanceof SendPhoto) {
            return PHOTO;
        }
        return UNKNOWN;
    }
}
