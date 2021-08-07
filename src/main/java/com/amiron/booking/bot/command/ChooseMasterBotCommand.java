package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
import com.amiron.booking.calendar.service.CalendarService;
import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.service.MasterService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.amiron.booking.bot.model.UserCommand.CHOOSE_MASTER;
import static com.amiron.booking.bot.util.CommandUtils.getUuidFromCommand;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildCalendarKeyboardMarkupForCurrentMonth;
import static com.amiron.booking.master.util.MasterCalendarUtils.updateKeyboardMarkupWithFreeDates;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ChooseMasterBotCommand extends BotCommand<CallbackQuery> {

    private final MasterService masterService;
    private final CalendarService calendarService;

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final String callbackText = callbackQuery.getData();
        final UUID masterId = getUuidFromCommand(callbackText);

        final Master master = masterService.getById(masterId);
        final String masterEmail = master.getEmail();

        return buildResponseMessage(chatId, masterEmail);
    }

    @Override
    public UserCommand getCommand() {
        return CHOOSE_MASTER;
    }

    private List<SendMessage> buildResponseMessage(final Long chatId, final String masterEmail) {
        final InlineKeyboardMarkup calendarKeyboardMarkup = buildCalendarKeyboardMarkup(masterEmail);
        final SendMessage message = new SendMessage();
        message.setReplyMarkup(calendarKeyboardMarkup);
        message.setText("Please choose a day:");
        message.setChatId(String.valueOf(chatId));
        return singletonList(message);
    }

    private InlineKeyboardMarkup buildCalendarKeyboardMarkup(final String masterEmail) {
        final DateTime from = new DateTime(Instant.now().toEpochMilli());
        final DateTime to = new DateTime(Instant.now().plus(31, DAYS).toEpochMilli());
        final List<Event> freeMasterBookings = calendarService.getFreeUserEvents(masterEmail, from, to);

        final InlineKeyboardMarkup calendarKeyboard = buildCalendarKeyboardMarkupForCurrentMonth();
        updateKeyboardMarkupWithFreeDates(calendarKeyboard, freeMasterBookings);
        return calendarKeyboard;
    }
}