package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
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
        keyboardMarkup.setOneTimeKeyboard(true);
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

    public static InlineKeyboardMarkup buildInlineKeyboardMarkupWithButtonsFromNewLine(final InlineKeyboardButton... buttons) {
        final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> inlineButtons = Arrays.stream(buttons)
                .map(Arrays::asList)
                .collect(Collectors.toList());
        keyboardMarkup.setKeyboard(inlineButtons);
        return keyboardMarkup;
    }

    public static InlineKeyboardButton buildInlineKeyboardButton(final String text, final String callback) {
        final InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText(text);
        keyboardButton.setCallbackData(callback);
        return keyboardButton;
    }

    public static InlineKeyboardMarkup buildInlineKeyboardMarkupWithSequentialButtons(final InlineKeyboardButton... buttons) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(asList(buttons))
                .build();
    }
}
