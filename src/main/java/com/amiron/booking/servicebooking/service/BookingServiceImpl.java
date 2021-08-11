package com.amiron.booking.servicebooking.service;

import com.amiron.booking.servicebooking.model.Booking;
import com.amiron.booking.servicebooking.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking save(@NotNull final Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllByClientUsername(@NotNull final String clientUsername) {
        return bookingRepository.findByClientUsername(clientUsername);
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public void delete(@NotNull final UUID id) {
        bookingRepository.deleteById(id);
    }
}
