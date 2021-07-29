package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@Validated
@NoArgsConstructor(access = PRIVATE)
public class MessageBuilder {

    public static SendMessage buildSendMessage(
            @NotNull final String chatId,
            @NotNull final String text,
            @Nullable final ReplyKeyboard replyKeyboard) {
        final SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.setReplyMarkup(replyKeyboard);
        return sendMessage;
    }
}
