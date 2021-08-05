package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.model.BotCommand.MENU;
import static com.amiron.booking.bot.util.MessageBuilder.buildMenuMessage;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class MenuCommand extends Command<Message> {

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final Message message) {
        final Long chatId = message.getChatId();
        final SendMessage menuMessage = buildMenuMessage(chatId);
        return singletonList(menuMessage);
    }

    @Override
    public BotCommand getCommand() {
        return MENU;
    }
}
