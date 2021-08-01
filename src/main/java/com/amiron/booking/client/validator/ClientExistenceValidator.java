package com.amiron.booking.client.validator;

import com.amiron.booking.client.exception.ClientAlreadyExistsException;
import com.amiron.booking.client.service.ClientService;
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
public class ClientExistenceValidator {

    private final ClientService clientService;

    public void validate(@NotNull final Long id) {
        throwIfExists(id);
    }

    private void throwIfExists(final Long id) {
        if (clientService.existsByTelegramId(id)) {
            throw new ClientAlreadyExistsException();
        }
    }
}
