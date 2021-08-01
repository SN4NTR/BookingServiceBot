package com.amiron.booking.client.model.converter;

import com.amiron.booking.client.model.Client;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.User;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class ClientConverter {

    public static Client fromTelegramUser(final Long chatId, final User telegramUser) {
        final Client client = new Client();
        client.setId(telegramUser.getId());
        client.setUsername(telegramUser.getUserName());
        client.setFirstName(telegramUser.getFirstName());
        client.setLastName(telegramUser.getLastName());
        client.setChatId(chatId);
        return client;
    }
}
