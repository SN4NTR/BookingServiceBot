package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
public interface BotFacade {

    List<? extends PartialBotApiMethod<?>> onUpdateEvent(@NotNull final Update update);
}
