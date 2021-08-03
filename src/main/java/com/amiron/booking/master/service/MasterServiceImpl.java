package com.amiron.booking.master.service;

import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.repository.MasterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
}
