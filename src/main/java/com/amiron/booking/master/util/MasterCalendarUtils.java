package com.amiron.booking.master.util;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Calendar;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class MasterCalendarUtils {

    public static InlineKeyboardMarkup updateKeyboardMarkupWithFreeDates(
            final InlineKeyboardMarkup calendarKeyboard, final List<Event> freeMasterBookings) {
        final List<List<InlineKeyboardButton>> buttons = calendarKeyboard.getKeyboard();
        buttons.forEach(rowButtons -> updateButtonsText(rowButtons, freeMasterBookings));
        return calendarKeyboard;
    }

    private static void updateButtonsText(
            final List<InlineKeyboardButton> rowButtons, final List<Event> freeMasterBookings) {
        rowButtons.stream()
                .filter(MasterCalendarUtils::isButtonDayOfMonth)
                .filter(button -> isCleaningRequired(button, freeMasterBookings))
                .forEach(MasterCalendarUtils::clearButton);
    }

    private static boolean isButtonDayOfMonth(final InlineKeyboardButton button) {
        final String text = button.getText();
        return isNumeric(text);
    }

    private static boolean isCleaningRequired(final InlineKeyboardButton button, final List<Event> freeMasterBookings) {
        final int buttonsDayOfMonth = Integer.parseInt(button.getText());
        return freeMasterBookings.stream()
                .noneMatch(booking -> hasAnyBookingForDay(buttonsDayOfMonth, booking));
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
