package com.amiron.booking.user.service;

import com.amiron.booking.user.model.User;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface UserService {

    User save(@NotNull final User user);

    User getById(@NotNull final Long id);

    User update(@NotNull final User user);

    boolean existsById(@NotNull final Long id);
}
