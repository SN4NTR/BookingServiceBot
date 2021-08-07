package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.command.BotCommand;
import com.amiron.booking.bot.resolver.BotCommandResolver;
import com.amiron.booking.bot.validator.ContactGenericValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Contact;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.model.UserCommand.SET_PHONE_NUMBER;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ContactFacadeImpl implements ContactFacade {

    private final ContactGenericValidator contactGenericValidator;
    private final BotCommandResolver<Contact> botCommandResolver;

    @Override
    public List<? extends PartialBotApiMethod<?>> onReceive(@NotNull final Contact contact) {
        contactGenericValidator.validate(contact);

        final BotCommand<Contact> botCommand = botCommandResolver.resolve(SET_PHONE_NUMBER);

        return botCommand.execute(contact);
    }
}
