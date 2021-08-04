package com.amiron.booking.client.repository;

import com.amiron.booking.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByTelegramId(@NotNull final Long telegramId);

    Optional<Client> findByUsername(@NotNull final String username);
}
