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
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final String callbackText = callbackQuery.getData();
        final String mastersEmail = getEmailFromCommand(callbackText);

        final Master master = masterService.getByEmail(mastersEmail);

        return buildMessage(chatId, messageId, master, callbackText);
    }

    @Override
    public BotCommandPattern getCommandPattern() {
        return CHOOSE_TIME;
    }

    private List<EditMessageText> buildMessage(
            final Long chatId, final Integer messageId, final Master master, final String callbackText) {
        final String text = buildBookingInfoText(master, callbackText);
        final String callbackData = buildCallbackData(callbackText, master.getEmail());
        final InlineKeyboardButton approveButton = buildInlineKeyboardButton("Approve", callbackData);
        final InlineKeyboardButton declineButton = buildInlineKeyboardButton("Decline", MENU.getPatternTemplate());
        final InlineKeyboardMarkup keyboardMarkup = buildInlineKeyboardMarkupWithSequentialButtons(approveButton, declineButton);
        final EditMessageText message = buildEditedMessageText(chatId, messageId, text, keyboardMarkup);
        return singletonList(message);
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
        return format(
                """
                        <b>Booking confirmation</b>
                        Master's first name: %s
                        Master's last name: %s
                        Date: %s-%s-%s
                        Time: %s:%s""",
                firstName, lastName, dayOfMonth, month, year, hours, minutes
        );
    }
}
