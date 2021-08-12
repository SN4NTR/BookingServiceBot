package com.amiron.booking.bot.command;

import com.amiron.booking.calendar.service.CalendarService;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.service.ClientService;
import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.service.MasterService;
import com.amiron.booking.servicebooking.model.Booking;
import com.amiron.booking.servicebooking.service.BookingService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.BOOK_TIME;
import static com.amiron.booking.bot.util.CommandUtils.getDayOfMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getEmailFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getHoursValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getMinutesValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getYearValueFromCommand;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static com.amiron.booking.calendar.util.CalendarUtils.getDateTimeFromLocalDateTime;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class BookTimeBotCommand extends BotCommand<CallbackQuery> {

    private final MasterService masterService;
    private final ClientService clientService;
    private final BookingService bookingService;
    private final CalendarService calendarService;

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Booking booking = saveBooking(callbackQuery);
        updateCalendarEvent(booking);
        return buildResponseMessage(message);
    }

    @Override
    public BotCommandPattern getCommandPattern() {
        return BOOK_TIME;
    }

    private void updateCalendarEvent(final Booking booking) {
        final String clientEmail = booking.getClient().getEmail();
        final String masterEmail = booking.getMaster().getEmail();
        final LocalDateTime localDateTime = booking.getDateTime();
        final DateTime dateTime = getDateTimeFromLocalDateTime(localDateTime);
        final Event bookingEvent = calendarService.getByStartDateTime(masterEmail, dateTime);
        calendarService.addGuestsToEvent(masterEmail, bookingEvent, asList(masterEmail, clientEmail));
    }

    private List<EditMessageText> buildResponseMessage(final Message message) {
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final EditMessageText editMessage = buildEditedMessageText(chatId, messageId, "You successfully booked service!", null);
        return singletonList(editMessage);
    }

    private Booking saveBooking(final CallbackQuery callbackQuery) {
        final Booking booking = buildBooking(callbackQuery);
        return bookingService.save(booking);
    }

    private Booking buildBooking(final CallbackQuery callbackQuery) {
        final String callbackText = callbackQuery.getData();
        final String userName = callbackQuery.getFrom().getUserName();
        final String mastersEmail = getEmailFromCommand(callbackText);
        final Master master = masterService.getByEmail(mastersEmail);
        final Client client = clientService.getByUsername(userName);
        final LocalDateTime localDateTime = buildLocalDateTime(callbackText);

        final Booking booking = new Booking();
        booking.setClient(client);
        booking.setMaster(master);
        booking.setDateTime(localDateTime);
        return booking;
    }

    private LocalDateTime buildLocalDateTime(final String callbackText) {
        final int dayOfMonth = getDayOfMonthValueFromCommand(callbackText);
        final int month = getMonthValueFromCommand(callbackText);
        final int year = getYearValueFromCommand(callbackText);
        final int hours = getHoursValueFromCommand(callbackText);
        final int minutes = getMinutesValueFromCommand(callbackText);
        return LocalDateTime.of(year, Month.of(month), dayOfMonth, hours, minutes);
    }
}
