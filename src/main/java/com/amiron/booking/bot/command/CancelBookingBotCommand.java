package com.amiron.booking.bot.command;

import com.amiron.booking.servicebooking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static com.amiron.booking.bot.command.BotCommandPattern.CANCEL_BOOKING;
import static com.amiron.booking.bot.util.CommandUtils.getUuidFromCommand;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class CancelBookingBotCommand extends BotCommand<CallbackQuery> {

    private final BookingService bookingService;

    @Override
    public BotCommandPattern getCommandPattern() {
        return CANCEL_BOOKING;
    }

    // TODO update calendar info
    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final String callbackData = callbackQuery.getData();
        final UUID bookingId = getUuidFromCommand(callbackData);
        bookingService.delete(bookingId);
        return buildResponseToMessage(message);
    }

    private List<EditMessageText> buildResponseToMessage(final Message message) {
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final EditMessageText responseMessage = buildEditedMessageText(chatId, messageId, "Booking canceled successfully!", null);
        return singletonList(responseMessage);
    }
}
