package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface BotFacade {

    BotApiMethod<?> onUpdateEvent(@NotNull final Update update);
}
