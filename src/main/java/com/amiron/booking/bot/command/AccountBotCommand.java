package com.amiron.booking.bot.command;

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
import java.util.Optional;

import static com.amiron.booking.bot.command.BotCommandPattern.ACCOUNT;
import static com.amiron.booking.bot.command.BotCommandPattern.CHANGE_EMAIL;
import static com.amiron.booking.bot.command.BotCommandPattern.CHANGE_PHONE_NUMBER;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkupWithButtonsFromNewLine;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class AccountBotCommand extends BotCommand<CallbackQuery> {

    private final ClientService clientService;

    @Override
    public BotCommandPattern getCommandPattern() {
        return ACCOUNT;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Client client = getClientByCallbackData(callbackQuery);
        final String accountText = buildAccountInfoText(client);
        return buildResponseToMessage(message, accountText);
    }

    private Client getClientByCallbackData(final CallbackQuery callbackQuery) {
        final String userName = callbackQuery.getFrom().getUserName();
        return clientService.getByUsername(userName);
    }

    private List<EditMessageText> buildResponseToMessage(final Message message, final String accountText) {
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();

        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup();
        final EditMessageText response = buildEditedMessageText(chatId, messageId, accountText, keyboardMarkup);
        return singletonList(response);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup() {
        final InlineKeyboardButton accountButton = buildInlineKeyboardButton("Change phone number", CHANGE_PHONE_NUMBER.getPatternTemplate());
        final InlineKeyboardButton bookingsButton = buildInlineKeyboardButton("Change email", CHANGE_EMAIL.getPatternTemplate());
        return buildInlineKeyboardMarkupWithButtonsFromNewLine(accountButton, bookingsButton);
    }

    private String buildAccountInfoText(final Client client) {
        final String name = buildNameValue(client);
        final String email = buildEmailValue(client);
        final String phoneNumber = buildPhoneNumberValue(client);
        return format("""
                        <b>Account</b>
                                                
                        Name: %s
                        Email: %s
                        Phone Number: %s
                        """,
                name, email, phoneNumber
        );
    }

    private String buildNameValue(final Client client) {
        final String firstName = client.getFirstName();
        final String lastName = Optional.ofNullable(client.getLastName()).orElse(EMPTY);
        return format("%s %s", firstName, lastName);
    }

    private String buildEmailValue(final Client client) {
        return Optional.ofNullable(client.getEmail()).orElse("Not set");
    }

    private String buildPhoneNumberValue(final Client client) {
        final String phoneNumber = client.getPhoneNumber();
        return nonNull(phoneNumber) ? "+" + phoneNumber : "Not set";
    }
}
