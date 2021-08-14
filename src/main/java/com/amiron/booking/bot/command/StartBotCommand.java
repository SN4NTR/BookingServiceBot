package com.amiron.booking.bot.command;

import com.amiron.booking.client.facade.ClientFacade;
import com.amiron.booking.client.model.Client;
import com.amiron.booking.client.model.converter.ClientConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.START;
import static com.amiron.booking.bot.util.MessageBuilder.buildMenuMessage;
import static com.amiron.booking.bot.util.MessageBuilder.buildSendMessage;
import static java.util.Arrays.asList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class StartBotCommand extends BotCommand<Message> {

    private final ClientFacade userFacade;

    @Override
    public BotCommandPattern getCommandPattern() {
        return START;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final Message message) {
        return buildResponseMessage(message);
    }

    private List<SendMessage> buildResponseMessage(final Message message) {
        final Long chatId = message.getChatId();
        final User telegramUser = message.getFrom();
        createUser(chatId, telegramUser);

        final SendMessage welcomeMessage = buildSendMessage(chatId, "Welcome to Booking Bot!", null);
        final SendMessage menuMessage = buildMenuMessage(chatId);
        return asList(welcomeMessage, menuMessage);
    }

    private void createUser(final Long chatId, final User telegramUser) {
        final Client client = ClientConverter.fromTelegramUser(chatId, telegramUser);
        userFacade.onCreate(client);
    }
}
