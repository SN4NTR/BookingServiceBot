package com.amiron.booking.bot.command;

import com.amiron.booking.client.facade.ClientFacade;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.SET_EMAIL;
import static com.amiron.booking.bot.util.MessageBuilder.buildSendMessage;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class SetEmailBotCommand extends BotCommand<Message> {

    private final ClientService clientService;
    private final ClientFacade userFacade;

    @Override
    public BotCommandPattern getCommandPattern() {
        return SET_EMAIL;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final Message message) {
        return buildResponseMessage(message);
    }

    private List<SendMessage> buildResponseMessage(final Message message) {
        final Long chatId = message.getChatId();
        final Long userId = message.getFrom().getId();
        final String email = message.getText();
        updateClientEmail(userId, email);

        final SendMessage sendMessage = buildSendMessage(chatId, "Email changed successfully!", null);
        return singletonList(sendMessage);
    }

    private void updateClientEmail(final Long userId, final String email) {
        final Client client = findUserAndSetEmail(userId, email);
        userFacade.onUpdate(client);
    }

    private Client findUserAndSetEmail(final Long userId, final String email) {
        final Client existingClient = clientService.getTelegramId(userId);
        existingClient.setEmail(email);
        return existingClient;
    }
}
