package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Contact;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface ContactFacade {

    BotApiMethod<?> onReceive(@NotNull final Contact contact);
}
