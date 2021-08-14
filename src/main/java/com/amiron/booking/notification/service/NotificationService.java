package com.amiron.booking.notification.service;

import com.amiron.booking.bot.handler.BotHandler;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.master.model.Master;
import com.amiron.booking.servicebooking.model.Booking;
import com.amiron.booking.servicebooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.amiron.booking.bot.util.MessageBuilder.buildSendMessage;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Miron
 */
@RequiredArgsConstructor
@EnableScheduling
@Service
public class NotificationService {

    private final BookingService bookingService;
    private final BotHandler botHandler;

    @Value("${service.notification.before-hours}")
    private Integer notifyBeforeHours;

    @Scheduled(fixedRate = 60_000)
    public void notifyAboutBooking() {
        final List<Booking> bookings = bookingService.getAll();
        final List<Booking> bookingsToNotify = findBookingsToNotify(bookings);
        notifyClientsAboutBooking(bookingsToNotify);
    }

    private void notifyClientsAboutBooking(final List<Booking> bookings) {
        bookings.forEach(this::notifyClientAboutBooking);
    }

    @SneakyThrows
    private void notifyClientAboutBooking(final Booking booking) {
        final Client client = booking.getClient();
        final Long chatId = client.getChatId();
        final SendMessage message = buildMessage(chatId, booking);
        botHandler.sendMessage(message);
        updateBookingNotificationFlag(booking);
    }

    private void updateBookingNotificationFlag(final Booking booking) {
        booking.setNotificationSent(true);
        bookingService.save(booking);
    }

    private SendMessage buildMessage(final Long chatId, final Booking booking) {
        final String text = buildBookingText(booking);
        return buildSendMessage(chatId, text, null);
    }

    private String buildBookingText(final Booking booking) {
        final Master master = booking.getMaster();
        final String firstName = master.getFirstName();
        final String lastName = master.getLastName();
        final LocalDateTime dateTime = booking.getDateTime();
        final int year = dateTime.getYear();
        final int month = dateTime.getMonthValue();
        final int dayOfMonth = dateTime.getDayOfMonth();
        final int hour = dateTime.getHour();
        final int minute = dateTime.getMinute();
        return format("""
                <b>Booking reminder notification</b>
                                
                Dear client, we would like to remind you about booked service.
                Master: %s %s
                Date: %s.%s.%s
                Time: %s:%s
                """, firstName, lastName, dayOfMonth, month, year, hour, minute);
    }

    private List<Booking> findBookingsToNotify(final List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> !booking.isNotificationSent())
                .filter(this::isNotificationRequired)
                .collect(toList());
    }

    private boolean isNotificationRequired(final Booking booking) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        final LocalDate currentDate = currentDateTime.toLocalDate();
        final LocalTime currentTime = currentDateTime.toLocalTime();
        final LocalDateTime bookingDateTime = booking.getDateTime();
        final LocalDate bookingDate = bookingDateTime.toLocalDate();
        final LocalTime bookingTime = bookingDateTime.toLocalTime();
        return hasEqualDates(currentDate, bookingDate) && hasRequiredDifferenceInHours(currentTime, bookingTime);
    }

    private boolean hasEqualDates(final LocalDate currentDate, final LocalDate bookingDate) {
        final int currentYear = currentDate.getYear();
        final int currentMonth = currentDate.getMonthValue();
        final int currentDayOfMonth = currentDate.getDayOfMonth();
        final int bookingYear = bookingDate.getYear();
        final int bookingMonth = bookingDate.getMonthValue();
        final int bookingDayOfMonth = bookingDate.getDayOfMonth();
        return hasEqualYears(currentYear, bookingYear)
                && hasEqualMonths(currentMonth, bookingMonth)
                && hasEqualDaysOfMonth(currentDayOfMonth, bookingDayOfMonth);
    }

    private boolean hasEqualYears(final int currentYear, final int bookingYear) {
        return currentYear == bookingYear;
    }

    private boolean hasEqualMonths(final int currentMonth, final int bookingMonth) {
        return currentMonth == bookingMonth;
    }

    private boolean hasEqualDaysOfMonth(final int currentDayOfMonth, final int bookingDayOfMonth) {
        return currentDayOfMonth == bookingDayOfMonth;
    }

    private boolean hasRequiredDifferenceInHours(final LocalTime currentTime, final LocalTime bookingTime) {
        final int currentTimeInMinutes = convertTimeInMinutes(currentTime);
        final int bookingTimeInMinutes = convertTimeInMinutes(bookingTime);
        final int notifyBeforeInMinutes = notifyBeforeHours * 60;
        return bookingTimeInMinutes - currentTimeInMinutes <= notifyBeforeInMinutes;
    }

    private int convertTimeInMinutes(final LocalTime time) {
        final int hour = time.getHour();
        final int minute = time.getMinute();
        return hour * 60 + minute;
    }
}
