package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.util.MessageBuilder;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.model.BotCommand.ACCOUNT;
import static com.amiron.booking.bot.model.BotCommand.SET_EMAIL;
import static com.amiron.booking.bot.model.BotCommand.SET_PHONE_NUMBER;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkup;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class AccountCommand extends Command<CallbackQuery> {

    private final ClientService clientService;

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Long chatId = message.getChatId();
        final String userName = callbackQuery.getFrom().getUserName();
        final Client client = clientService.getByUsername(userName);
        return buildAccountInfoMessage(chatId, client);
    }

    @Override
    public BotCommand getCommand() {
        return ACCOUNT;
    }

    private List<SendMessage> buildAccountInfoMessage(final Long chatId, final Client client) {
        final String accountText = buildAccountText(client);
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup();
        final SendMessage sendMessage = MessageBuilder.buildSendMessage(chatId, accountText, keyboardMarkup);
        return singletonList(sendMessage);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup() {
        final InlineKeyboardButton accountButton = buildInlineKeyboardButton("Change phone number", SET_PHONE_NUMBER.getOriginValue());
        final InlineKeyboardButton bookingsButton = buildInlineKeyboardButton("Change email", SET_EMAIL.getOriginValue());
        return buildInlineKeyboardMarkup(accountButton, bookingsButton);
    }

    // TODO replace with message builder
    private String buildAccountText(final Client client) {
        return format("<b>Account</b>\n" +
                        "First Name: %s\n" +
                        "Last Name: %s\n" +
                        "Email: %s\n" +
                        "Phone Number: +%s",
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber()
        );
    }
}
