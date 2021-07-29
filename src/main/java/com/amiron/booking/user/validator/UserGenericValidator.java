package com.amiron.booking.user.validator;

import com.amiron.booking.bot.validator.EmailValidator;
import com.amiron.booking.bot.validator.PhoneNumberValidator;
import com.amiron.booking.core.validator.RequiredFieldValidator;
import com.amiron.booking.user.model.User;
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
public class UserGenericValidator {

    private final RequiredFieldValidator requiredFieldValidator;
    private final PhoneNumberValidator phoneNumberValidator;
    private final EmailValidator emailValidator;

    public void validate(final User user) {
        final Long id = user.getId();
        final String username = user.getUsername();
        final Optional<String> firstNameOpt = Optional.ofNullable(user.getFirstName());
        final Optional<String> lastNameOpt = Optional.ofNullable(user.getLastName());
        final Optional<String> emailOpt = Optional.ofNullable(user.getEmail());
        final Optional<String> phoneNumberOpt = Optional.ofNullable(user.getPhoneNumber());

        validateId(id);
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
