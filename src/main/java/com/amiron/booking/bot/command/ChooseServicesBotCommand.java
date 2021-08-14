package com.amiron.booking.bot.command;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_NAILS;
import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_SERVICES;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkupWithButtonsFromNewLine;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class ChooseServicesBotCommand extends BotCommand<CallbackQuery> {

    @Override
    public BotCommandPattern getCommandPattern() {
        return CHOOSE_SERVICES;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        return buildResponseToMessage(message);
    }

    private List<EditMessageText> buildResponseToMessage(final Message message) {
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup();
        final EditMessageText responseMessage = buildEditedMessageText(chatId, messageId, "Choose required service:", keyboardMarkup);
        return singletonList(responseMessage);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup() {
        final InlineKeyboardButton bookNailsButton = buildInlineKeyboardButton("Book nails", CHOOSE_NAILS.getPatternTemplate());
        return buildInlineKeyboardMarkupWithButtonsFromNewLine(bookNailsButton);
    }
}
