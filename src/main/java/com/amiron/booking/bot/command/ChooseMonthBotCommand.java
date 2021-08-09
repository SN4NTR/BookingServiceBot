package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
import com.amiron.booking.calendar.service.CalendarService;
import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.service.MasterService;
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
import java.util.UUID;

import static com.amiron.booking.bot.model.UserCommand.CHOOSE_MONTH;
import static com.amiron.booking.bot.util.CommandUtils.getUuidFromCommand;
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

    // TODO logic
    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final String callbackText = callbackQuery.getData();
        final UUID masterId = getUuidFromCommand(callbackText);

        final Master master = masterService.getById(masterId);
        final String masterEmail = master.getEmail();

        return buildResponseMessage(chatId, messageId, masterEmail);
    }

    @Override
    public UserCommand getCommand() {
        return CHOOSE_MONTH;
    }

    private List<EditMessageText> buildResponseMessage(
            final Long chatId, final Integer messageId, final String masterEmail) {
        final InlineKeyboardMarkup calendarKeyboardMarkup = buildCalendarKeyboardMarkup(masterEmail);
        final EditMessageText message = new EditMessageText();
        message.setMessageId(messageId);
        message.setReplyMarkup(calendarKeyboardMarkup);
        message.setText("Please choose a day:");
        message.setChatId(String.valueOf(chatId));
        return singletonList(message);
    }

    private InlineKeyboardMarkup buildCalendarKeyboardMarkup(final String masterEmail) {

//        final List<Event> freeMasterBookings = calendarService.getFreeUserEvents(masterEmail, from, to);

//        final InlineKeyboardMarkup calendarKeyboard = buildCalendarKeyboardMarkupForCurrentMonth();
//        updateKeyboardMarkupWithFreeDates(calendarKeyboard, freeMasterBookings);
//        return calendarKeyboard;
        return null;
    }
}
