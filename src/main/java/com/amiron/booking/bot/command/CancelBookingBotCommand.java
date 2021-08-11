package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
import com.amiron.booking.bot.util.CommandUtils;
import com.amiron.booking.servicebooking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static com.amiron.booking.bot.model.UserCommand.CANCEL_BOOKING;
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

    // TODO update calendar info
    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final String data = callbackQuery.getData();
        final String bookingId = CommandUtils.getUuidFromCommandAsString(data);
        bookingService.delete(UUID.fromString(bookingId));
        return buildResponseMessage(callbackQuery);
    }

    @Override
    public UserCommand getResponsibleForUserCommand() {
        return CANCEL_BOOKING;
    }

    private List<EditMessageText> buildResponseMessage(final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final Integer messageId = callbackQuery.getMessage().getMessageId();
        final EditMessageText message = buildEditedMessageText(chatId, messageId, "Booking canceled successfully!", null);
        return singletonList(message);
    }
}
