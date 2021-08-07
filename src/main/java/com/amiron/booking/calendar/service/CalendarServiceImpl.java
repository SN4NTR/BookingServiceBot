package com.amiron.booking.calendar.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class CalendarServiceImpl implements CalendarService {

    private static final String APPLICATION_NAME = "Booking Service";
    private static final String START_TIME_FIELD = "startTime";

    private final GoogleCredential googleCredentials;

    @SneakyThrows
    @Override
    public List<Event> getUserEvents(@NotNull final String userEmail, @NotNull final DateTime from, @NotNull final DateTime to) {
        final Calendar calendarService = getCalendarService();
        return calendarService.events()
                .list(userEmail)
                .setTimeMin(from)
                .setTimeMax(to)
                .setOrderBy(START_TIME_FIELD)
                .setSingleEvents(true)
                .execute()
                .getItems();
    }

    @SneakyThrows
    @Override
    public List<TimePeriod> getUserBusyTime(
            @NotNull final String userEmail, @NotNull final DateTime from, @NotNull final DateTime to) {
        final Calendar calendarService = getCalendarService();
        final FreeBusyRequest freeBusyRequest = buildFreeBusyRequest(userEmail, from, to);
        return calendarService.freebusy()
                .query(freeBusyRequest)
                .execute()
                .getCalendars()
                .get(userEmail)
                .getBusy();
    }

    @Override
    public List<Event> getFreeUserEvents(
            @NotNull final String userEmail, @NotNull final DateTime from, @NotNull final DateTime to) {
        final List<Event> userAllEvents = getUserEvents(userEmail, from, to);
        final List<TimePeriod> userBusyTimePeriods = getUserBusyTime(userEmail, from, to);
        return findFreeUserEvents(userAllEvents, userBusyTimePeriods);
    }

    private List<Event> findFreeUserEvents(final List<Event> userEvents, final List<TimePeriod> userBusyTimePeriods) {
        return userEvents.stream()
                .filter(event -> !isUserBusy(event, userBusyTimePeriods))
                .collect(toList());
    }

    private boolean isUserBusy(final Event event, final List<TimePeriod> userBusyTimePeriods) {
        return userBusyTimePeriods.stream()
                .anyMatch(busyTimePeriod -> areEventsTimeClashes(event, busyTimePeriod));
    }

    private boolean areEventsTimeClashes(final Event event, final TimePeriod busyTimePeriod) {
        final long eventStart = event.getStart().getDateTime().getValue();
        final long eventEnd = event.getEnd().getDateTime().getValue();
        final long busyStart = busyTimePeriod.getStart().getValue();
        final long busyEnd = busyTimePeriod.getEnd().getValue();
        return eventStart == busyStart && eventEnd == busyEnd;
    }

    private FreeBusyRequest buildFreeBusyRequest(final String userEmail, final DateTime from, final DateTime to) {
        final FreeBusyRequestItem freeBusyRequestItem = new FreeBusyRequestItem().setId(userEmail);
        return new FreeBusyRequest()
                .setTimeMin(from)
                .setTimeMax(to)
                .setItems(singletonList(freeBusyRequestItem));
    }

    private Calendar getCalendarService() {
        final HttpTransport httpTransport = googleCredentials.getTransport();
        final JsonFactory jsonFactory = googleCredentials.getJsonFactory();
        return new Calendar.Builder(httpTransport, jsonFactory, googleCredentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
