package com.amiron.booking.bot.command;

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
import java.util.List;
import java.util.UUID;

import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_MASTER;
import static com.amiron.booking.bot.util.CommandUtils.getUuidFromCommand;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildCalendarKeyboardMarkupForCurrentMonth;
import static com.amiron.booking.bot.util.MessageBuilder.buildSendMessage;
import static com.amiron.booking.calendar.util.CalendarUtils.addDaysToDateTime;
import static com.amiron.booking.calendar.util.CalendarUtils.getCurrentDateTime;
import static com.amiron.booking.master.util.MasterCalendarUtils.updateKeyboardMarkupWithMasterEmail;
import static com.amiron.booking.master.util.MasterCalendarUtils.updateKeyboardMarkupWithMasterFreeDates;
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
    public BotCommandPattern getCommandPattern() {
        return CHOOSE_MASTER;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final Master master = getMasterFromCallbackData(callbackQuery);
        final List<Event> mastersFreeCalendarEventsForMonth = getMastersFreeCalendarEventsForMonth(master);
        return buildResponseMessage(chatId, master, mastersFreeCalendarEventsForMonth);
    }

    private Master getMasterFromCallbackData(final CallbackQuery callbackQuery) {
        final String callbackText = callbackQuery.getData();
        final UUID masterId = getUuidFromCommand(callbackText);
        return masterService.getById(masterId);
    }

    private List<Event> getMastersFreeCalendarEventsForMonth(final Master master) {
        final String mastersEmail = master.getEmail();
        final DateTime from = getCurrentDateTime();
        final DateTime to = addDaysToDateTime(from, 31);
        return calendarService.getFreeUserEvents(mastersEmail, from, to);
    }

    private List<SendMessage> buildResponseMessage(final Long chatId, final Master master, final List<Event> freeMasterEvents) {
        final InlineKeyboardMarkup calendarKeyboardMarkup = buildMastersCalendarKeyboardMarkup(master, freeMasterEvents);
        final SendMessage message = buildSendMessage(chatId, "Please choose a day:", calendarKeyboardMarkup);
        return singletonList(message);
    }

    private InlineKeyboardMarkup buildMastersCalendarKeyboardMarkup(final Master master, final List<Event> freeMasterEvents) {
        final String mastersEmail = master.getEmail();
        final DateTime from = getCurrentDateTime();

        final InlineKeyboardMarkup calendarKeyboard = buildCalendarKeyboardMarkupForCurrentMonth();
        updateKeyboardMarkupWithMasterFreeDates(calendarKeyboard, from, freeMasterEvents);
        updateKeyboardMarkupWithMasterEmail(calendarKeyboard, mastersEmail);
        return calendarKeyboard;
    }
}
