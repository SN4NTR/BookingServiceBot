package com.amiron.booking.user.model.converter;

import com.amiron.booking.user.model.User;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class UserConverter {

    public static User fromTelegramUser(final Long chatId, final org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        final User user = new User();
        user.setId(telegramUser.getId());
        user.setUsername(telegramUser.getUserName());
        user.setFirstName(telegramUser.getFirstName());
        user.setLastName(telegramUser.getLastName());
        user.setChatId(chatId);
        return user;
    }
}
