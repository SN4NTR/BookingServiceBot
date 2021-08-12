package com.amiron.booking.bot.command;

import com.amiron.booking.bot.util.KeyboardBuilder;
import com.amiron.booking.bot.util.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.CHANGE_PHONE_NUMBER;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class ChangePhoneNumberBotCommand extends BotCommand<CallbackQuery> {

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        return buildSendPhoneNumberMessage(chatId);
    }

    @Override
    public BotCommandPattern getCommandPattern() {
        return CHANGE_PHONE_NUMBER;
    }

    private List<SendMessage> buildSendPhoneNumberMessage(final Long chatId) {
        final ReplyKeyboardMarkup keyboardMarkup = KeyboardBuilder.buildMarkupForPhoneNumber();
        final SendMessage sendMessage = MessageBuilder.buildSendMessage(chatId, "Please click button to send your phone number.", keyboardMarkup);
        return singletonList(sendMessage);
    }
}
