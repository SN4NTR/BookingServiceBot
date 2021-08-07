package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.amiron.booking.bot.model.UserCommand.ACCOUNT;
import static com.amiron.booking.bot.model.UserCommand.BOOKINGS;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_SERVICES;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkupWithButtonsFromNewLine;
import static lombok.AccessLevel.PRIVATE;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWNV2;

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
        sendMessage.setParseMode(MARKDOWNV2);
        sendMessage.setReplyMarkup(replyKeyboard);
        return sendMessage;
    }

    // TODO real photo
    public static SendPhoto buildSendPhotoMessage(final Long chatId, final byte[] photo, final String caption) {
        final InputStream inputStream = new ByteArrayInputStream(photo);
        final InputFile inputFile = new InputFile("https://as.ftcdn.net/r/v1/pics/dba1341b7e0ef32dd2cd034ed7501df0d4f9ba28/home/discover_collections/21Jun/premium.jpg");

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
        editMessageText.setParseMode(MARKDOWNV2);
        return editMessageText;
    }

    public static SendMessage buildMenuMessage(final Long chatId) {
        final String menuText = buildMenuText();
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup();
        return buildSendMessage(chatId, menuText, keyboardMarkup);
    }

    private static InlineKeyboardMarkup buildKeyboardMarkup() {
        final InlineKeyboardButton accountButton = buildInlineKeyboardButton("Account", ACCOUNT.getOriginValue());
        final InlineKeyboardButton bookServiceButton = buildInlineKeyboardButton("Book Service", CHOOSE_SERVICES.getOriginValue());
        final InlineKeyboardButton bookingsButton = buildInlineKeyboardButton("Bookings", BOOKINGS.getOriginValue());
        return buildInlineKeyboardMarkupWithButtonsFromNewLine(accountButton, bookServiceButton, bookingsButton);
    }

    private static String buildMenuText() {
        return "This menu is used to check your account settings and existing bookings\\. " +
                "Click provided buttons for required actions\\.\n\n" +
                "*Menu*\n" +
                "__Account__ \\- check your account info\n" +
                "__Book Service__ \\- book required service\n" +
                "__Bookings__ \\- check your existing bookings\n";
    }
}
