package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.ByteArrayInputStream;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class MessageBuilder {

    public static SendMessage buildSendMessage(
            final Long chatId,
            final String text,
            final ReplyKeyboard replyKeyboard) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(replyKeyboard);
        return sendMessage;
    }

    public static SendPhoto buildSendPhotoMessage(final Long chatId, final byte[] photo, final String caption) {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
        final InputFile inputFile = new InputFile();

        final SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption(caption);
        return sendPhoto;
    }

    public static EditMessageText buildEditedMessageText(
            final Long chatId,
            final Integer messageId,
            final String text,
            final InlineKeyboardMarkup inlineKeyboardMarkup) {
        final EditMessageText editMessageText = new EditMessageText(text);
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageText;
    }
}
