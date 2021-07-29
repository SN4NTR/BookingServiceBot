package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface MessageCommandFacade {

    BotApiMethod<?> onReceive(@NotNull final Message message);
}
