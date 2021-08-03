package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static java.util.Collections.singletonList;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class KeyboardBuilder {

    public static ReplyKeyboardMarkup buildMarkupForPhoneNumber() {
        final KeyboardButton sendPhoneNumberButton = new KeyboardButton("Send phone number");
        sendPhoneNumberButton.setRequestContact(true);
        final KeyboardRow firstKeyboardRow = new KeyboardRow(singletonList(sendPhoneNumberButton));
        final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(singletonList(firstKeyboardRow));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboardRemove buildKeyboardRemove() {
        return new ReplyKeyboardRemove(true);
    }

    public static InlineKeyboardMarkup buildInlineKeyboardMarkup(final String text, final String callback) {
        final InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText(text);
        keyboardButton.setCallbackData(callback);
        return InlineKeyboardMarkup.builder()
                .keyboardRow(singletonList(keyboardButton))
                .build();
    }
}
