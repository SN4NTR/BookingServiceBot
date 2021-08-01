package com.amiron.booking.client.facade;

import com.amiron.booking.client.model.Client;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
public interface ClientFacade {

    Client onCreate(@NotNull final Client client);

    Client onUpdate(@NotNull final Client client);
}
