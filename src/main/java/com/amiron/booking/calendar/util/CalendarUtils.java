package com.amiron.booking.calendar.util;

import com.google.api.client.util.DateTime;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class CalendarUtils {

    public static int getDayOfMonthFromDate(final DateTime dateTime) {
        final LocalDate localDate = getLocalDateFromDateTime(dateTime);
        return localDate.getDayOfMonth();
    }

    public static DateTime getDateTimeFromValues(final int dayOfMonth, final int month, final int year) {
        final LocalDateTime dateTime = LocalDate.of(year, Month.of(month), dayOfMonth).atStartOfDay();
        final Instant instant = dateTime.toInstant(now().getOffset());
        return new DateTime(instant.toEpochMilli());
    }

    public static LocalDateTime getLocalDateTimeFromDateTime(final DateTime dateTime) {
        final long dateTimeValue = dateTime.getValue();
        final Instant instant = Instant.ofEpochMilli(dateTimeValue);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static String getTimeAsStringFromDate(final DateTime dateTime) {
        final LocalDateTime localDateTime = getLocalDateTimeFromDateTime(dateTime);
        final int hours = localDateTime.getHour();
        final int minutes = localDateTime.getMinute();
        final String minutesFormat = minutes < 10 ? "0%s" : String.valueOf(minutes);
        return format("%s:" + minutesFormat, hours, minutes);
    }

    private static LocalDate getLocalDateFromDateTime(final DateTime dateTime) {
        final long dateTimeValue = dateTime.getValue();
        final Instant instant = Instant.ofEpochMilli(dateTimeValue);
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }
}