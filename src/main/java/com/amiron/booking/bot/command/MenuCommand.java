package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.util.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.amiron.booking.bot.model.BotCommand.ACCOUNT;
import static com.amiron.booking.bot.model.BotCommand.BOOKINGS;
import static com.amiron.booking.bot.model.BotCommand.MENU;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkup;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class MenuCommand extends Command<Message> {

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final Message message) {
        final Long chatId = message.getChatId();
        return buildMenuMessage(chatId);
    }

    @Override
    public BotCommand getCommand() {
        return MENU;
    }

    private List<SendMessage> buildMenuMessage(final Long chatId) {
        final String menuText = buildMenuText();
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup();
        final SendMessage sendMessage = MessageBuilder.buildSendMessage(chatId, menuText, keyboardMarkup);
        return singletonList(sendMessage);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup() {
        final InlineKeyboardButton accountButton = buildInlineKeyboardButton("Account", ACCOUNT.getOriginValue());
        final InlineKeyboardButton bookingsButton = buildInlineKeyboardButton("Bookings", BOOKINGS.getOriginValue());
        return buildInlineKeyboardMarkup(accountButton, bookingsButton);
    }

    private String buildMenuText() {
        return "This menu is used to check your account settings and existing bookings. " +
                "Click provided buttons for required actions.\n\n" +
                "<b>Menu</b>\n" +
                "Account - check your account info\n" +
                "Bookings - check your existing bookings\n";
    }
}
