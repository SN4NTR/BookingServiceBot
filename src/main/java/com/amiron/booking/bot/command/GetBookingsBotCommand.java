package com.amiron.booking.bot.command;

import com.amiron.booking.master.model.Master;
import com.amiron.booking.servicebooking.model.Booking;
import com.amiron.booking.servicebooking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.amiron.booking.bot.command.BotCommandPattern.CANCEL_BOOKING;
import static com.amiron.booking.bot.command.BotCommandPattern.GET_BOOKINGS;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkupWithButtonsFromNewLine;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static com.amiron.booking.bot.util.MessageBuilder.buildSendMessage;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class GetBookingsBotCommand extends BotCommand<CallbackQuery> {

    private final BookingService bookingService;

    @Override
    public BotCommandPattern getCommandPattern() {
        return GET_BOOKINGS;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        return buildResponseMessage(callbackQuery);
    }

    private List<SendMessage> buildResponseMessage(final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final String username = callbackQuery.getFrom().getUserName();
        final List<Booking> userBookings = bookingService.getAllByClientUsername(username);

        final EditMessageText headerMessage = buildHeaderMessage(chatId, messageId);
        final List messages = buildBookingsInfoMessages(chatId, userBookings);
        messages.add(0, headerMessage);
        return messages;
    }

    private List<SendMessage> buildBookingsInfoMessages(final Long chatId, final List<Booking> userBookings) {
        return userBookings.stream()
                .map(booking -> buildBookingInfoMessage(chatId, booking))
                .collect(toList());
    }

    private SendMessage buildBookingInfoMessage(final Long chatId, final Booking booking) {
        final String text = buildTextByBookingInfo(booking);
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup(booking);
        return buildSendMessage(chatId, text, keyboardMarkup);
    }

    private EditMessageText buildHeaderMessage(final Long chatId, final Integer messageId) {
        final String header = "Active bookings:";
        return buildEditedMessageText(chatId, messageId, header, null);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup(final Booking booking) {
        final UUID bookingId = booking.getId();
        final InlineKeyboardButton cancelButton = buildInlineKeyboardButton("Cancel", format(CANCEL_BOOKING.getPatternTemplate(), bookingId));
        return buildInlineKeyboardMarkupWithButtonsFromNewLine(cancelButton);
    }

    private String buildTextByBookingInfo(final Booking booking) {
        final Master master = booking.getMaster();
        final String firstName = master.getFirstName();
        final String lastName = master.getLastName();
        final LocalDateTime dateTime = booking.getDateTime();
        final int dayOfMonth = dateTime.getDayOfMonth();
        final int month = dateTime.getMonthValue();
        final int year = dateTime.getYear();
        final int hour = dateTime.getHour();
        final int minute = dateTime.getMinute();
        return format("""
                Master: %s %s
                Date: %s.%s.%s
                Time: %s:%s
                """, firstName, lastName, dayOfMonth, month, year, hour, minute);
    }
}
