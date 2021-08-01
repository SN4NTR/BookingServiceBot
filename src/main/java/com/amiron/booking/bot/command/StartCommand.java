package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.util.KeyboardBuilder;
import com.amiron.booking.bot.util.MessageBuilder;
import com.amiron.booking.client.facade.ClientFacade;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.model.converter.ClientConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class StartCommand extends Command<Message> {

    private final ClientFacade userFacade;

    @Override
    public BotApiMethod<?> execute(@NotNull final Message message) {
        final Long chatId = message.getChatId();
        final User telegramUser = message.getFrom();
        final Client client = ClientConverter.fromTelegramUser(chatId, telegramUser);

        userFacade.onCreate(client);

        return buildResponseMessage(chatId);
    }

    @Override
    public BotCommand getCommand() {
        return BotCommand.START;
    }

    private SendMessage buildResponseMessage(final Long chatId) {
        final ReplyKeyboardMarkup keyboardMarkup = KeyboardBuilder.buildMarkupForPhoneNumber();
        return MessageBuilder.buildSendMessage(String.valueOf(chatId), "Please click button to send your mobile number.", keyboardMarkup);
    }
}
