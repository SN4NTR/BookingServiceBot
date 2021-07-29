package com.amiron.booking.bot.service;

import com.amiron.booking.bot.model.BotUpdateType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.bot.model.BotUpdateType.CALLBACK_QUERY;
import static com.amiron.booking.bot.model.BotUpdateType.COMMAND;
import static com.amiron.booking.bot.model.BotUpdateType.CONTACT;
import static com.amiron.booking.bot.model.BotUpdateType.TEXT;
import static com.amiron.booking.bot.model.BotUpdateType.UNKNOWN;
import static java.util.Objects.nonNull;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class BotUpdateTypeResolver {

    public BotUpdateType resolve(@NotNull final Update update) {
        if (isCommandUpdateType(update)) {
            return COMMAND;
        } else if (isTextUpdateType(update)) {
            return TEXT;
        } else if (isCallbackQueryUpdateType(update)) {
            return CALLBACK_QUERY;
        } else if (isContactUpdateType(update)) {
            return CONTACT;
        }
        return UNKNOWN;
    }

    private boolean isCommandUpdateType(final Update update) {
        final Message incomingMessage = update.getMessage();
        return isTextUpdateType(update) && incomingMessage.getText().startsWith("/");
    }

    private boolean isTextUpdateType(final Update update) {
        final Message incomingMessage = update.getMessage();
        return nonNull(incomingMessage) && nonNull(incomingMessage.getText());
    }

    private boolean isCallbackQueryUpdateType(final Update update) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        return nonNull(callbackQuery);
    }

    private boolean isContactUpdateType(final Update update) {
        final Message message = update.getMessage();
        return nonNull(message) && nonNull(message.getContact());
    }
}
