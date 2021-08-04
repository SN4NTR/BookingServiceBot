package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.util.BotCalendarBuilder;
import com.amiron.booking.calendar.service.CalendarService;
import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.service.MasterService;
import com.google.api.services.calendar.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static com.amiron.booking.bot.model.BotCommand.BOOK_MASTER;
import static com.amiron.booking.bot.util.CommandUtils.getUuidFromCommand;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class BookMasterCommand extends Command<CallbackQuery> {

    private final MasterService masterService;
    private final CalendarService calendarService;

    // TODO integrate with calendar
    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final String callbackText = callbackQuery.getData();
        final String masterId = getUuidFromCommand(callbackText);

        final Master master = masterService.getById(UUID.fromString(masterId));
        final String masterEmail = master.getEmail();

        final List<Event> userEvents = calendarService.getUserEvents(masterEmail);

        final SendMessage sendMessage = BotCalendarBuilder.buildForCurrentMonth();
        sendMessage.setText("Days:");
        sendMessage.setChatId(String.valueOf(chatId));

        return singletonList(sendMessage);
    }

    @Override
    public BotCommand getCommand() {
        return BOOK_MASTER;
    }
}
