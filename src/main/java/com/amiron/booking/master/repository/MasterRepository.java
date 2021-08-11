package com.amiron.booking.master.repository;

import com.amiron.booking.master.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
public interface MasterRepository extends JpaRepository<Master, UUID> {

    Optional<Master> findByEmail(@NotNull final String email);
}
