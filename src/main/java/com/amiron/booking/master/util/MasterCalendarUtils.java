package com.amiron.booking.master.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static com.amiron.booking.calendar.util.CalendarUtils.getLocalDateFromDateTime;
import static java.util.Calendar.DAY_OF_MONTH;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class MasterCalendarUtils {

    public static InlineKeyboardMarkup updateKeyboardMarkupWithMasterFreeDates(
            final InlineKeyboardMarkup calendarKeyboard, final DateTime dateTime, final List<Event> freeMasterBookings) {
        final List<List<InlineKeyboardButton>> buttons = calendarKeyboard.getKeyboard();
        buttons.forEach(rowButtons -> updateButtonsText(rowButtons, dateTime, freeMasterBookings));
        return calendarKeyboard;
    }

    public static InlineKeyboardMarkup updateKeyboardMarkupWithMasterEmail(
            final InlineKeyboardMarkup calendarKeyboard, final String email) {
        final List<List<InlineKeyboardButton>> buttons = calendarKeyboard.getKeyboard();
        buttons.forEach(rowButtons -> updateButtonsCallbackData(rowButtons, email));
        return calendarKeyboard;
    }

    private static void updateButtonsCallbackData(final List<InlineKeyboardButton> rowButtons, final String email) {
        rowButtons.forEach(button -> setEmailInCallbackData(button, email));
    }

    private static void setEmailInCallbackData(final InlineKeyboardButton button, final String email) {
        final String callbackData = button.getCallbackData();
        final String formattedCallbackData = String.format(callbackData, email);
        button.setCallbackData(formattedCallbackData);
    }

    private static void updateButtonsText(
            final List<InlineKeyboardButton> rowButtons, final DateTime dateTime, final List<Event> freeMasterBookings) {
        rowButtons.stream()
                .filter(MasterCalendarUtils::isButtonDayOfMonth)
                .filter(button -> isCleaningRequired(button, dateTime, freeMasterBookings))
                .forEach(MasterCalendarUtils::clearButton);
    }

    private static boolean isButtonDayOfMonth(final InlineKeyboardButton button) {
        final String text = button.getText();
        return isNumeric(text);
    }

    private static boolean isCleaningRequired(final InlineKeyboardButton button, final DateTime dateTime, final List<Event> freeMasterBookings) {
        final int buttonsDayOfMonth = Integer.parseInt(button.getText());
        return freeMasterBookings.stream()
                .filter(booking -> isBookingInRequiredMonth(dateTime, booking))
                .noneMatch(booking -> hasAnyBookingForDay(buttonsDayOfMonth, booking));
    }

    private static boolean isBookingInRequiredMonth(final DateTime requiredDateTime, final Event freeMasterBooking) {
        final DateTime bookingDateTime = freeMasterBooking.getStart().getDateTime();
        final LocalDate requiredLocalDate = getLocalDateFromDateTime(requiredDateTime);
        final LocalDate bookingLocalDate = getLocalDateFromDateTime(bookingDateTime);
        final int requiredMonth = requiredLocalDate.getMonth().getValue();
        final int requiredYear = requiredLocalDate.getYear();
        final int bookingMonth = bookingLocalDate.getMonth().getValue();
        final int bookingYear = bookingLocalDate.getYear();
        return requiredMonth == bookingMonth && requiredYear == bookingYear;
    }

    private static boolean hasAnyBookingForDay(final int buttonsDayOfMonth, final Event freeMasterBooking) {
        final int bookingStartDayOfMonth = getBookingStartDayOfMonth(freeMasterBooking);
        return buttonsDayOfMonth == bookingStartDayOfMonth;
    }

    private static int getBookingStartDayOfMonth(final Event freeMasterBooking) {
        final EventDateTime bookingStart = freeMasterBooking.getStart();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(bookingStart.getDateTime().getValue());
        return calendar.get(DAY_OF_MONTH);
    }

    private static void clearButton(final InlineKeyboardButton button) {
        button.setText(" ");
        button.setCallbackData("/emptyDay");
    }
}
