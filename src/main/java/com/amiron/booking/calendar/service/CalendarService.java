package com.amiron.booking.calendar.service;

import com.google.api.services.calendar.model.Event;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
public interface CalendarService {

    List<Event> getUserEvents(@NotNull final String userEmail);
}
