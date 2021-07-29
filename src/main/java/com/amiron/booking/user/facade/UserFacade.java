package com.amiron.booking.user.facade;

import com.amiron.booking.user.model.User;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface UserFacade {

    User onCreate(@NotNull final User user);

    User onUpdate(@NotNull final User user);
}
