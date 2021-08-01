package com.amiron.booking.client.service;

import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client save(@NotNull final Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client getTelegramId(@NotNull final Long id) {
        return clientRepository.findByTelegramId(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Client update(@NotNull final Client client) {
        final Long telegramId = client.getTelegramId();
        final Client existingClient = getTelegramId(telegramId);

        existingClient.setUsername(client.getUsername());
        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setChatId(client.getChatId());
        existingClient.setPhoneNumber(client.getPhoneNumber());

        return save(client);
    }

    @Override
    public boolean existsByTelegramId(@NotNull final Long id) {
        return clientRepository.findByTelegramId(id).isPresent();
    }
}
