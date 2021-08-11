package com.amiron.booking.calendar.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.calendar.util.CalendarUtils.addDaysToDateTime;
import static com.amiron.booking.calendar.util.CalendarUtils.getDayOfMonthFromDate;
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
    public List<Event> getUserEvents(@NotNull final String calendarId, @NotNull final DateTime from, @NotNull final DateTime to) {
        final Calendar calendarService = getCalendarService();
        return calendarService.events()
                .list(calendarId)
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
            @NotNull final String calendarId, @NotNull final DateTime from, @NotNull final DateTime to) {
        final Calendar calendarService = getCalendarService();
        final FreeBusyRequest freeBusyRequest = buildFreeBusyRequest(calendarId, from, to);
        return calendarService.freebusy()
                .query(freeBusyRequest)
                .execute()
                .getCalendars()
                .get(calendarId)
                .getBusy();
    }

    @Override
    public List<Event> getFreeUserEvents(
            @NotNull final String calendarId, @NotNull final DateTime from, @NotNull final DateTime to) {
        final List<Event> userAllEvents = getUserEvents(calendarId, from, to);
        final List<TimePeriod> userBusyTimePeriods = getUserBusyTime(calendarId, from, to);
        return findFreeUserEvents(userAllEvents, userBusyTimePeriods);
    }

    @Override
    public List<Event> getFreeUserDayEvents(@NotNull final String calendarId, @NotNull final DateTime dateTime) {
        final DateTime nextDay = addDaysToDateTime(dateTime, 1);
        final List<Event> freeUserDayEvents = getFreeUserEvents(calendarId, dateTime, nextDay);
        return filterOutNextDayEvents(freeUserDayEvents, dateTime);
    }

    @Override
    public Event getByStartDateTime(@NotNull final String calendarId, @NotNull final DateTime startDateTime) {
        final DateTime to = addDaysToDateTime(startDateTime, 1);
        final List<Event> userEvents = getUserEvents(calendarId, startDateTime, to);
        return userEvents.stream()
                .filter(item -> startDateTime.equals(item.getStart().getDateTime()))
                .findFirst()
                .orElseThrow();
    }

    // TODO will throw always... google's domain wide policy must be changed
    @SneakyThrows
    @Override
    public void addGuestsToEvent(@NotNull final String calendarId, @NotNull final Event event, @NotNull final List<String> guestEmails) {
        final Calendar calendarService = getCalendarService();
        final Event updatedEvent = updateEventWithGuests(event, guestEmails);
        final String eventId = updatedEvent.getId();
        calendarService.events()
                .update(calendarId, eventId, event)
                .execute();
    }

    private Event updateEventWithGuests(final Event event, final List<String> guestEmails) {
        final List<EventAttendee> eventAttendees = mapEmailsToEventAttendee(guestEmails);
        event.setAttendees(eventAttendees);
        return event;
    }

    private List<EventAttendee> mapEmailsToEventAttendee(final List<String> guestEmails) {
        return guestEmails.stream()
                .map(this::mapEmailsToEventAttendee)
                .collect(toList());
    }

    private EventAttendee mapEmailsToEventAttendee(final String guestEmail) {
        final EventAttendee eventAttendee = new EventAttendee();
        eventAttendee.setEmail(guestEmail);
        return eventAttendee;
    }

    private List<Event> filterOutNextDayEvents(final List<Event> events, final DateTime dateTime) {
        return events.stream()
                .filter(event -> isCurrentDayEvent(event, dateTime))
                .collect(toList());
    }

    private boolean isCurrentDayEvent(final Event event, final DateTime dateTime) {
        final DateTime eventsStartDateTime = event.getStart().getDateTime();
        final int dayOfMonthFromDate = getDayOfMonthFromDate(dateTime);
        final int eventsDayOfMonth = getDayOfMonthFromDate(eventsStartDateTime);
        return dayOfMonthFromDate == eventsDayOfMonth;
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
