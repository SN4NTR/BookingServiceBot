package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
public interface MessageTextFacade {

    List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final Message message);
}
