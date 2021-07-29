package com.amiron.booking.bot.validator;

import com.amiron.booking.core.validator.RequiredFieldValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Contact;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ContactGenericValidator {

    private final PhoneNumberValidator phoneNumberValidator;
    private final RequiredFieldValidator requiredFieldValidator;

    public void validate(@NotNull final Contact contact) {
        final String phoneNumber = contact.getPhoneNumber();

        requiredFieldValidator.validate(phoneNumber);
        phoneNumberValidator.validate(phoneNumber);
    }
}
