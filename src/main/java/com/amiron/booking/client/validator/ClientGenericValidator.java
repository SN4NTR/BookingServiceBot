package com.amiron.booking.client.validator;

import com.amiron.booking.client.model.Client;
import com.amiron.booking.core.validator.EmailValidator;
import com.amiron.booking.core.validator.PhoneNumberValidator;
import com.amiron.booking.core.validator.RequiredFieldValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ClientGenericValidator {

    private final RequiredFieldValidator requiredFieldValidator;
    private final PhoneNumberValidator phoneNumberValidator;
    private final EmailValidator emailValidator;

    public void validate(final Client client) {
        final Long telegramId = client.getTelegramId();
        final String username = client.getUsername();
        final Optional<String> firstNameOpt = Optional.ofNullable(client.getFirstName());
        final Optional<String> lastNameOpt = Optional.ofNullable(client.getLastName());
        final Optional<String> emailOpt = Optional.ofNullable(client.getEmail());
        final Optional<String> phoneNumberOpt = Optional.ofNullable(client.getPhoneNumber());

        validateId(telegramId);
        validateUsername(username);
        firstNameOpt.ifPresent(this::validateFirstName);
        lastNameOpt.ifPresent(this::validateLastName);
        emailOpt.ifPresent(this::validateEmail);
        phoneNumberOpt.ifPresent(this::validatePhoneNumber);
    }

    private void validateId(final Long id) {
        requiredFieldValidator.validate(id);
    }

    private void validateUsername(final String username) {
        requiredFieldValidator.validate(username);
    }

    private void validateFirstName(final String firstName) {
        requiredFieldValidator.validate(firstName);
    }

    private void validateLastName(final String lastName) {
        requiredFieldValidator.validate(lastName);
    }

    private void validatePhoneNumber(final String phoneNumber) {
        requiredFieldValidator.validate(phoneNumber);
        phoneNumberValidator.validate(phoneNumber);
    }

    private void validateEmail(final String email) {
        requiredFieldValidator.validate(email);
        emailValidator.validate(email);
    }
}
