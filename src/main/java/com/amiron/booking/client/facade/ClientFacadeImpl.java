package com.amiron.booking.client.facade;

import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.service.ClientService;
import com.amiron.booking.client.validator.ClientExistenceValidator;
import com.amiron.booking.client.validator.ClientGenericValidator;
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
public class ClientFacadeImpl implements ClientFacade {

    private final ClientGenericValidator clientGenericValidator;
    private final ClientExistenceValidator clientExistenceValidator;
    private final ClientService clientService;

    @Override
    public Client onCreate(@NotNull final Client client) {
        validateOnCreate(client);

        return clientService.save(client);
    }

    @Override
    public Client onUpdate(@NotNull final Client client) {
        validateOnUpdate(client);

        return clientService.update(client);
    }

    private void validateOnCreate(final Client client) {
        final Long id = client.getId();

        clientGenericValidator.validate(client);
        clientExistenceValidator.validate(id);
    }

    private void validateOnUpdate(final Client client) {
        clientGenericValidator.validate(client);
    }
}
