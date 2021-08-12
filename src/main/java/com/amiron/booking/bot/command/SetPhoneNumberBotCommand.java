package com.amiron.booking.bot.command;

import com.amiron.booking.client.facade.ClientFacade;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.SET_PHONE_NUMBER;
import static com.amiron.booking.bot.util.MessageBuilder.buildSendMessage;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class SetPhoneNumberBotCommand extends BotCommand<Contact> {

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
    public BotCommandPattern getCommandPattern() {
        return SET_PHONE_NUMBER;
    }

    private Client setPhoneNumberForExistingUser(final Long userId, final String phoneNumber) {
        final Client existingClient = clientService.getTelegramId(userId);
        existingClient.setPhoneNumber(phoneNumber);
        return existingClient;
    }

    private List<SendMessage> buildResponseMessage(final Long chatId) {
        final SendMessage sendMessage = buildSendMessage(chatId, "Phone number is changed successfully!", null);
        return singletonList(sendMessage);
    }
}
