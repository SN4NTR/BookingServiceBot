package com.amiron.booking.user.facade;

import com.amiron.booking.user.model.User;
import com.amiron.booking.user.service.UserService;
import com.amiron.booking.user.validator.UserExistenceValidator;
import com.amiron.booking.user.validator.UserGenericValidator;
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
public class UserFacadeImpl implements UserFacade {

    private final UserGenericValidator userGenericValidator;
    private final UserExistenceValidator userExistenceValidator;
    private final UserService userService;

    @Override
    public User onCreate(@NotNull final User user) {
        validateOnCreate(user);

        return userService.save(user);
    }

    @Override
    public User onUpdate(@NotNull final User user) {
        validateOnUpdate(user);

        return userService.update(user);
    }

    private void validateOnCreate(final User user) {
        final Long id = user.getId();

        userGenericValidator.validate(user);
        userExistenceValidator.validate(id);
    }

    private void validateOnUpdate(final User user) {
        userGenericValidator.validate(user);
    }
}
