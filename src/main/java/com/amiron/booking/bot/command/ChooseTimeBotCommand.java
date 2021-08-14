package com.amiron.booking.bot.command;

import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.service.MasterService;
import lombok.AllArgsConstructor;
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

import static com.amiron.booking.bot.command.BotCommandPattern.BOOK_TIME;
import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_TIME;
import static com.amiron.booking.bot.command.BotCommandPattern.MENU;
import static com.amiron.booking.bot.util.CommandUtils.getDayOfMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getEmailFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getHoursValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getMinutesValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getMonthValueFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.getYearValueFromCommand;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardButton;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkupWithSequentialButtons;
import static com.amiron.booking.bot.util.MessageBuilder.buildEditedMessageText;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ChooseTimeBotCommand extends BotCommand<CallbackQuery> {

    private final MasterService masterService;

    @Override
    public BotCommandPattern getCommandPattern() {
        return CHOOSE_TIME;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        return buildMessage(message, callbackQuery);
    }

    private List<EditMessageText> buildMessage(final Message message, final CallbackQuery callbackQuery) {
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final String callbackData = callbackQuery.getData();
        final String mastersEmail = getEmailFromCommand(callbackData);
        final Master master = masterService.getByEmail(mastersEmail);

        final String text = buildBookingInfoText(master, callbackData);
        final InlineKeyboardMarkup keyboardMarkup = buildKeyboardMarkup(callbackData, mastersEmail);
        final EditMessageText responseMessage = buildEditedMessageText(chatId, messageId, text, keyboardMarkup);
        return singletonList(responseMessage);
    }

    private InlineKeyboardMarkup buildKeyboardMarkup(final String callbackData, final String mastersEmail) {
        final String callbackDataForApproveButton = buildCallbackData(callbackData, mastersEmail);
        final InlineKeyboardButton approveButton = buildInlineKeyboardButton("Approve", callbackDataForApproveButton);
        final InlineKeyboardButton declineButton = buildInlineKeyboardButton("Decline", MENU.getPatternTemplate());
        return buildInlineKeyboardMarkupWithSequentialButtons(approveButton, declineButton);
    }

    private String buildCallbackData(final String callbackText, final String mastersEmail) {
        final int dayOfMonth = getDayOfMonthValueFromCommand(callbackText);
        final int month = getMonthValueFromCommand(callbackText);
        final int year = getYearValueFromCommand(callbackText);
        final int hours = getHoursValueFromCommand(callbackText);
        final int minutes = getMinutesValueFromCommand(callbackText);
        return format(BOOK_TIME.getPatternTemplate(), mastersEmail, dayOfMonth, month, year, hours, minutes);
    }

    private String buildBookingInfoText(final Master master, final String callbackText) {
        final String firstName = master.getFirstName();
        final String lastName = master.getLastName();
        final int dayOfMonth = getDayOfMonthValueFromCommand(callbackText);
        final int month = getMonthValueFromCommand(callbackText);
        final int year = getYearValueFromCommand(callbackText);
        final int hours = getHoursValueFromCommand(callbackText);
        final int minutes = getMinutesValueFromCommand(callbackText);
        return format("""
                <b>Booking confirmation</b>
                Master: %s %s
                Date: %s.%s.%s
                Time: %s:%s
                """, firstName, lastName, dayOfMonth, month, year, hours, minutes
        );
    }
}
