package com.amiron.booking.calendar.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class CalendarServiceImpl implements CalendarService {

    private static final String APPLICATION_NAME = "Booking Service";

    private final GoogleCredential googleCredentials;

    @SneakyThrows
    @Override
    public List<Event> getUserEvents(@NotNull final String userEmail) {
        final Calendar calendar = getCalendar();
        final DateTime now = new DateTime(System.currentTimeMillis());
        final Events events = calendar.events().list(userEmail)
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        return events.getItems();
    }

    private Calendar getCalendar() {
        final HttpTransport httpTransport = googleCredentials.getTransport();
        final JsonFactory jsonFactory = googleCredentials.getJsonFactory();
        return new Calendar.Builder(httpTransport, jsonFactory, googleCredentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
