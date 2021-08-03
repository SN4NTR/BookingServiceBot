package com.amiron.booking.bot.facade;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Contact;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
public interface ContactFacade {

    List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final Contact contact);
}
