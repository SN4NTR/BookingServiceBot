package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
public interface CallbackQueryFacade {

    List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final CallbackQuery callbackQuery);
}
