package com.amiron.booking.bot.command;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.CHANGE_EMAIL;
import static com.amiron.booking.bot.util.MessageBuilder.buildSendMessage;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class ChangeEmailBotCommand extends BotCommand<CallbackQuery> {

    @Override
    public BotCommandPattern getCommandPattern() {
        return CHANGE_EMAIL;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        return buildResponseMessage(chatId);
    }

    private List<SendMessage> buildResponseMessage(final Long chatId) {
        final SendMessage sendMessage = buildSendMessage(chatId, "Please enter your email:", null);
        return singletonList(sendMessage);
    }
}
