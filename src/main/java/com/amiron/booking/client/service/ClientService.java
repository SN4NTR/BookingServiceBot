package com.amiron.booking.client.service;

import com.amiron.booking.client.model.Client;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface ClientService {

    Client save(@NotNull final Client client);

    Client getTelegramId(@NotNull final Long id);

    Client update(@NotNull final Client client);

    boolean existsByTelegramId(@NotNull final Long id);
}
