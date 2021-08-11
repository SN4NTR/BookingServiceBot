package com.amiron.booking.master.service;

import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.repository.MasterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;

    @Override
    public List<Master> getAll() {
        return masterRepository.findAll();
    }

    @Override
    public Master getById(@NotNull final UUID id) {
        return masterRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Master getByEmail(@NotNull final String email) {
        return masterRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }
}
