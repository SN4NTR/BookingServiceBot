package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.util.KeyboardBuilder;
import com.amiron.booking.bot.util.MessageBuilder;
import com.amiron.booking.client.facade.ClientFacade;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.model.BotCommand.SET_PHONE_NUMBER;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class SetPhoneNumberCommand extends Command<Contact> {

    private final ClientService clientService;
    private final ClientFacade clientFacade;

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final Contact contact) {
        final Long userId = contact.getUserId();
        final String phoneNumber = contact.getPhoneNumber();

        final Client client = setPhoneNumberForExistingUser(userId, phoneNumber);
        clientFacade.onUpdate(client);

        final Long chatId = client.getChatId();
        return buildResponseMessage(chatId);
    }

    @Override
    public BotCommand getCommand() {
        return SET_PHONE_NUMBER;
    }

    private Client setPhoneNumberForExistingUser(final Long userId, final String phoneNumber) {
        final Client existingClient = clientService.getTelegramId(userId);
        existingClient.setPhoneNumber(phoneNumber);
        return existingClient;
    }

    private List<SendMessage> buildResponseMessage(final Long chatId) {
        final ReplyKeyboardRemove keyboardRemove = KeyboardBuilder.buildKeyboardRemove();
        final SendMessage sendMessage = MessageBuilder.buildSendMessage(chatId, "Please enter your email.", keyboardRemove);
        return singletonList(sendMessage);
    }
}
