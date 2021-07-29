package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface CallbackQueryFacade {

    BotApiMethod<?> onReceive(@NotNull final CallbackQuery callbackQuery);
}
