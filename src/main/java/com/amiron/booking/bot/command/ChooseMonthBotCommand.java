package com.amiron.booking.bot.command;

import com.amiron.booking.bot.util.MessageBuilder;
import com.amiron.booking.calendar.service.CalendarService;
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

import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_MONTH;
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

    private final CalendarService calendarService;

    @Override
    public BotCommandPattern getCommandPattern() {
        return CHOOSE_MONTH;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final String callbackData = callbackQuery.getData();
        final String mastersEmail = getEmailFromCommand(callbackData);
        final DateTime from = getDateTimeFromCallbackData(callbackData);
        final List<Event> mastersFreeEvents = getMastersFreeEventsByCallbackData(mastersEmail, callbackQuery);
        return buildResponseToMessage(message, mastersEmail, from, mastersFreeEvents);
    }

    private List<Event> getMastersFreeEventsByCallbackData(final String mastersEmail, final CallbackQuery callbackQuery) {
        final String callbackData = callbackQuery.getData();
        final DateTime from = getDateTimeFromCallbackData(callbackData);
        final DateTime to = addDaysToDateTime(from, 31);
        return calendarService.getFreeUserEvents(mastersEmail, from, to);
    }

    private List<EditMessageText> buildResponseToMessage(
            final Message message, final String mastersEmail, final DateTime from, final List<Event> mastersFreeEvents) {
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final InlineKeyboardMarkup calendarKeyboardMarkup = buildCalendarKeyboardMarkup(mastersEmail, from, mastersFreeEvents);
        final EditMessageText responseMessage = MessageBuilder.buildEditedMessageText(chatId, messageId, "Please choose a day:", calendarKeyboardMarkup);
        return singletonList(responseMessage);
    }

    private InlineKeyboardMarkup buildCalendarKeyboardMarkup(
            final String mastersEmail, final DateTime from, final List<Event> mastersFreeEvents) {
        final LocalDate localDate = getLocalDateFromDateTime(from);
        final InlineKeyboardMarkup calendarKeyboard = buildCalendarKeyboardForDate(localDate);
        updateKeyboardMarkupWithMasterFreeDates(calendarKeyboard, from, mastersFreeEvents);
        updateKeyboardMarkupWithMasterEmail(calendarKeyboard, mastersEmail);
        return calendarKeyboard;
    }

    private DateTime getDateTimeFromCallbackData(final String callbackText) {
        final int dayOfMonth = getDayOfMonthValueFromCommand(callbackText);
        final int month = getMonthValueFromCommand(callbackText);
        final int year = getYearValueFromCommand(callbackText);
        return getDateTimeFromValues(dayOfMonth, month, year);
    }
}
