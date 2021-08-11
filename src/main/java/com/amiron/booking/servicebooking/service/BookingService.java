package com.amiron.booking.servicebooking.service;

import com.amiron.booking.servicebooking.model.Booking;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
public interface BookingService {

    Booking save(@NotNull final Booking booking);

    List<Booking> getAllByClientUsername(@NotNull final String username);

    List<Booking> getAll();

    void delete(@NotNull final UUID id);
}
