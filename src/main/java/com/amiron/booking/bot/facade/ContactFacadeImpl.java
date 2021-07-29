package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.Command;
import com.amiron.booking.bot.command.CommandResolver;
import com.amiron.booking.bot.validator.ContactGenericValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Contact;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.bot.model.BotCommand.SET_PHONE_NUMBER;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ContactFacadeImpl implements ContactFacade {

    private final ContactGenericValidator contactGenericValidator;
    private final CommandResolver<Contact> commandResolver;

    @Override
    public BotApiMethod<?> onReceive(@NotNull final Long chatId, @NotNull final Contact contact) {
        contactGenericValidator.validate(contact);

        final Command<Contact> command = commandResolver.resolve(SET_PHONE_NUMBER);

        return command.execute(contact);
    }
}
