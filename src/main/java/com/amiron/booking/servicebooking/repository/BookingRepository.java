package com.amiron.booking.servicebooking.repository;

import com.amiron.booking.servicebooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Aliaksandr Miron
 */
public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
