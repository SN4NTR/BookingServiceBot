package com.amiron.booking.servicebooking.service;

import com.amiron.booking.servicebooking.model.Booking;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
public interface BookingService {

    Booking save(@NotNull final Booking booking);

    List<Booking> getAll();
}
