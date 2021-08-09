package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
import com.amiron.booking.calendar.service.CalendarService;
import com.amiron.booking.master.service.MasterService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static com.amiron.booking.bot.model.UserCommand.CHOOSE_MONTH;
import static com.amiron.booking.bot.util.CommandUtils.getDayOfMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getEmailFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getYearValueFromCommand;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildCalendarKeyboardForDate;
import static com.amiron.booking.calendar.util.CalendarUtils.addDaysToDateTime;
import static com.amiron.booking.calendar.util.CalendarUtils.getDateTimeFromValues;
import static com.amiron.booking.calendar.util.CalendarUtils.getLocalDateFromDateTime;
import static com.amiron.booking.master.util.MasterCalendarUtils.updateKeyboardMarkupWithMasterEmail;
import static com.amiron.booking.master.util.MasterCalendarUtils.updateKeyboardMarkupWithMasterFreeDates;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ChooseMonthBotCommand extends BotCommand<CallbackQuery> {

    private final MasterService masterService;
    private final CalendarService calendarService;

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final String callbackText = callbackQuery.getData();
        final String mastersEmail = getEmailFromCommand(callbackText);
        return buildResponseMessage(chatId, messageId, callbackText, mastersEmail);
    }

    @Override
    public UserCommand getCommand() {
        return CHOOSE_MONTH;
    }

    private List<EditMessageText> buildResponseMessage(
            final Long chatId, final Integer messageId, final String callbackText, final String masterEmail) {
        final InlineKeyboardMarkup calendarKeyboardMarkup = buildCalendarKeyboardMarkup(callbackText, masterEmail);
        final EditMessageText message = new EditMessageText();
        message.setMessageId(messageId);
        message.setReplyMarkup(calendarKeyboardMarkup);
        message.setText("Please choose a day:");
        message.setChatId(String.valueOf(chatId));
        return singletonList(message);
    }

    private InlineKeyboardMarkup buildCalendarKeyboardMarkup(final String callbackText, final String masterEmail) {
        final DateTime from = getDateTimeFromCallbackData(callbackText);
        final DateTime to = addDaysToDateTime(from, 31);
        final List<Event> mastersBookings = calendarService.getFreeUserEvents(masterEmail, from, to);

        final LocalDate localDate = getLocalDateFromDateTime(from);
        final InlineKeyboardMarkup calendarKeyboard = buildCalendarKeyboardForDate(localDate);
        updateKeyboardMarkupWithMasterFreeDates(calendarKeyboard, from, mastersBookings);
        updateKeyboardMarkupWithMasterEmail(calendarKeyboard, masterEmail);
        return calendarKeyboard;
    }

    private DateTime getDateTimeFromCallbackData(final String callbackText) {
        final int dayOfMonth = getDayOfMonthValueFromCommand(callbackText);
        final int month = getMonthValueFromCommand(callbackText);
        final int year = getYearValueFromCommand(callbackText);
        return getDateTimeFromValues(dayOfMonth, month, year);
    }
}
