package com.amiron.booking.master.service;

import com.amiron.booking.master.model.Master;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
public interface MasterService {

    List<Master> getAll();

    Master getById(@NotNull final UUID id);

    Master getByEmail(@NotNull final String email);
}
