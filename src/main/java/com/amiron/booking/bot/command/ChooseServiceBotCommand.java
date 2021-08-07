package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.model.UserCommand.CHOOSE_NAILS;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_SERVICES;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkupWithButtonsFromNewLine;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class ChooseServiceBotCommand extends BotCommand<CallbackQuery> {

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final Integer messageId = callbackQuery.getMessage().getMessageId();
        return buildResponseMessage(chatId, messageId);
    }

    @Override
    public UserCommand getCommand() {
        return CHOOSE_SERVICES;
    }

    private List<EditMessageText> buildResponseMessage(final Long chatId, final Integer messageId) {
        final String accountText = "Choose required service:";
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup();
        final EditMessageText message = buildEditedMessageText(chatId, messageId, accountText, keyboardMarkup);
        return singletonList(message);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup() {
        final InlineKeyboardButton bookNailsButton = buildInlineKeyboardButton("Book nails", CHOOSE_NAILS.getOriginValue());
        return buildInlineKeyboardMarkupWithButtonsFromNewLine(bookNailsButton);
    }
}
