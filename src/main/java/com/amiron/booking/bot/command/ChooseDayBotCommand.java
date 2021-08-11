package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
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
import java.util.List;

import static com.amiron.booking.bot.model.UserCommand.CHOOSE_DAY;
import static com.amiron.booking.bot.util.CommandUtils.getDayOfMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getEmailFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getYearValueFromCommand;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildCalendarKeyboardMarkupForDay;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static com.amiron.booking.calendar.util.CalendarUtils.getDateTimeFromValues;
import static com.amiron.booking.master.util.MasterCalendarUtils.updateKeyboardMarkupWithMasterEmail;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ChooseDayBotCommand extends BotCommand<CallbackQuery> {

    private final CalendarService calendarService;

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final String callbackText = callbackQuery.getData();
        final DateTime dateTime = getDateTimeFromCallbackData(callbackText);
        final String masterEmail = getEmailFromCommand(callbackText);

        final List<Event> mastersDayBookings = calendarService.getFreeUserDayEvents(masterEmail, dateTime);

        return buildResponseMessage(chatId, messageId, masterEmail, mastersDayBookings);
    }

    @Override
    public UserCommand getResponsibleForUserCommand() {
        return CHOOSE_DAY;
    }

    private DateTime getDateTimeFromCallbackData(final String callbackText) {
        final int dayOfMonth = getDayOfMonthValueFromCommand(callbackText);
        final int month = getMonthValueFromCommand(callbackText);
        final int year = getYearValueFromCommand(callbackText);
        return getDateTimeFromValues(dayOfMonth, month, year);
    }

    private List<EditMessageText> buildResponseMessage
            (final Long chatId, final Integer messageId, final String masterEmail, final List<Event> mastersDayBookings) {
        final InlineKeyboardMarkup calendarKeyboardMarkup = buildCalendarKeyboardMarkup(masterEmail, mastersDayBookings);
        final EditMessageText message = buildEditedMessageText(chatId, messageId, "Please choose time:", calendarKeyboardMarkup);
        return singletonList(message);
    }

    private InlineKeyboardMarkup buildCalendarKeyboardMarkup(final String masterEmail, final List<Event> mastersDayBookings) {
        final InlineKeyboardMarkup keyboardMarkup = buildCalendarKeyboardMarkupForDay(mastersDayBookings);
        updateKeyboardMarkupWithMasterEmail(keyboardMarkup, masterEmail);
        return keyboardMarkup;
    }
}
