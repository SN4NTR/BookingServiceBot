package com.amiron.booking.calendar.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.TimePeriod;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
public interface CalendarService {

    List<Event> getUserEvents(@NotNull final String calendarId, @NotNull final DateTime from, @NotNull final DateTime to);

    List<TimePeriod> getUserBusyTime(@NotNull final String calendarId, @NotNull final DateTime from, @NotNull final DateTime to);

    List<Event> getFreeUserEvents(@NotNull final String calendarId, @NotNull final DateTime from, @NotNull final DateTime to);

    List<Event> getUsersFreeDayEvents(@NotNull final String calendarId, @NotNull final DateTime dateTime);

    Event getByStartDateTime(@NotNull final String calendarId, @NotNull final DateTime dateTime);

    void addGuestsToEvent(@NotNull final String calendarId, @NotNull final Event event, @NotNull final List<String> guestEmails);
}
