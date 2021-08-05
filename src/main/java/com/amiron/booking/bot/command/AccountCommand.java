package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.model.BotCommand.ACCOUNT;
import static com.amiron.booking.bot.model.BotCommand.CHANGE_EMAIL;
import static com.amiron.booking.bot.model.BotCommand.CHANGE_PHONE_NUMBER;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkupWithButtonsFromNewLine;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
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
        final Integer messageId = message.getMessageId();
        final Long chatId = message.getChatId();
        final String userName = callbackQuery.getFrom().getUserName();
        final Client client = clientService.getByUsername(userName);
        return buildResponseMessage(chatId, messageId, client);
    }

    @Override
    public BotCommand getCommand() {
        return ACCOUNT;
    }

    private List<EditMessageText> buildResponseMessage(final Long chatId, final Integer messageId, final Client client) {
        final String accountText = buildAccountText(client);
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup();
        final EditMessageText message = buildEditedMessageText(chatId, messageId, accountText, keyboardMarkup);
        return singletonList(message);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup() {
        final InlineKeyboardButton accountButton = buildInlineKeyboardButton("Change phone number", CHANGE_PHONE_NUMBER.getOriginValue());
        final InlineKeyboardButton bookingsButton = buildInlineKeyboardButton("Change email", CHANGE_EMAIL.getOriginValue());
        return buildInlineKeyboardMarkupWithButtonsFromNewLine(accountButton, bookingsButton);
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
