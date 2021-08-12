package com.amiron.booking.bot.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_DAY;
import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_MONTH;
import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_TIME;
import static com.amiron.booking.calendar.util.CalendarUtils.getLocalDateTimeFromDateTime;
import static com.amiron.booking.calendar.util.CalendarUtils.getTimeAsStringFromDate;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class KeyboardBuilder {

    private static final int CALENDAR_COLUMNS_AMOUNT = 7;

    public static ReplyKeyboardMarkup buildMarkupForPhoneNumber() {
        final KeyboardButton sendPhoneNumberButton = new KeyboardButton("Send phone number");
        sendPhoneNumberButton.setRequestContact(true);
        final KeyboardRow firstKeyboardRow = new KeyboardRow(singletonList(sendPhoneNumberButton));
        final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(singletonList(firstKeyboardRow));
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboardRemove buildKeyboardRemove() {
        return new ReplyKeyboardRemove(true);
    }

    public static InlineKeyboardMarkup buildInlineKeyboardMarkup(final String text, final String callback) {
        final InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText(text);
        keyboardButton.setCallbackData(callback);
        return InlineKeyboardMarkup.builder()
                .keyboardRow(singletonList(keyboardButton))
                .build();
    }

    public static InlineKeyboardMarkup buildInlineKeyboardMarkupWithButtonsFromNewLine(
            final InlineKeyboardButton... buttons) {
        final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> inlineButtons = Arrays.stream(buttons)
                .map(Arrays::asList)
                .collect(Collectors.toList());
        keyboardMarkup.setKeyboard(inlineButtons);
        return keyboardMarkup;
    }

    public static InlineKeyboardButton buildInlineKeyboardButton(final String text, final String callback) {
        final InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText(text);
        keyboardButton.setCallbackData(callback);
        return keyboardButton;
    }

    public static InlineKeyboardMarkup buildInlineKeyboardMarkupWithSequentialButtons(
            final InlineKeyboardButton... buttons) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(asList(buttons))
                .build();
    }

    public static InlineKeyboardMarkup buildCalendarKeyboardMarkupForCurrentMonth() {
        final LocalDate now = LocalDate.now();
        final int year = now.getYear();
        final int month = now.getMonthValue();
        final int dayOfMonth = now.getDayOfMonth();
        return buildCalendarKeyboardForDate(dayOfMonth, month, year);
    }

    public static InlineKeyboardMarkup buildCalendarKeyboardForDate(final int day, final int month, final int year) {
        final LocalDate date = LocalDate.of(year, month, day);
        return buildCalendarKeyboardForDate(date);
    }

    public static InlineKeyboardMarkup buildCalendarKeyboardForDate(final LocalDate date) {
        return buildKeyboardMarkup(date);
    }

    public static InlineKeyboardMarkup buildCalendarKeyboardMarkupForDay(final List<Event> masterDayBookings) {
        final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> buttons = buildKeyboardButtonsLinesForDay(masterDayBookings);
        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }

    private static List<List<InlineKeyboardButton>> buildKeyboardButtonsLinesForDay(final List<Event> masterDayBookings) {
        final List<InlineKeyboardButton> buttonsRows = masterDayBookings.stream()
                .map(KeyboardBuilder::buildTimeButton)
                .collect(Collectors.toList());
        return List.of(buttonsRows);
    }

    private static InlineKeyboardButton buildTimeButton(final Event booking) {
        final DateTime startDateTime = booking.getStart().getDateTime();
        final DateTime endDateTime = booking.getEnd().getDateTime();
        final String startTime = getTimeAsStringFromDate(startDateTime);
        final String endTime = getTimeAsStringFromDate(endDateTime);
        final LocalDateTime bookingLocalDateTime = getLocalDateTimeFromDateTime(startDateTime);
        final int dayOfMonth = bookingLocalDateTime.getDayOfMonth();
        final int month = bookingLocalDateTime.getMonth().getValue();
        final int year = bookingLocalDateTime.getYear();
        final int hours = bookingLocalDateTime.getHour();
        final int minutes = bookingLocalDateTime.getMinute();

        final InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(format("%s-%s", startTime, endTime));
        button.setCallbackData(format(CHOOSE_TIME.getPatternTemplate(), "%s", dayOfMonth, month, year, hours, minutes));
        return button;
    }

    private static InlineKeyboardMarkup buildKeyboardMarkup(final LocalDate date) {
        final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> buttonsLines = buildKeyboardButtonsRowsForMonth(date);
        keyboardMarkup.setKeyboard(buttonsLines);
        return keyboardMarkup;
    }

    private static List<List<InlineKeyboardButton>> buildKeyboardButtonsRowsForMonth(final LocalDate date) {
        final List<InlineKeyboardButton> monthNavigationButtons = buildMonthNavigationButtons(date);
        final List<InlineKeyboardButton> daysOfWeekButtons = buildDaysOfWeekButtons();
        final List<List<InlineKeyboardButton>> buttons = buildDaysButtons(date);
        buttons.add(0, monthNavigationButtons);
        buttons.add(1, daysOfWeekButtons);
        return buttons;
    }

    private static List<InlineKeyboardButton> buildMonthNavigationButtons(final LocalDate date) {
        final InlineKeyboardButton previousMonthButton = buildPreviousMonthButton(date);
        final InlineKeyboardButton currentMonthButton = buildCurrentMonthButton(date);
        final InlineKeyboardButton nextMonthButton = buildNextMonthButton(date);
        return asList(previousMonthButton, currentMonthButton, nextMonthButton);
    }

    private static InlineKeyboardButton buildPreviousMonthButton(final LocalDate date) {
        final LocalDate now = LocalDate.now();
        final int currentYear = now.getYear();
        final int currentMonth = now.getMonth().getValue();

        final Month month = date.getMonth();
        final int previousMonthValue = month.minus(1).getValue();
        final int year = date.getYear();
        final int dayOfMonth = currentYear == year && currentMonth == previousMonthValue
                ? now.getDayOfMonth()
                : 1;

        final String callbackData = format(CHOOSE_MONTH.getPatternTemplate(), "%s", dayOfMonth, previousMonthValue, year);
        final InlineKeyboardButton previousMonthButton = new InlineKeyboardButton("<");
        previousMonthButton.setCallbackData(callbackData);
        return previousMonthButton;
    }

    private static InlineKeyboardButton buildCurrentMonthButton(final LocalDate date) {
        final String monthName = date.getMonth().name();
        final int year = date.getYear();

        final InlineKeyboardButton currentMonthButton = new InlineKeyboardButton(format("%s %s", monthName, year));
        currentMonthButton.setCallbackData("/currentMonth");
        return currentMonthButton;
    }

    private static InlineKeyboardButton buildNextMonthButton(final LocalDate date) {
        final LocalDate now = LocalDate.now();
        final int currentYear = now.getYear();
        final int currentMonth = now.getMonth().getValue();

        final Month month = date.getMonth();
        final int nextMonthValue = month.plus(1).getValue();
        final int year = date.getYear(); // TODO fix problem with year is changed
        final int dayOfMonth = currentYear == year && currentMonth == nextMonthValue
                ? now.getDayOfMonth()
                : 1;

        final String callbackData = format(CHOOSE_MONTH.getPatternTemplate(), "%s", dayOfMonth, nextMonthValue, year);
        final InlineKeyboardButton nextMonthButton = new InlineKeyboardButton(">");
        nextMonthButton.setCallbackData(callbackData);
        return nextMonthButton;
    }

    private static List<InlineKeyboardButton> buildDaysOfWeekButtons() {
        final InlineKeyboardButton mondayButton = new InlineKeyboardButton("Mon");
        mondayButton.setCallbackData("/dayOfWeek");
        final InlineKeyboardButton tuesdayButton = new InlineKeyboardButton("Tue");
        tuesdayButton.setCallbackData("/dayOfWeek");
        final InlineKeyboardButton wednesdayButton = new InlineKeyboardButton("Wed");
        wednesdayButton.setCallbackData("/dayOfWeek");
        final InlineKeyboardButton thursdayButton = new InlineKeyboardButton("Thu");
        thursdayButton.setCallbackData("/dayOfWeek");
        final InlineKeyboardButton fridayButton = new InlineKeyboardButton("Fri");
        fridayButton.setCallbackData("/dayOfWeek");
        final InlineKeyboardButton saturdayButton = new InlineKeyboardButton("Sat");
        saturdayButton.setCallbackData("/dayOfWeek");
        final InlineKeyboardButton sundayButton = new InlineKeyboardButton("Sun");
        sundayButton.setCallbackData("/dayOfWeek");
        return asList(mondayButton, tuesdayButton, wednesdayButton, thursdayButton,
                fridayButton, saturdayButton, sundayButton);
    }

    private static List<List<InlineKeyboardButton>> buildDaysButtons(final LocalDate date) {
        final int lengthOfMonth = date.lengthOfMonth();
        final LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        final int numberOfFirstDayOfMonth = firstDayOfMonth.getDayOfWeek().getValue();
        final int calendarRowsAmount = calculateCalendarRowsAmount(date);
        final InlineKeyboardButton[][] calendar = new InlineKeyboardButton[calendarRowsAmount][CALENDAR_COLUMNS_AMOUNT];

        for (int i = 0; i < calendar.length; i++) {
            for (int j = 0; j < calendar[i].length; j++) {
                final int dayOfWeek = (i * CALENDAR_COLUMNS_AMOUNT) + (j + 1);
                final int dayOfMonth = dayOfWeek - (numberOfFirstDayOfMonth - 1);
                calendar[i][j] = isDayOutOfMonth(dayOfWeek, numberOfFirstDayOfMonth, dayOfMonth, lengthOfMonth)
                        ? buildEmptyButton()
                        : buildDayButton(dayOfMonth, date);
            }
        }
        return convertArrayToList(calendar);
    }

    private static InlineKeyboardButton buildEmptyButton() {
        final InlineKeyboardButton emptyButton = new InlineKeyboardButton(" ");
        emptyButton.setCallbackData("/emptyDay");
        return emptyButton;
    }

    private static InlineKeyboardButton buildDayButton(final int dayOfMonth, final LocalDate date) {
        final int month = date.getMonthValue();
        final int year = date.getYear();
        final InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(dayOfMonth));
        button.setCallbackData(format(CHOOSE_DAY.getPatternTemplate(), "%s", dayOfMonth, month, year));
        return button;
    }

    private static boolean isDayOutOfMonth(
            final int dayOfWeek, final int numberOfFirstDayOfMonth, final int dayOfMonth, final int lengthOfMonth) {
        return isDayBeforeMonthStart(dayOfWeek, numberOfFirstDayOfMonth) || isDayAfterMonthEnd(dayOfMonth, lengthOfMonth);
    }

    private static boolean isDayBeforeMonthStart(final int dayOfWeek, final int numberOfFirstDayOfMonth) {
        return dayOfWeek < numberOfFirstDayOfMonth;
    }

    private static boolean isDayAfterMonthEnd(final int dayOfMonth, final int lengthOfMonth) {
        return dayOfMonth > lengthOfMonth;
    }

    private static List<List<InlineKeyboardButton>> convertArrayToList(final InlineKeyboardButton[][] calendarAsArray) {
        return Arrays.stream(calendarAsArray)
                .map(Arrays::asList)
                .collect(Collectors.toList());
    }

    private static int calculateCalendarRowsAmount(final LocalDate date) {
        return isMonthStartedOnSixDayOfWeekOrLater(date) ? 6 : 5;
    }

    private static boolean isMonthStartedOnSixDayOfWeekOrLater(final LocalDate date) {
        final LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        final int firstDayOfMonthNumber = firstDayOfMonth.getDayOfWeek().getValue();
        return firstDayOfMonthNumber >= 6;
    }
}
