package com.amiron.booking.user.validator;

import com.amiron.booking.user.exception.UserAlreadyExistsException;
import com.amiron.booking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class UserExistenceValidator {

    private final UserService userService;

    public void validate(@NotNull final Long id) {
        throwIfExists(id);
    }

    private void throwIfExists(final Long id) {
        if (userService.existsById(id)) {
            throw new UserAlreadyExistsException();
        }
    }
}
