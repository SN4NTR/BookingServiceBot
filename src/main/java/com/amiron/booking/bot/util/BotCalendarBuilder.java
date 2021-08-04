package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class BotCalendarBuilder {

    private static final int CALENDAR_COLUMNS_AMOUNT = 7;

    public static SendMessage buildForCurrentMonth() {
        final LocalDate now = LocalDate.now();
        final int year = now.getYear();
        final int month = now.getMonthValue();
        return buildForDate(year, month);
    }

    public static SendMessage buildForDate(final LocalDate date) {
        final SendMessage sendMessage = new SendMessage();
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup(date);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public static SendMessage buildForDate(final int year, final int month) {
        final LocalDate date = LocalDate.of(year, month, 1);
        return buildForDate(date);
    }

    private static InlineKeyboardMarkup buildKeyboardMarkup(final LocalDate date) {
        final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> buttonsLines = buildKeyboardButtonsLines(date);
        keyboardMarkup.setKeyboard(buttonsLines);
        return keyboardMarkup;
    }

    private static List<List<InlineKeyboardButton>> buildKeyboardButtonsLines(final LocalDate date) {
        final List<InlineKeyboardButton> monthNavigationButtons = buildMonthNavigationButtons(date);
        final List<InlineKeyboardButton> daysOfWeekButtons = buildDaysOfWeekButtons();
        final List<List<InlineKeyboardButton>> buttons = buildDaysButtons(date);
        buttons.add(0, monthNavigationButtons);
        buttons.add(1, daysOfWeekButtons);
        return buttons;
    }

    private static List<InlineKeyboardButton> buildMonthNavigationButtons(final LocalDate date) {
        final String monthName = date.getMonth().name();
        final int year = date.getYear();

        final InlineKeyboardButton previousButton = new InlineKeyboardButton("<");
        previousButton.setCallbackData("/previous");
        final InlineKeyboardButton dateButton = new InlineKeyboardButton(format("%s %s", monthName, year));
        dateButton.setCallbackData("/next");
        final InlineKeyboardButton nextButton = new InlineKeyboardButton(">");
        nextButton.setCallbackData("/next");

        return asList(previousButton, dateButton, nextButton);
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
                        : buildDayButton(dayOfMonth);
            }
        }
        return convertArrayToList(calendar);
    }

    private static InlineKeyboardButton buildEmptyButton() {
        final InlineKeyboardButton emptyButton = new InlineKeyboardButton(" ");
        emptyButton.setCallbackData("/emptyDay");
        return emptyButton;
    }

    private static InlineKeyboardButton buildDayButton(final int dayOfMonth) {
        final InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(dayOfMonth));
        button.setCallbackData("/bookMaster/{id}/day/" + dayOfMonth);
        return button;
    }

    private static boolean isDayOutOfMonth(final int dayOfWeek, final int numberOfFirstDayOfMonth, final int dayOfMonth, final int lengthOfMonth) {
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
